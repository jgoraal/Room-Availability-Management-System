package com.example.apptemplates.navigation

sealed class NavigationEvent {
    data object NavigateToHome : NavigationEvent()
    data class ShowError(val message: String) : NavigationEvent()
}