package com.example.apptemplates.presentation.main.room_availability

import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.viewmodel.MainViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate

class RoomAvailabilityViewModel(

) : MainViewModel() {

    /*private val _state = MutableStateFlow(RoomAvailabilityState())
    val state = _state.asStateFlow()


    private suspend inline fun <T> wrapWithLoadingState(
        crossinline successState: (T) -> Unit,
        crossinline errorState: (String) -> Unit,
        crossinline block: suspend () -> T
    ) {

        viewModelScope.launch {

            _state.update { it.copy(screenState = ScreenState.Loading) }

            try {
                val result = block()
                successState(result)
            } catch (e: Exception) {
                errorState(e.localizedMessage ?: "An unknown error occurred")

                Log.e("Error", e.printStackTrace().toString())
            }

        }


    }*/


    fun fetchReservations(floor: String, selectedRoom: String, selectedDate: LocalDate) {
        viewModelScope.launch {
            val reservations = loadReservationsForRoom(selectedRoom, selectedDate)
            _state.update {
                it.copy(
                    selectedFloorName = floor,
                    selectedRoomNumber = selectedRoom,
                    selectedDateCheck = selectedDate,
                    reservations = reservations
                )
            }
        }
    }

    // Simulate loading reservations with proper epoch time for startTime and endTime
    private fun loadReservationsForRoom(room: String, date: LocalDate): List<Reservation> {
        return listOf(
            Reservation(
                id = "1",
                roomId = room,
                startTime = Instant.now().epochSecond,
                endTime = Instant.now().plusSeconds(3600).epochSecond
            ),
            Reservation(
                id = "2",
                roomId = room,
                startTime = Instant.now().plusSeconds(7200).epochSecond,
                endTime = Instant.now().plusSeconds(10800).epochSecond
            )
        )
    }


}

data class RoomAvailabilityState(
    val selectedFloor: String = "Parter",
    val selectedRoom: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val reservations: List<Reservation> = emptyList(),
    val screenState: ScreenState = ScreenState.Idle
)
