package com.example.apptemplates.presentation.main.home

import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.data.room.Equipment
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.home.domain.FetchReservationsUseCase
import com.example.apptemplates.presentation.main.home.domain.FetchRoomsUseCase
import com.example.apptemplates.presentation.main.home.domain.ReservationUseCase
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.presentation.main.temp.TimePeriod
import com.example.apptemplates.viewmodel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeViewModel(
    private val fetchRoomsUseCase: FetchRoomsUseCase = FetchRoomsUseCase(),
    private val fetchReservationsUseCase: FetchReservationsUseCase = FetchReservationsUseCase(),
    private val reservationUseCase: ReservationUseCase = ReservationUseCase()
) : MainViewModel() {

    private val _stateReservations = ActiveReservations.getReservations()
    private val _stateRooms = ActiveRooms.getRooms()

    init {
        if (_stateReservations.value.isEmpty() || _stateRooms.value.isEmpty()) {
            loadInitialData()
        } else {
            updateDataIfNeeded()
        }
    }

    private fun loadInitialData() {

        instantUserReload()

        val user = _state.value.user ?: ActiveUser.getUser()
            .also { user -> _state.update { it.copy(user = user) } }

        if (permission.cannotReserve() && !ActiveUser.isUserVerified()) {
            handleError(UiError.PermissionError("Potwierdz swój email aby kontynuować"))
            return
        }

        wrapWithLoadingState(
            successState = { data ->
                _state.update {
                    it.copy(
                        rooms = data.first,
                        reservations = data.second.filter { r -> r.status != ReservationStatus.CANCELED },
                        reservationsLeft = data.second.filter { r -> r.status != ReservationStatus.CANCELED }.size,
                        maxReservations = permission.getMaxReservationCount(),
                        lastReservationDate = data.second.filter { r -> r.status != ReservationStatus.CANCELED }
                            .maxOfOrNull { r -> r.createdAt }
                            ?.parseLastDate(),
                        screenState = ScreenState.Success
                    )
                }
                ActiveReservations.setReservations(data.second)
                ActiveRooms.setRooms(data.first)
                filterReservations()
            },
            errorState = { message ->
                _state.update {
                    it.copy(
                        screenState = ScreenState.Error(UiError.DatabaseError(message))
                    )
                }
            }
        ) {


            val reservations = fetchReservationsUseCase(user!!.uid)
            val rooms = fetchRoomsUseCase(reservations.map { it.roomId }.distinct().toSet())
            rooms to reservations
        }
    }

    private fun updateDataIfNeeded() {

        if (_state.value.reservations.isNotEmpty() && _state.value.rooms.isNotEmpty()) return

        instantUserReload()

        _state.value.user ?: ActiveUser.getUser()
            .also { user -> _state.update { it.copy(user = user) } }

        if (permission.cannotReserve()) {
            handleError(UiError.PermissionError("Potwierdz swój email aby kontynuować"))
            return
        }

        wrapWithLoadingState(
            successState = {
                _state.update { it.copy(screenState = ScreenState.Success) }
                filterReservations()
            },
            errorState = { message ->
                _state.update {
                    it.copy(
                        screenState = ScreenState.Error(
                            UiError.DatabaseError(
                                message
                            )
                        )
                    )
                }
            }
        ) {
            _state.update {
                it.copy(
                    reservations = _stateReservations.value,
                    rooms = _stateRooms.value,
                    reservationsLeft = _stateReservations.value.size,
                    maxReservations = permission.getMaxReservationCount(),
                    lastReservationDate = _stateReservations.value.maxOfOrNull { r -> r.createdAt }
                        ?.parseLastDate()
                )
            }
        }

    }


    fun filterReservations() {
        val periods = LocalDate.now().convertPeriodsToMillis()

        val filteredReservations = if (_state.value.reservationTimePeriods != TimePeriod.ALL) {
            getNonRecurringReservations(periods) + getRecurringReservations(periods)
        } else {
            _state.value.reservations
        }
        _state.update(filteredReservations)
    }


    fun changeReservationTimePeriod(timePeriod: TimePeriod) {
        _state.update { it.copy(reservationTimePeriods = timePeriod) }
    }


    private fun isOverlapping(reservation: Reservation, period: Pair<Long, Long>): Boolean {
        val recurrencePattern = reservation.recurrencePattern ?: return false

        val reservationStart =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(reservation.startTime), ZoneOffset.UTC)
        val periodStart =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(period.first), ZoneOffset.UTC)

        val recurrenceIntervalDays = when (recurrencePattern.frequency) {
            RecurrenceFrequency.WEEKLY -> 7
            RecurrenceFrequency.BIWEEKLY -> 14
            RecurrenceFrequency.MONTHLY -> 28
        }

        val daysDifference = Duration.between(
            reservationStart.toLocalDate().atStartOfDay(),
            periodStart.toLocalDate().atStartOfDay()
        ).toDays()

        return daysDifference % recurrenceIntervalDays == 0L && daysDifference >= 0
    }

    private fun MutableStateFlow<MainUiState>.update(filteredReservations: List<Reservation>) {
        this.update { state ->
            state.copy(
                filteredReservations = filteredReservations.distinctBy { it.id }
                    .sortedWith(compareBy<Reservation> { it.status }.thenBy { it.endTime }),
            )
        }
    }

    private fun Long.parseLastDate(): String {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC).format(
            DateTimeFormatter.ofPattern("dd MMMM HH:mm").withLocale(Locale.forLanguageTag("pl"))
        )
    }


    private fun getNonRecurringReservations(period: Pair<Long, Long>): List<Reservation> {
        return _state.value.reservations.filter { reservation ->
            !reservation.isRecurring &&
                    reservation.startTime in period.first..period.second
        }
    }

    private fun getRecurringReservations(period: Pair<Long, Long>): List<Reservation> {
        return _state.value.reservations.filter { reservation ->
            isOverlapping(reservation, period)
        }
    }

    private fun LocalDate.convertPeriodsToMillis(): Pair<Long, Long> {

        val timePeriod = _state.value.reservationTimePeriods
        val start: LocalDateTime
        val end: LocalDateTime


        when (timePeriod) {
            TimePeriod.TODAY -> {
                start = this.atStartOfDay()
                end = this.atTime(LocalTime.MAX)
            }

            TimePeriod.TOMORROW -> {
                start = this.plusDays(1).atStartOfDay()
                end = this.plusDays(1).atTime(LocalTime.MAX)
            }

            TimePeriod.WEEK -> {
                start = this/*.with(DayOfWeek.MONDAY)*/.atStartOfDay()
                end = this/*.with(DayOfWeek.SUNDAY)*/.plusDays(7).atTime(LocalTime.MAX)
            }

            TimePeriod.MONTH -> {
                start = this/*.withDayOfMonth(1)*/.atStartOfDay()
                end =
                    this/*.withDayOfMonth(this.lengthOfMonth())*/.plusWeeks(4).atTime(LocalTime.MAX)
            }

            TimePeriod.ALL -> {
                return Pair(0, 0)
            }
        }


        return start.toInstant(ZoneOffset.UTC).toEpochMilli() to end.toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }


    fun showDialog(show: Boolean = true) {
        _state.update { it.copy(showReservationDetailsDialog = show) }
    }

    fun setSelectedReservation(reservation: Reservation? = null) {
        _state.update { it.copy(selectedReservation = reservation) }
    }


    fun cancelReservation() {
        if (_state.value.selectedReservation == null) {
            handleError(UiError.DatabaseError("Nie wybrano rezerwacji"))
            return
        }

        wrapWithLoadingState(
            successState = {

                ActiveReservations.removeReservation(_state.value.selectedReservation!!)
                setSelectedReservation()

                _state.update {
                    it.copy(
                        screenState = ScreenState.Success,
                        reservations = _stateReservations.value,
                        rooms = _stateRooms.value,
                        reservationsLeft = _stateReservations.value.size,
                        maxReservations = permission.getMaxReservationCount(),
                        lastReservationDate = _stateReservations.value.maxOfOrNull { r -> r.createdAt }
                            ?.parseLastDate()
                    )
                }

                filterReservations()
            },
            errorState = { message ->
                _state.update {
                    it.copy(
                        screenState = ScreenState.Error(UiError.DatabaseError(message))
                    )
                }
            },
            {
                reservationUseCase(
                    _state.value.selectedReservation!!.id,
                    //_state.value.selectedAdditionalEquipment.toList()
                )
            }
        )
    }

    fun requestAdditionalEquipment() {
        if (_state.value.selectedReservation == null) {
            handleError(UiError.DatabaseError("Nie wybrano rezerwacji"))
            return
        }

        if (_state.value.selectedAdditionalEquipment.isEmpty()) {
            handleError(UiError.DatabaseError("Nie wybrano dodatkowego sprzętu"))
            return
        }

        wrapWithLoadingState(
            successState = {

                ActiveReservations.updateReservationEquipment(
                    _state.value.selectedReservation!!,
                    _state.value.selectedAdditionalEquipment
                )
                setSelectedReservation()

                _state.update {
                    it.copy(
                        screenState = ScreenState.Success,
                    )
                }

            },
            errorState = { message ->
                _state.update {
                    it.copy(
                        screenState = ScreenState.Error(UiError.DatabaseError(message))
                    )
                }
            },
            {
                reservationUseCase(
                    _state.value.selectedReservation!!.id,
                    _state.value.selectedAdditionalEquipment
                )
            }
        )


    }

}


