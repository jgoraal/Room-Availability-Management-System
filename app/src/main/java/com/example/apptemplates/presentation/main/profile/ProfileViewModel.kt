package com.example.apptemplates.presentation.main.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel {


    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()


}

data class ProfileState(
    val username: String = "jgoraal",
    val role: String = "Guest",
    val email: String = "jakubgorskki@gmail.com",
    val isEmailVerified: Boolean = true,
    val overallReservationCount: Int = 2,
    val lastSeen: Long = 1
)
