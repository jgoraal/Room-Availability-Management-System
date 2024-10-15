package com.example.apptemplates.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.form.HomeUiState
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.home.domain.FetchReservationsUseCase
import com.example.apptemplates.presentation.main.home.domain.FetchRoomsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel(
    private val fetchRoomsUseCase: FetchRoomsUseCase = FetchRoomsUseCase(),
    private val fetchReservationsUseCase: FetchReservationsUseCase = FetchReservationsUseCase()
) : ViewModel() {

    // StateFlow to represent the UI state
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // SharedFlow for one-off navigation events
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()


    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            wrapWithLoadingState(
                successState = { data ->
                    _uiState.update {
                        it.copy(
                            rooms = data.first,
                            reservations = data.second,
                            screenState = ScreenState.Success
                        )
                    }
                },
                errorState = { message ->
                    _uiState.update {
                        it.copy(
                            rooms = getMockRooms(),
                            reservations = getMockReservations(),
                            screenState = ScreenState.Error(
                                UiError.DatabaseError(
                                    message
                                )
                            )
                        )
                    }
                }
            ) {
                val rooms = fetchRoomsUseCase()
                val reservations = fetchReservationsUseCase()
                rooms to reservations
            }
        }
    }

    fun refreshRooms() {
        viewModelScope.launch {

            wrapWithLoadingState(
                successState = { data ->
                    _uiState.update {
                        it.copy(
                            rooms = data.first,
                            reservations = data.second,
                            screenState = ScreenState.Success
                        )
                    }
                },
                errorState = { message ->
                    _uiState.update {
                        it.copy(
                            rooms = getMockRooms(),
                            reservations = getMockReservations(),
                            screenState = ScreenState.Error(
                                UiError.DatabaseError(
                                    message
                                )
                            )
                        )
                    }
                }
            ) {
                val rooms = fetchRoomsUseCase()
                val reservations = fetchReservationsUseCase()
                rooms to reservations
            }


        }
    }


    // Utility function to wrap API calls with loading state
    private suspend fun <T> wrapWithLoadingState(
        successState: (T) -> Unit,
        errorState: (String) -> Unit,
        block: suspend () -> T
    ) {
        _uiState.update { it.copy(screenState = ScreenState.Loading) }

        try {
            val result = block()
            successState(result)
        } catch (e: Exception) {
            errorState(e.localizedMessage ?: "An unknown error occurred")
        }

    }


    fun navigateToProfile() {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.NavigateToProfile)
        }
    }

}


sealed class UiEvent {
    data object NavigateToProfile : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}


private fun getMockReservations(): List<Reservation> {
    return listOf(
        Reservation(
            roomId = "1",
            userId = "123",
            startTime = Date(),
            endTime = Date(System.currentTimeMillis() + 3600000), // +1 godzina
            purpose = "Team Meeting",
            status = ReservationStatus.CONFIRMED
        ),
        Reservation(
            roomId = "2",
            userId = "456",
            startTime = Date(),
            endTime = Date(System.currentTimeMillis() + 7200000), // +2 godziny
            purpose = "Project Planning",
            status = ReservationStatus.PENDING
        )
    )
}

private fun getMockRooms(): List<Room> {
    return listOf(
        Room(
            id = "1",
            name = "Conference Room A",
            capacity = 10
        ),
        Room(
            id = "2",
            name = "Meeting Room B",
            capacity = 5
        )
    )
}
