package com.example.apptemplates.presentation.state

import com.example.apptemplates.data.model.model.user.User
import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.domain.model.Room
import java.time.LocalDateTime

data class HomeUiState(
    val user: User? = null,
    val screenState: ScreenState = ScreenState.Idle,
    val rooms: List<Room> = emptyList(),
    val reservations: List<Reservation> = emptyList(),
    val errors: List<UiError> = emptyList(),
    val lastUpdated: LocalDateTime? = null,
    val isLoading: Boolean = false
)


sealed class ScreenState {
    data object Idle : ScreenState()
    data object Loading : ScreenState()

    data object Success : ScreenState()
    data class Error(val uiError: UiError) :
        ScreenState()
}


sealed class UiError {
    data class NetworkError(val message: String, val throwable: Throwable? = null) : UiError()
    data class DatabaseError(val message: String, val throwable: Throwable? = null) : UiError()
    data class ValidationError(val field: String, val message: String) : UiError()
    data class UnknownError(val message: String, val throwable: Throwable? = null) : UiError()
    data class PermissionError(val message: String) : UiError()
}
