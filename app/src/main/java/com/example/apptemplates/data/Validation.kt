package com.example.apptemplates.data

import android.util.Log
import android.util.Patterns
import com.example.apptemplates.firebase.auth.AuthResponse
import com.example.apptemplates.firebase.database.FirestoreDatabase
import com.example.apptemplates.firebase.database.FirestoreResult
import com.example.apptemplates.presentation.login.sign_in.validation.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Validation(
    private val repository: AuthResponse,
    private val database: FirestoreDatabase
) {

    companion object {
        // Constants for validation rules
        private const val USERNAME_MIN_LENGTH = 3
        private const val USERNAME_MAX_LENGTH = 15
        private const val PASSWORD_MIN_LENGTH = 8
        private val USERNAME_REGEX = Regex("^(?=.{3,15}$)(?![_.])[a-zA-Z0-9._]+(?<![_.])$")
        private val PASSWORD_REGEX = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

        // Error messages
        private const val MESSAGE_EMAIL_REQUIRED = "Proszę wypełnić pole email"
        private const val MESSAGE_EMAIL_INVALID = "Niewłaściwy format email"
        private const val MESSAGE_PASSWORD_REQUIRED = "Proszę wypełnić pole hasło"
        private const val MESSAGE_PASSWORD_LENGTH = "Hasło musi zawierać więcej niż $PASSWORD_MIN_LENGTH znaków"
        private const val MESSAGE_PASSWORD_COMPLEXITY = "Hasło musi zawierać co najmniej jedną dużą literę, cyfrę i znak specjalny"
    }

    // Validate form fields based on specific validation rules
    fun validateForm(state: FormState): FormState {
        val errors = mutableMapOf<FormKey, String?>()

        // Validate username, email, password, and confirm password
        errors[FormKey.NAME] = validateUsername(state.name)
        errors[FormKey.EMAIL] = validateEmail(state.email)
        errors[FormKey.PASSWORD] = validatePassword(state.password)
        errors[FormKey.CONFIRM_PASSWORD] = validatePasswordConfirm(state.password, state.confirmPassword)

        // Check if all fields are valid
        val isValid = errors.values.all { it == null }

        return state.copy(
            errors = errors,
            uiState = if (isValid) UIState.Loading else UIState.Idle
        )
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Proszę wybrać nazwę użytkownika"
            username.length < USERNAME_MIN_LENGTH -> "Nazwa użytkownika powinna być dłuższa niż $USERNAME_MIN_LENGTH znaki"
            username.length > USERNAME_MAX_LENGTH -> "Nazwa użytkownika powinna być krótsza niż $USERNAME_MAX_LENGTH znaków"
            !username.matches(USERNAME_REGEX) -> "Niepoprawny format nazwy użytkownika"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> MESSAGE_EMAIL_REQUIRED
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> MESSAGE_EMAIL_INVALID
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> MESSAGE_PASSWORD_REQUIRED
            password.length < PASSWORD_MIN_LENGTH -> MESSAGE_PASSWORD_LENGTH
            !password.matches(PASSWORD_REGEX) -> MESSAGE_PASSWORD_COMPLEXITY
            else -> null
        }
    }

    private fun validatePasswordConfirm(password: String, passwordConfirm: String): String? {
        return when {
            passwordConfirm.isBlank() -> "Proszę powtórzyć hasło"
            password != passwordConfirm -> "Hasła nie są zgodne"
            else -> null
        }
    }

    // Database validations for username and email
    suspend fun validateDatabaseFields(state: FormState): FormState = withContext(Dispatchers.IO) {
        val usernameError = isUsernameRegistered(state.name)
        val emailError = isEmailRegistered(state.email)

        val isValid = usernameError == null && emailError == null

        return@withContext state.copy(
            errors = state.errors.plus(FormKey.NAME to usernameError).plus(FormKey.EMAIL to emailError),
        )
    }

    // Check if username is already registered in the database
    private suspend fun isUsernameRegistered(username: String): String? {
        return when (val result = database.getUserByUsername(username)) {
            is FirestoreResult.SuccessWithResult<*> -> {
                val isRegistered = result.data as? Boolean
                if (isRegistered == true) "Nazwa użytkownika jest już zajęta" else null
            }
            is FirestoreResult.Failure -> {
                Log.e("Firestore", "Błąd podczas sprawdzania username: ${result.exception.message}")
                null
            }
            else -> null
        }
    }

    // Check if email is already registered in the database
    private suspend fun isEmailRegistered(email: String): String? {
        return when (val result = database.getUserByEmail(email)) {
            is FirestoreResult.SuccessWithResult<*> -> {
                val isRegistered = result.data as? User
                if (isRegistered?.email == email) "Email jest już zajęty" else null
            }
            is FirestoreResult.Failure -> {
                Log.e("Firestore", "Błąd podczas sprawdzania email: ${result.exception.message}")
                null
            }
            else -> null
        }
    }
}