package com.example.apptemplates.presentation.login.sign_in.validation

data class SignInState(
    val email: String = "",
    val password: String = "",
    val attempts: Int = 0,
    val errors: Map<String, String?> = emptyMap(),
    val uiState: UIState = UIState.Idle,
)


sealed class UIState {
    data object Idle : UIState()
    data object Loading : UIState()
    data class Timeout(val message: String) : UIState()
    data object Success : UIState()
    data class Error(val message: String) : UIState()
}
