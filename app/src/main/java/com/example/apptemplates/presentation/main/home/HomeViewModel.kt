package com.example.apptemplates.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.form.HomeUiState
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.home.domain.AddRoomUseCase
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

class HomeViewModel(
    private val fetchRoomsUseCase: FetchRoomsUseCase = FetchRoomsUseCase(),
    private val fetchReservationsUseCase: FetchReservationsUseCase = FetchReservationsUseCase(),
    private val addRoomUseCase: AddRoomUseCase = AddRoomUseCase()
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
                            //rooms = getMockRooms(),
                            //reservations = getMockReservations(),
                            screenState = ScreenState.Error(
                                UiError.DatabaseError(
                                    message
                                )
                            )
                        )
                    }
                }
            ) {

                _uiState.update { it.copy(user = ActiveUser.getUser()) }

                val rooms = fetchRoomsUseCase()
                val reservations = fetchReservationsUseCase(_uiState.value.user!!.uid)
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
                            //rooms = getMockRooms(),
                            //reservations = getMockReservations(),
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
                val reservations = fetchReservationsUseCase(_uiState.value.user!!.uid)
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

