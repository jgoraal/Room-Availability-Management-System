package com.example.apptemplates.presentation.screens.home.reservation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.domain.model.EquipmentType
import com.example.apptemplates.domain.model.RecurrenceFrequency
import com.example.apptemplates.domain.model.RecurrencePattern
import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.domain.model.ReservationStatus
import com.example.apptemplates.domain.model.Room
import com.example.apptemplates.domain.usecase.ActiveUser
import com.example.apptemplates.domain.usecase.AddLessonUseCase
import com.example.apptemplates.domain.usecase.AddReservationUseCase
import com.example.apptemplates.domain.usecase.AddRoomUseCase
import com.example.apptemplates.domain.usecase.AddUserUseCase
import com.example.apptemplates.domain.usecase.FetchAvailableRoomsUseCase
import com.example.apptemplates.domain.usecase.FetchLessonsUseCase
import com.example.apptemplates.domain.usecase.FetchRecurringReservationsUseCase
import com.example.apptemplates.domain.usecase.FetchStandardReservationsUseCase
import com.example.apptemplates.domain.usecase.QuickReservation
import com.example.apptemplates.domain.usecase.StateLoader
import com.example.apptemplates.presentation.screens.home.base.ReservationError
import com.example.apptemplates.presentation.screens.home.start.ActiveReservations
import com.example.apptemplates.presentation.screens.home.start.ActiveRooms
import com.example.apptemplates.presentation.state.ScreenState
import com.example.apptemplates.presentation.state.UiError
import com.example.apptemplates.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset


