package com.example.apptemplates.presentation.main.reservation

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.RecurrencePattern
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.home.ActiveReservations
import com.example.apptemplates.presentation.main.home.ActiveRooms
import com.example.apptemplates.presentation.main.home.domain.AddRoomUseCase
import com.example.apptemplates.presentation.main.reservation.domain.AddLessonUseCase
import com.example.apptemplates.presentation.main.reservation.domain.AddReservationUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchAvailableRoomsUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchLessonsUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchRecurringReservationsUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchStandardReservationsUseCase
import com.example.apptemplates.presentation.main.temp.ReservationError
import com.example.apptemplates.viewmodel.MainViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
class ReservationViewModel(
    private val fetchStandardReservationsUseCase: FetchStandardReservationsUseCase = FetchStandardReservationsUseCase(),
    private val fetchRecurringReservationsUseCase: FetchRecurringReservationsUseCase = FetchRecurringReservationsUseCase(),
    private val fetchRooms: FetchAvailableRoomsUseCase = FetchAvailableRoomsUseCase(),
    private val fetchLessons: FetchLessonsUseCase = FetchLessonsUseCase(),
    private val addReservation: AddReservationUseCase = AddReservationUseCase(),
    private val addLesson: AddLessonUseCase = AddLessonUseCase(),
    private val addRoom: AddRoomUseCase = AddRoomUseCase(),
) : MainViewModel() {


    /*init {
        viewModelScope.launch {

            val randomRoomIds = generateRandomRoomIds(40)

            addRoom(generateRandomRooms(randomRoomIds))

            val lessons = generateRealisticLessonsForRooms(randomRoomIds)
            addLesson(lessons)

            addReservation(generateRandomReservations(randomRoomIds, lessons))
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


    // Fetch and combine room IDs for standard and recurring reservations
    private suspend fun fetchCombinedRoomIds(newReservation: Reservation): Set<String> {
        val standardReservationsRoomIds = fetchStandardReservationsUseCase(newReservation)
        val recurringReservationsRoomIds =
            fetchRecurringReservationsUseCase(newReservation, standardReservationsRoomIds)
        return standardReservationsRoomIds + recurringReservationsRoomIds
    }

    // Fetch lessons and check for conflicts with the new reservation
    private suspend fun fetchLessons(
        roomIds: Set<String>,
        newReservation: Reservation
    ): Set<String> {
        return fetchLessons(roomIds.toList(), newReservation).toSet()
    }

    // Fetch available rooms by excluding conflicting ones and applying filters
    private suspend fun fetchAvailableRooms(excludedRoomIds: Set<String>): List<Room> {
        val selectedFloor = _state.value.selectedFloor
        val selectedAttendees = _state.value.selectedAttendees
        val selectedEquipment = _state.value.selectedEquipment.toSet()

        // Fetch initial room list based on excluded rooms
        val rooms = fetchRooms(excludedRoomIds.toList())

        Log.i("ROOMS", "Available rooms before filtering ${rooms.size}")

        ActiveRooms.addRooms(rooms)

        // Apply filters for capacity, floor, and equipment
        return rooms.filter { room ->
            meetsCapacityRequirements(room, selectedAttendees) &&
                    meetsFloorRequirement(room, selectedFloor) &&
                    meetsEquipmentRequirements(room, selectedEquipment)
        }
    }

    // Helper function to check room capacity
    private fun meetsCapacityRequirements(room: Room, requiredCapacity: Int): Boolean {
        return room.capacity >= requiredCapacity
    }

    // Helper function to check if room matches the selected floor
    private fun meetsFloorRequirement(room: Room, selectedFloor: Int?): Boolean {
        return selectedFloor == null || room.floor == selectedFloor
    }

    // Helper function to check if room has all required equipment
    private fun meetsEquipmentRequirements(
        room: Room,
        selectedEquipment: Set<EquipmentType>
    ): Boolean {
        return selectedEquipment.isEmpty() || selectedEquipment.any { it in room.equipment.map { e -> e.type } }
    }


    // Log debug results for reservations and lessons
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

    fun changeTime(time: TimePickerState) {
        _state.value = _state.value.copy(
            selectedTime = LocalTime.of(
                time.hour, time.minute
            )
        )
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
        _state.value = _state.value.copy(recurringFrequency = frequency)
    }

    fun changeEndTime(timePickerState: TimePickerState) {
        _state.update {
            it.copy(
                selectedEndTime = LocalTime.of(
                    timePickerState.hour, timePickerState.minute
                )
            )
        }
    }

    fun changeEndDate(date: LocalDate) {
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
    }

    fun getMaximumAttendees(): Int {
        return permission.getMaximumAttendees()
    }

}



