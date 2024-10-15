package com.example.apptemplates.navigation.event

sealed class NavigationEvent {
    data object NavigateOnSuccess : NavigationEvent()
    data class ShowError(val message: String) : NavigationEvent()
}