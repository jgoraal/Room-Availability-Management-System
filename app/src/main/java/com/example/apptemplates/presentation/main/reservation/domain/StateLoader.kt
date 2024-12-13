package com.example.apptemplates.presentation.main.reservation.domain

import com.example.apptemplates.data.room.Room
import com.example.apptemplates.presentation.main.temp.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

object StateLoader {

    //  State saver for availability screen
    private val _stateAvailabilityScreen = MutableStateFlow<MainUiState?>(null)
    val stateAvailabilityScreen: StateFlow<MainUiState?> = _stateAvailabilityScreen.asStateFlow()

    //  State saver for reservation screen
    private val _stateReservationScreen = MutableStateFlow<MainUiState?>(null)
    val stateReservationScreen: StateFlow<MainUiState?> = _stateReservationScreen.asStateFlow()

    fun updateAvailAbilityScreenState(updated: MainUiState) {
        _stateAvailabilityScreen.value = updated
    }

    fun updateReservationScreenState(updated: MainUiState) {
        _stateReservationScreen.value = updated
    }


    fun clearAvailabilityScreenState() {
        _stateAvailabilityScreen.value = null
    }

    fun clearReservationScreenState() {
        _stateReservationScreen.value = null
    }

    fun clearAll() {
        _stateAvailabilityScreen.value = null
        _stateReservationScreen.value = null
    }
}



