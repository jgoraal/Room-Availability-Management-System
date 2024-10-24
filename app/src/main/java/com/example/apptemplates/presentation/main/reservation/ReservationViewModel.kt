package com.example.apptemplates.presentation.main.reservation

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.home.domain.AddRoomUseCase
import com.example.apptemplates.presentation.main.reservation.domain.AddLessonUseCase
import com.example.apptemplates.presentation.main.reservation.domain.AddReservationUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchAvailableRoomsUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchLessonsUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchRecurringReservationsUseCase
import com.example.apptemplates.presentation.main.reservation.domain.FetchStandardReservationsUseCase
import com.example.apptemplates.presentation.main.reservation.generator.generateTestRecurringReservation
import com.example.apptemplates.presentation.main.reservation.generator.generateTestReservation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
class ReservationViewModel(
    private val fetchStandardReservationsUseCase: FetchStandardReservationsUseCase = FetchStandardReservationsUseCase(),
    private val fetchRecurringReservationsUseCase: FetchRecurringReservationsUseCase = FetchRecurringReservationsUseCase(),
    private val fetchRooms: FetchAvailableRoomsUseCase = FetchAvailableRoomsUseCase(),
    private val fetchLessons: FetchLessonsUseCase = FetchLessonsUseCase(),
    private val addReservation: AddReservationUseCase = AddReservationUseCase(),
    private val addLesson: AddLessonUseCase = AddLessonUseCase(),
    private val addRoom: AddRoomUseCase = AddRoomUseCase(),
) : ViewModel() {

    private val _state = MutableStateFlow(ReservationState())
    val state: StateFlow<ReservationState> = _state.asStateFlow()


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

        viewModelScope.launch {
            wrapWithLoadingState(
                successState = { rooms ->
                    _state.update {
                        it.copy(
                            screenState = ScreenState.Success,
                            availableRooms = rooms
                        )
                    }
                },
                errorState = { message ->
                    _state.update {
                        it.copy(screenState = ScreenState.Error(UiError.DatabaseError(message)))
                    }
                },
                {
                    val isRecurring = listOf(true, false).random()
                    val newReservation =
                        if (isRecurring) generateTestReservation() else generateTestRecurringReservation()

                    val combinedRoomIds = fetchCombinedRoomIds(newReservation)

                    val overlappingLessonsRoomIds = fetchLessons(combinedRoomIds, newReservation)

                    val allConflictingRoomIds = combinedRoomIds + overlappingLessonsRoomIds

                    val availableRooms = fetchRooms(allConflictingRoomIds)

                    logRoomResults(availableRooms, combinedRoomIds, overlappingLessonsRoomIds)

                    availableRooms
                }
            )
        }
    }


    private suspend fun <T> wrapWithLoadingState(
        successState: (T) -> Unit, errorState: (String) -> Unit, block: suspend () -> T
    ) {
        _state.update { it.copy(screenState = ScreenState.Loading) }

        try {
            val result = block()
            successState(result)
        } catch (e: Exception) {
            errorState(e.localizedMessage ?: "An unknown error occurred")

            Log.e("Error", e.printStackTrace().toString())
        }

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

    // Fetch available rooms by excluding conflicting ones
    private suspend fun fetchRooms(excludedRoomIds: Set<String>): List<Room> {
        return fetchRooms(excludedRoomIds.toList()).filter { room ->
            room.capacity >= _state.value.selectedAttendees && // Ensure room has enough capacity
                    room.floor == _state.value.selectedFloor && // Filter by selected floor
                    room.equipment.all { it.type in _state.value.selectedEquipment } // Filter rooms with selected equipment
        }
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

    fun updateSelectedEquipment(updatedEquipment: List<EquipmentType>) {
        _state.update { it.copy(selectedEquipment = updatedEquipment) }
    }

    fun updateSelectedFloor(selectedFloor: Int) {
        _state.update { it.copy(selectedFloor = selectedFloor) }
    }

}



