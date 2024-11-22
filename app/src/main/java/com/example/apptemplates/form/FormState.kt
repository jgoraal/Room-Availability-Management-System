package com.example.apptemplates.form

data class FormState(
    val username: String = "",
    val email: String = "",
    val password: String = "@Test1234",
    val confirmPassword: String = "@Test1234",
    val attempts: Int = 0,
    val errors: Map<FormKey, String?> = emptyMap(),
    val uiState: UIState = UIState.Idle,
    val timeoutStart: Long = 0L,
    val timeoutRemaining: Long = 0L,
)