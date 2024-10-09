package com.example.apptemplates.presentation.login.sign_in.validation

import android.util.Log
import android.util.Patterns
import com.example.apptemplates.data.ActiveUser
import com.example.apptemplates.data.User
import com.example.apptemplates.firebase.auth.AuthResponseCollector
import com.example.apptemplates.firebase.auth.AuthResult
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreResult
import com.example.apptemplates.presentation.login.sign_in.SignInResult

/**
 * Represents the result of a validation process.
 */
sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

/**
 * Keys used for validation purposes.
 */
enum class ValidationKey {
    EMAIL, PASSWORD, ATTEMPTS, DATABASE, FIREBASE
}

/**
 * Handles validation logic for the sign-in process.
 */
class SignInValidation {

    companion object {
        // Constants for validation rules
        const val MAX_ATTEMPTS = 3
        private const val PASSWORD_MIN_LENGTH = 8
        private val PASSWORD_REGEX =
            Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")

        // Error messages
        private const val MESSAGE_EMAIL_REQUIRED = "Proszę wypełnić pole email"
        private const val MESSAGE_EMAIL_INVALID = "Niewłaściwy format email"
        private const val MESSAGE_PASSWORD_REQUIRED = "Proszę wypełnić pole hasło"
        private const val MESSAGE_PASSWORD_LENGTH =
            "Hasło musi zawierać więcej niż $PASSWORD_MIN_LENGTH znaków"
        private const val MESSAGE_PASSWORD_COMPLEXITY =
            "Hasło musi zawierać co najmniej jedną dużą literę, cyfrę i znak specjalny"
        private const val MESSAGE_ATTEMPTS_EXCEEDED = "Liczba prób przekroczona"
    }

    /**
     * Adds an error to the provided [errors] map if the [validationResult] is an error.
     *
     * @param key The validation key representing the type of error.
     * @param validationResult The result of the validation.
     * @param errors The map where errors are stored.
     */
    private fun MutableMap<ValidationKey, String>.addIfError(
        key: ValidationKey,
        validationResult: ValidationResult
    ) {
        if (validationResult is ValidationResult.Error) {
            this[key] = validationResult.message
        }
    }

    /**
     * Updates the [attempts] value in the [SignInState] if an attempt error is present.
     *
     * @param errors The map containing validation errors.
     * @return Updated [SignInState] with incremented attempts if needed.
     */
    private fun SignInState.updateAttempts(errors: Map<ValidationKey, String>): SignInState {
        return if (errors.isNotEmpty()) {
            this
        } else {
            copy(attempts = attempts - 1)
        }
    }

    /**
     * Validates the entire sign-in form.
     *
     * @param state The current state of the sign-in form.
     * @return Updated [SignInState] with validation errors and attempts count.
     */
    fun validateForm(state: SignInState): SignInState {
        val errors = mutableMapOf<ValidationKey, String>()

        // Perform individual validations and add errors if any
        errors.addIfError(ValidationKey.EMAIL, validateEmail(state.email))
        errors.addIfError(ValidationKey.PASSWORD, validatePassword(state.password))

        // Update the state with the errors and increment attempts if necessary
        return state
            .copy(
                errors = errors.mapKeys { it.key.name },
                uiState = UIState.Idle
            )
    }

    /**
     * Validates the email address.
     *
     * @param email The email address to validate.
     * @return [ValidationResult] indicating success or error with a message.
     */
    private fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error(MESSAGE_EMAIL_REQUIRED)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult.Error(
                MESSAGE_EMAIL_INVALID
            )

            else -> ValidationResult.Success
        }
    }

    /**
     * Validates the password.
     *
     * @param password The password to validate.
     * @return [ValidationResult] indicating success or error with a message.
     */
    private fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error(MESSAGE_PASSWORD_REQUIRED)
            password.length < PASSWORD_MIN_LENGTH -> ValidationResult.Error(MESSAGE_PASSWORD_LENGTH)
            !password.matches(PASSWORD_REGEX) -> ValidationResult.Error(MESSAGE_PASSWORD_COMPLEXITY)
            else -> ValidationResult.Success
        }
    }

    /**
     * Validates the number of login attempts.
     *
     * @param attempts The number of attempts made by the user.
     * @return [ValidationResult] indicating success or error if attempts exceeded.
     */
    private fun validateAttempts(attempts: Int): ValidationResult {
        return when {
            attempts >= MAX_ATTEMPTS -> ValidationResult.Error(MESSAGE_ATTEMPTS_EXCEEDED)
            else -> ValidationResult.Success
        }
    }

    private suspend fun validateDatabase(email: String, password: String): ValidationResult {
        return when (val result = Database.getUserByEmail(email)) {
            is FirestoreResult.SuccessWithResult<*> -> {
                val isRegistered = result.data as? User

                if (isRegistered != null && isRegistered.password == password) {
                    ValidationResult.Success
                } else {
                    ValidationResult.Error("Nieprawidłowe email lub hasło")
                }


            }

            is FirestoreResult.Failure -> {
                // Obsługa błędu
                ValidationResult.Error("Błąd podczas sprawdzania czy email jest zarejestrowany")
            }


            else -> {
                ValidationResult.Error("Błąd podczas sprawdzania czy email jest zarejestrowany:")
            }

        }
    }


    suspend fun validateAndUpdateAttempts(state: SignInState): SignInState {
        val errors = mutableMapOf<ValidationKey, String>()

        // Validate basic fields (email, password) first
        errors.addIfError(ValidationKey.EMAIL, validateEmail(state.email))
        errors.addIfError(ValidationKey.PASSWORD, validatePassword(state.password))

        // If there are no basic validation errors, validate against the database
        if (errors.isEmpty()) {
            errors.addIfError(ValidationKey.DATABASE, validateDatabase(state.email, state.password))
        }

        // Validate attempts only if previous errors were found
        errors.addIfError(ValidationKey.ATTEMPTS, validateAttempts(state.attempts))

        Log.i("SignInValidation", "Errors: $errors")

        return state
            .updateAttempts(errors)
            .copy(
                errors = errors.mapKeys { it.key.name },
                uiState = if (errors.isEmpty()) UIState.Loading else UIState.Idle
            )
    }


    suspend fun signInToFirebase(state: SignInState): SignInResult {

        return when (AuthResponseCollector.signInWithEmailAndPassword(state.email, state.password)
        ) {
            is AuthResult.Success -> {
                getUserFromDatabase(state)
            }

            is AuthResult.Failure -> {
                SignInResult.Error("Błąd autentykacji")
            }
        }

    }


    private suspend fun getUserFromDatabase(state: SignInState): SignInResult {

        return when (val result = Database.getUserByEmail(state.email)) {

            is FirestoreResult.SuccessWithResult<*> -> {
                val user = result.data as User?

                if (user != null) {
                    ActiveUser.setUser(user)

                    SignInResult.Success

                } else {
                    SignInResult.Error("Nie znaleziono użytkownika")
                }


            }

            is FirestoreResult.Failure -> {
                SignInResult.Error("Nie znaleziono użytkownika")
            }

            else -> {
                SignInResult.Error("Nie znaleziono użytkownika")
            }

        }


    }
}
