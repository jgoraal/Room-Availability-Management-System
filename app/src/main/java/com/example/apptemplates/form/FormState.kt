package com.example.apptemplates.form

import com.example.apptemplates.presentation.login.sign_in.validation.UIState

data class FormState(
    val username: String = "jgoraal_",
    val email: String = "jakub.gorski@student.put.poznan.pl",
    val password: String = "@Test1234",
    val confirmPassword: String = "@Test1234",
    val attempts: Int = 0,
    val errors: Map<FormKey, String?> = emptyMap(),
    val uiState: UIState = UIState.Idle
)
