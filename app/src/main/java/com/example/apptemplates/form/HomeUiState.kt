package com.example.apptemplates.form

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.data.user.User
import java.time.LocalDateTime

data class HomeUiState(
    val user: User? = null,
    val screenState: ScreenState = ScreenState.Idle,
    val rooms: List<Room> = emptyList(),
    val reservations: List<Reservation> = emptyList(),
    val errors: List<UiError> = emptyList(),
    val lastUpdated: LocalDateTime? = null,  // Track when data was last updated
    val isLoading: Boolean = false
)


// Sealed class to represent different states of the screen
sealed class ScreenState {
    data object Idle : ScreenState()  // Initial state
    data object Loading : ScreenState()  // Loading state when data is being fetched

    data object Success : ScreenState()  // Success state with optional message
    data class Error(val uiError: UiError) : ScreenState()  // Error state with detailed error message
}


// Sealed class to represent different types of UI errors
sealed class UiError {
    data class NetworkError(val message: String, val throwable: Throwable? = null) : UiError()
    data class DatabaseError(val message: String, val throwable: Throwable? = null) : UiError()
    data class ValidationError(val field: String, val message: String) : UiError()
    data class UnknownError(val message: String, val throwable: Throwable? = null) : UiError()
}