object ActiveReservations {
    private val reservations = MutableStateFlow<List<Reservation>>(emptyList())
    private val allReservations = MutableStateFlow<List<Reservation>>(emptyList())

    fun getReservations(): MutableStateFlow<List<Reservation>> {
        return reservations
    }

    fun getAllReservations(): MutableStateFlow<List<Reservation>> {
        return allReservations
    }

    fun setReservations(newReservations: List<Reservation>) {
        allReservations.value = newReservations
        reservations.value = newReservations.filter { it.status != ReservationStatus.CANCELED }
    }

    fun addReservation(newReservation: Reservation) {
        val currentReservations = reservations.value.toMutableList()
        val currentAllReservations = allReservations.value.toMutableList()
        currentReservations.add(newReservation)
        currentAllReservations.add(newReservation)
        reservations.value = currentReservations
        allReservations.value = currentAllReservations
    }

    fun removeReservation(reservationToRemove: Reservation) {
        val currentReservations = reservations.value.toMutableList()
        currentReservations.remove(reservationToRemove)
        reservations.value = currentReservations
    }

    fun updateReservationEquipment(
        reservationToUpdate: Reservation,
        equipment: List<EquipmentType>
    ) {
        val currentReservations = reservations.value.toMutableList()
        val currentAllReservation = allReservations.value.toMutableList()
        val index = currentReservations.indexOfFirst { it.id == reservationToUpdate.id }

        if (index != -1) {
            currentReservations[index] =
                reservationToUpdate.copy(additionalEquipment = equipment.map { Equipment(it) })
            reservations.value = currentReservations
        }
    }
}

object ActiveRooms {
    private val rooms = MutableStateFlow<List<Room>>(emptyList())

    fun getRooms(): MutableStateFlow<List<Room>> {
        return rooms
    }

    fun setRooms(newRooms: List<Room>) {
        rooms.value = newRooms
    }

    fun addRooms(newRooms: List<Room>) {
        val currentRooms = rooms.value.toMutableList()
        currentRooms.addAll(newRooms)
        rooms.value = currentRooms
    }
}

sealed class UiEvent {
    data object NavigateToProfile : UiEvent()
    data class ShowSnackBar(val message: String) : UiEvent()
}

