package com.example.apptemplates.form

import com.example.apptemplates.presentation.login.sign_in.validation.UIState

data class FormState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val attempts: Int = 0,
    val errors: Map<FormKey, String?> = emptyMap(),
    val uiState: UIState = UIState.Idle
)
