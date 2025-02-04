package com.example.apptemplates.utils.validation

import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.presentation.state.FormKey
import com.example.apptemplates.presentation.state.FormState
import com.example.apptemplates.presentation.state.UIState

class Validation() {

    private val validators = mutableMapOf<FormKey, Validator>()

    fun getValidators(): Map<FormKey, Validator> {
        return validators
    }


    fun addValidator(key: FormKey): Validation {
        validators[key] = getValidator(key)
        return this
    }

    private fun getValidator(key: FormKey): Validator {
        return when (key) {
            FormKey.USERNAME -> UsernameValidator()
            FormKey.EMAIL -> EmailValidator()
            FormKey.PASSWORD -> PasswordValidator()
            FormKey.CONFIRM_PASSWORD -> PasswordValidator()
            FormKey.ATTEMPTS -> AttemptsValidator()
            FormKey.UI_STATE -> UIStateValidator()

            else -> DatabaseValidator()
        }
    }

    fun validateForm(state: FormState): FormState {
        val errors = mutableMapOf<FormKey, String?>()

        validators[FormKey.ATTEMPTS]?.let { validator ->
            if (validator is AttemptsValidator && validator.isTimedOut(state.timeoutStart)) {
                return state.copy(
                    errors = errors,
                )
            }
        }


        validators.forEach { (key, validator) ->
            val result = getResult(state, key, validator)
            if (result is Result.Error) {
                errors[key] = result.error
            }
        }

        return state.copy(errors = errors)
    }

    private fun getResult(state: FormState, key: FormKey, validator: Validator): Result<*> {

        return when (key) {
            FormKey.USERNAME -> validator.validate(state.username)
            FormKey.EMAIL -> validator.validate(state.email)
            FormKey.PASSWORD -> validator.validate(state.password)
            FormKey.CONFIRM_PASSWORD -> (validator as PasswordValidator).validatePasswordConfirm(
                state.password,
                state.confirmPassword
            )

            else -> Result.Success
        }
    }


    suspend fun validateAuthentication(state: FormState): FormState {
        val errors = mutableMapOf<FormKey, String?>()


        validators[FormKey.ATTEMPTS]?.let { validator ->
            if (validator is AttemptsValidator && validator.isTimedOut(state.timeoutStart)) {
                return state.copy(
                    errors = errors,
                )
            }
        }


        validators.forEach { (key, validator) ->
            val result = getAuthenticationResult(state, key, validator)
            if (result is Result.Error) {
                errors[key] = result.error
            }
        }



        return state.copy(
            errors = errors,
            uiState = if (errors.isEmpty()) UIState.Loading else UIState.Idle
        )

    }

    private suspend fun getAuthenticationResult(
        state: FormState,
        key: FormKey,
        validator: Validator
    ): Result<*> {
        return when (key) {

            FormKey.DATABASE_EMAIL_PASSWORD -> (validator as DatabaseValidator).validateEmailAndPasswordInDatabase(
                state.email
            )

            FormKey.DATABASE_USERNAME -> (validator as DatabaseValidator).validateUsernameInDatabase(
                state.username
            )

            FormKey.DATABASE_EMAIL -> (validator as DatabaseValidator).validateEmailInDatabase(
                state.email
            )

            FormKey.ATTEMPTS -> validator.validate(state.attempts.toString())

            else -> getResult(state, key, validator)
        }
    }

}