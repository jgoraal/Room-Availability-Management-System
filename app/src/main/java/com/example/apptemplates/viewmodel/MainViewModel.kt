package com.example.apptemplates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.user.ReservationPermissionManager
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.home.UiEvent
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.presentation.main.temp.UserReloadUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

abstract class MainViewModel(
    val userReloadUseCase: UserReloadUseCase = UserReloadUseCase()
) : ViewModel() {

    protected val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    protected val permission = ReservationPermissionManager()


    protected fun <T> wrapWithLoadingState(
        successState: (T) -> Unit,
        errorState: (String) -> Unit,
        block: suspend () -> T
    ) {
        viewModelScope.launch {
            _state.update { it.copy(screenState = ScreenState.Loading) }

            try {
                reloadUser()
                val result = block()
                successState(result)
            } catch (e: Exception) {
                errorState(e.localizedMessage ?: "An unknown error occurred")
            }
        }

    }


    protected fun handleError(error: UiError) {
        viewModelScope.launch {
            _state.update { it.copy(screenState = ScreenState.Error(error)) }

            delay((2.5).seconds)
            _state.update { it.copy(screenState = ScreenState.Idle) }
        }

    }


    private suspend inline fun reloadUser() {
        userReloadUseCase()
    }

    protected fun instantUserReload() {
        viewModelScope.launch {
            try {
                reloadUser()
            } catch (e: Exception) {
                handleError(UiError.UnknownError(e.localizedMessage ?: "An unknown error occurred"))
            }

        }
    }


    protected fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }


}