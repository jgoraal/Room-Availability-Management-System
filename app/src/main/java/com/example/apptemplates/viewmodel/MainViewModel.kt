package com.example.apptemplates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.presentation.main.home.UiEvent
import com.example.apptemplates.presentation.main.temp.MainUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MainViewModel() : ViewModel() {

    protected val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()


    protected inline fun <T> wrapWithLoadingState(
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
            }
        }

    }

    protected fun emitEvent(event: UiEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

}