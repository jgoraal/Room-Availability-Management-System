package com.example.apptemplates.domain.usecase


import com.example.apptemplates.presentation.screens.home.base.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow

object StateCopy {

    private val _state = MutableStateFlow(MainUiState())

    // Funkcja do kopiowania aktualnego stanu
    fun copyState(currentState: MainUiState) {
        _state.value = currentState.copy()
    }

    // Funkcja do pobierania skopiowanego stanu
    fun getState(): MutableStateFlow<MainUiState> {
        return _state
    }

    // Funkcja do czyszczenia stanu
    fun clearState() {
        _state.value = MainUiState()
    }
}