class ReservationViewModel(
    private val fetchStandardReservationsUseCase: FetchStandardReservationsUseCase = FetchStandardReservationsUseCase(),
    private val fetchRecurringReservationsUseCase: FetchRecurringReservationsUseCase = FetchRecurringReservationsUseCase(),
    private val fetchRooms: FetchAvailableRoomsUseCase = FetchAvailableRoomsUseCase(),
    private val fetchLessons: FetchLessonsUseCase = FetchLessonsUseCase(),
    private val addReservation: AddReservationUseCase = AddReservationUseCase(),
    private val addLesson: AddLessonUseCase = AddLessonUseCase(),
    private val addRoom: AddRoomUseCase = AddRoomUseCase(),
    private val addUser: AddUserUseCase = AddUserUseCase(),
) : MainViewModel() {

    private val savedScreenState = StateLoader.stateReservationScreen

    fun saveState() {
        StateLoader.updateReservationScreenState(_state.value)
    }

    init {
        if (savedScreenState.value != null) {
            _state.value = savedScreenState.value!!
        }
    }


    /*init {
        viewModelScope.launch {

            val randomRoomIds = generateRandomRoomIds(40)

            addRoom(generateRandomRooms(randomRoomIds))

            val users = generateRandomUsers(100)

            addUser(users)

            val userIds = users.getUserIds()

            val lessons = generateRealisticLessonsForRooms(randomRoomIds, userIds)
            addLesson(lessons)

            addReservation(generateRandomReservations(randomRoomIds, userIds, lessons))
        }
    }*/


    fun findRooms() {

        if (!canUserMakeReservation()) {

            handleError(UiError.PermissionError("Przekroczono limit rezerwacji"))
            return

        }

        viewModelScope.launch {
            wrapWithLoadingState(
                successState = { rooms ->
                    _state.update {
                        it.copy(
                            screenState = if (rooms.isNotEmpty()) ScreenState.Success else ScreenState.Idle,
                            availableRooms = rooms
                        )
                    }

                    if (rooms.isEmpty()) {
                        handleError(UiError.DatabaseError("Brak dostępnych sal!"))
                    }

                    saveState()

                },
                errorState = { message ->
                    _state.update {
                        it.copy(screenState = ScreenState.Error(UiError.DatabaseError(message)))
                    }
                },
                {

                    val newReservation = createNewReservation()

                    val combinedRoomIds = fetchCombinedRoomIds(newReservation)

                    val overlappingLessonsRoomIds = fetchLessons(combinedRoomIds, newReservation)

                    val allConflictingRoomIds = combinedRoomIds + overlappingLessonsRoomIds

                    val availableRooms = fetchAvailableRooms(allConflictingRoomIds)

                    logRoomResults(availableRooms, combinedRoomIds, overlappingLessonsRoomIds)

                    availableRooms
                }
            )
        }
    }

    private fun canUserMakeReservation(): Boolean {
        return permission.canReserve(_state.value.isRecurring)
    }

    fun checkMaxReservationTime(minutesBetween: Long): Boolean {
        return permission.hasExceededMaxReservationTime(minutesBetween.toInt())
    }

    fun canUserMakeRecurringReservation(): Boolean {
        return permission.canMakeRecurringReservation()
    }


    private suspend fun fetchCombinedRoomIds(newReservation: Reservation): Set<String> {
        val standardReservationsRoomIds = fetchStandardReservationsUseCase(newReservation)
        val recurringReservationsRoomIds =
            fetchRecurringReservationsUseCase(newReservation, standardReservationsRoomIds)
        return standardReservationsRoomIds + recurringReservationsRoomIds
    }


    private suspend fun fetchLessons(
        roomIds: Set<String>,
        newReservation: Reservation
    ): Set<String> {
        return fetchLessons(roomIds.toList(), newReservation).toSet()
    }


    private suspend fun fetchAvailableRooms(excludedRoomIds: Set<String>): List<Room> {
        val selectedFloor = _state.value.selectedFloor
        val selectedAttendees = _state.value.selectedAttendees
        val selectedEquipment = _state.value.selectedEquipment.toSet()


        val rooms = fetchRooms(excludedRoomIds.toList())

        Log.i("ROOMS", "Available rooms before filtering ${rooms.size}")

        ActiveRooms.addRooms(rooms)


        return rooms.filter { room ->
            meetsCapacityRequirements(room, selectedAttendees) &&
                    meetsFloorRequirement(room, selectedFloor) &&
                    meetsEquipmentRequirements(room, selectedEquipment)
        }
    }


    private fun meetsCapacityRequirements(room: Room, requiredCapacity: Int): Boolean {
        return room.capacity >= requiredCapacity
    }


    private fun meetsFloorRequirement(room: Room, selectedFloor: Int?): Boolean {
        return selectedFloor == null || room.floor == selectedFloor
    }


    private fun meetsEquipmentRequirements(
        room: Room,
        selectedEquipment: Set<EquipmentType>
    ): Boolean {
        return selectedEquipment.isEmpty() || selectedEquipment.all { it in room.equipment.map { e -> e.type } }
    }


    private fun logRoomResults(
        availableRooms: List<Room>,
        combinedRoomIds: Set<String>,
        lessonsRoomIds: Set<String>
    ) {
        Log.i("ROOMS", "Combined room IDs (reservations): ${combinedRoomIds.size}")
        Log.i("ROOMS", "Overlapping lessons room IDs: ${lessonsRoomIds.size}")
        Log.i("ROOMS", "Available rooms: ${availableRooms.size}")
    }

    private fun createNewReservation(): Reservation {
        return Reservation(
            userId = ActiveUser.getUser()!!.uid,
            createdAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
            startTime = LocalDateTime.of(_state.value.selectedDate!!, _state.value.selectedTime!!)
                .toInstant(ZoneOffset.UTC).toEpochMilli(),
            endTime = LocalDateTime.of(_state.value.selectedDate!!, _state.value.selectedEndTime!!)
                .toInstant(ZoneOffset.UTC).toEpochMilli(),
            dayOfWeek = _state.value.selectedDate!!.dayOfWeek,
            participants = _state.value.selectedAttendees,
            isRecurring = _state.value.isRecurring,
            recurrencePattern = if (_state.value.isRecurring) RecurrencePattern(
                frequency = _state.value.recurringFrequency ?: RecurrenceFrequency.WEEKLY,
                endDate = _state.value.endRecurrenceDate!!.atStartOfDay().toInstant(ZoneOffset.UTC)
                    .toEpochMilli()
            ) else null,
        )

    }


    fun confirmReservation(navigateOnSuccess: () -> Unit) {

        if (!canUserMakeReservation()) {
            handleError(UiError.PermissionError("Przekroczono limit rezerwacji"))
            return
        }


        wrapWithLoadingState(
            successState = {
                _state.update {
                    it.copy(
                        screenState = ScreenState.Success,
                    )
                }

                navigateOnSuccess()
            },
            errorState = { message ->
                _state.update {
                    it.copy(screenState = ScreenState.Error(UiError.DatabaseError(message)))
                }
            },
            {
                val newReservation = createNewReservation().copy(
                    roomId = _state.value.selectedRoomToReserve!!.id,
                    status = if (permission.requiresAdminApproval()) ReservationStatus.PENDING else ReservationStatus.CONFIRMED
                )

                ActiveReservations.addReservation(newReservation)

                addReservation(newReservation)
            }
        )


    }


    fun changeDate(date: LocalDate) {
        _state.value = _state.value.copy(selectedDate = date)
    }

    fun changeTime(time: LocalTime) {
        _state.update {
            it.copy(
                selectedTime = time
            )
        }
    }

    fun changeAttendees(attendees: Int) {

        if (!permission.checkMaxAttendeeCount(attendees)) {
            handleError(UiError.PermissionError("Przekroczono limit uczestników"))
            return
        }

        _state.value = _state.value.copy(selectedAttendees = attendees)
    }

    fun changeRecurring(recurring: Boolean) {
        _state.value = _state.value.copy(isRecurring = recurring)
    }

    fun changeFrequency(frequency: RecurrenceFrequency) {
        _state.value =
            _state.value.copy(recurringFrequency = frequency, duration = adjustDuration(frequency))
    }

    private fun adjustDuration(frequency: RecurrenceFrequency): Int {
        val currentFrequency = _state.value.recurringFrequency ?: RecurrenceFrequency.WEEKLY
        val duration = _state.value.duration


        val currentFactor = when (currentFrequency) {
            RecurrenceFrequency.WEEKLY -> 1
            RecurrenceFrequency.BIWEEKLY -> 2
            RecurrenceFrequency.MONTHLY -> 4
        }

        val newFactor = when (frequency) {
            RecurrenceFrequency.WEEKLY -> 1
            RecurrenceFrequency.BIWEEKLY -> 2
            RecurrenceFrequency.MONTHLY -> 4
        }


        return (duration * currentFactor / newFactor).coerceAtLeast(1)
    }


    fun changeEndTime(time: LocalTime) {
        _state.update {
            it.copy(
                selectedEndTime = time
            )
        }
    }

    fun changeReservationTimes(startTime: LocalTime, endTime: LocalTime) {
        _state.update {
            it.copy(
                selectedTime = startTime,
                selectedEndTime = endTime
            )
        }
    }

    fun changeEndDate(date: LocalDate?) {
        _state.update { it.copy(endRecurrenceDate = date) }
    }

    fun updateSelectedEquipment(updatedEquipment: List<EquipmentType>) {
        _state.update { it.copy(selectedEquipment = updatedEquipment) }
    }

    fun updateSelectedFloor(selectedFloor: Int?) {
        _state.update { it.copy(selectedFloor = selectedFloor) }
    }

    fun errorState(error: ReservationError?) {
        _state.update { it.copy(reservationError = error) }
    }


    fun changeSelectedRoom(room: Room?) {
        _state.update { it.copy(selectedRoomToReserve = room) }
        ActiveRooms.addRoom(room)
    }

    fun getMaximumAttendees(): Int {
        return permission.getMaximumAttendees()
    }

    fun checkIfQuickReservationIsReady() {
        if (QuickReservation.getSelectedDate() != null && QuickReservation.getStartTime() != null) {
            _state.update {
                it.copy(
                    selectedDate = QuickReservation.getSelectedDate(),
                    selectedTime = QuickReservation.getStartTime(),
                    selectedEndTime = QuickReservation.getEndTime(),
                    showTimePicker = true,
                    showRecurringPicker = true,
                    showAttendeesPicker = true,
                    showOtherFiltersPicker = true,
                    availableRooms = if (QuickReservation.getSelectedRoom() != null) listOf(
                        QuickReservation.getSelectedRoom()!!
                    ) else emptyList()
                )
            }
        }
    }

    fun updateIgnoreEquipment(ignoreEquipment: Boolean) {
        _state.update { it.copy(ignoreEquipment = ignoreEquipment) }
    }

    fun updateEndRecurrenceDate() {

        val freq = _state.value.recurringFrequency ?: RecurrenceFrequency.WEEKLY
        val addWeeks = when (freq) {
            RecurrenceFrequency.WEEKLY -> 1
            RecurrenceFrequency.BIWEEKLY -> 2
            RecurrenceFrequency.MONTHLY -> 4
        }

        if (_state.value.selectedDate == null) return

        val updatedEndRecurrenceDate = _state.value.selectedDate!!.plusWeeks(addWeeks.toLong())

        _state.update {
            it.copy(
                endRecurrenceDate = updatedEndRecurrenceDate,
                recurringFrequency = freq,
            )
        }
    }


    fun updateDuration(step: Int) {
        _state.update { it.copy(duration = (it.duration + step)) }
    }

    fun canAddDuration(): Boolean {
        val endDate = _state.value.endRecurrenceDate ?: return false
        val selectedDate = _state.value.selectedDate ?: return false

        val addWeeks = when (_state.value.recurringFrequency) {
            RecurrenceFrequency.WEEKLY -> 1
            RecurrenceFrequency.BIWEEKLY -> 2
            RecurrenceFrequency.MONTHLY -> 4
            null -> return false
        }

        return endDate.plusWeeks(addWeeks.toLong()).isBefore(selectedDate.plusMonths(4))
    }


    fun getDurationText(): String {
        val frequency = _state.value.recurringFrequency ?: RecurrenceFrequency.WEEKLY
        val duration = _state.value.duration

        return when (frequency) {

            RecurrenceFrequency.MONTHLY -> "$duration ${
                getPluralForm(
                    duration,
                    "Miesiąc",
                    "Miesiące",
                    "Miesięcy"
                )
            }"

            else -> "$duration ${
                getPluralForm(
                    duration,
                    "Tydzień",
                    "Tygodnie",
                    "Tygodni"
                )
            }"
        }
    }


    private fun getPluralForm(
        value: Int,
        singular: String,
        pluralFew: String,
        pluralMany: String
    ): String {
        return when (value) {
            1 -> singular
            in 2..4 -> pluralFew
            else -> pluralMany
        }
    }

    fun clearAvailableRooms() {
        _state.update { it.copy(availableRooms = emptyList()) }
    }


    fun updateShowTimePicker(value: Boolean) {
        _state.update { it.copy(showTimePicker = value) }
    }

    fun updateShowAttendeesPicker(value: Boolean) {
        _state.update { it.copy(showAttendeesPicker = value) }
    }

    fun updateShowRecurringPicker(value: Boolean) {
        _state.update { it.copy(showRecurringPicker = value) }
    }

    fun updateShowOtherFiltersPicker(value: Boolean) {
        _state.update { it.copy(showOtherFiltersPicker = value) }
    }

    fun updateVisibility(value: Boolean) {
        _state.update {
            it.copy(
                showTimePicker = value,
                showAttendeesPicker = value,
                showRecurringPicker = value,
                showOtherFiltersPicker = value
            )
        }
    }


}



