package com.example.apptemplates.presentation.state

sealed class UIState {
    data object Idle : UIState()
    data object Loading : UIState()
    data class Timeout(val message: String) : UIState()

    data object Success : UIState()
    data class Error(val message: String) : UIState()
}