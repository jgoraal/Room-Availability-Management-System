package com.example.apptemplates.presentation.login.sign_up.validation

data class SignUpFormState(
    val username: String = "jgoraal",
    val email: String = "jakubgorskki@gmail.com",
    val password: String = "@Testpass23",
    val passwordConfirm: String = "@Testpass23",
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,
    val isUsernameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isValid: Boolean = false
)
