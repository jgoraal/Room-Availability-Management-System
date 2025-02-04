package com.example.apptemplates.presentation.navigation.event

sealed class NavigationEvent {
    data object NavigateOnSuccess : NavigationEvent()
    data class ShowError(val message: String) : NavigationEvent()
}