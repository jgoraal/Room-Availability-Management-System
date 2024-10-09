package com.example.apptemplates.presentation.login.sign_up.validation

import android.util.Log
import android.util.Patterns
import com.example.apptemplates.data.User
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreResult

class SignUpUseCase {

    companion object {
        private const val USERNAME_MIN_LENGTH = 3
        private const val USERNAME_MAX_LENGTH = 15
        private const val PASSWORD_MIN_LENGTH = 8
        private val USERNAME_REGEX = Regex("^(?=.{3,15}$)(?![_.])[a-zA-Z0-9._]+(?<![_.])$")
        private val PASSWORD_REGEX =
            Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
    }


    suspend fun validateUsernameAndEmail(state: SignUpFormState): SignUpFormState {
        val formValidationState = validateUsernameAndEmailForm(state)

        val databaseValidationState = validateDatabaseFields(formValidationState)

        return databaseValidationState
    }


    fun validateUsernameAndEmailForm(state: SignUpFormState): SignUpFormState {
        val usernameError = validateUsername(state.username)
        val emailError = validateEmail(state.email)

        val isUsernameValid = usernameError == null
        val isEmailValid = emailError == null

        return state.copy(
            usernameError = usernameError,
            emailError = emailError,
            isUsernameValid = isUsernameValid,
            isEmailValid = isEmailValid,
            isValid = false
        )
    }


    fun validateSignUpForm(state: SignUpFormState): SignUpFormState {
        val usernameError = validateUsername(state.username)
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)
        val passwordConfirmError = validatePasswordConfirm(state.password, state.passwordConfirm)

        val isValid = listOf(
            usernameError,
            emailError,
            passwordError,
            passwordConfirmError
        ).all { it == null }

        return state.copy(
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError,
            passwordConfirmError = passwordConfirmError,
            isValid = isValid
        )
    }


    //==============================================================================================
    //  Simple validation
    //==============================================================================================


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
            email.isBlank() -> "Proszę wypełnić pole email"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Niewłaściwy format email"
            else -> null
        }
    }


    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Proszę wypełnić pole hasło"
            password.length < PASSWORD_MIN_LENGTH -> "Hasło musi zawierać więcej niż $PASSWORD_MIN_LENGTH znaków"
            !password.matches(PASSWORD_REGEX) -> "Hasło musi zawierać co najmniej jedną dużą literę, cyfrę i znak specjalny"
            else -> null
        }
    }


    private fun validatePasswordConfirm(password: String, passwordConfirm: String): String? {
        return when {
            passwordConfirm.isBlank() -> "Proszę powtórzyć hasło"
            password != passwordConfirm -> "Hasła nie są zgodne"
            !passwordConfirm.matches(PASSWORD_REGEX) -> "Hasło musi zawierać co najmniej jedną dużą literę, cyfrę i znak specjalny"
            else -> null
        }
    }


    //==============================================================================================
    //  Database check
    //==============================================================================================

    private suspend fun validateDatabaseFields(state: SignUpFormState): SignUpFormState {
        val usernameError = isUsernameRegistered(state.username)
        val emailError = isEmailRegistered(state.email)

        val isUsernameValid = usernameError == null
        val isEmailValid = emailError == null

        return state.copy(
            usernameError = usernameError,
            emailError = emailError,
            isUsernameValid = isUsernameValid,
            isEmailValid = isEmailValid,
            isValid = state.isValid && isUsernameValid && isEmailValid  // Combine form and database validation
        )
    }


    private suspend fun isUsernameRegistered(username: String): String? {
        return when (val result = Database.getUserByUsername(username)) {

            is FirestoreResult.SuccessWithResult<*> -> {

                val isRegistered = result.data as? Boolean

                if (isRegistered == true) {
                    "Nazwa użytkownika jest już zajęta"
                } else {
                    null
                }
            }

            is FirestoreResult.Failure -> {
                Log.e(
                    "Firestore",
                    "Błąd podczas sprawdzania czy username jest zarejestrowany: ${result.exception.message}"
                )
                null
            }

            else -> null
        }
    }


    private suspend fun isEmailRegistered(email: String): String? {
        return when (val result = Database.getUserByEmail(email)) {

            is FirestoreResult.SuccessWithResult<*> -> {

                val isRegistered = result.data as? User

                if (isRegistered != null && isRegistered.email == email) {
                    "Email jest już zajęty"
                } else {
                    null
                }
            }

            is FirestoreResult.Failure -> {
                Log.e(
                    "Firestore",
                    "Błąd podczas sprawdzania czy email jest zarejestrowany: ${result.exception.message}"
                )
                null
            }

            else -> null
        }
    }
}
