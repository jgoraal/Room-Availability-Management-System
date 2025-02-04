package com.example.apptemplates.utils.validation

import com.example.apptemplates.data.firebase.database.FirestoreRepository
import com.example.apptemplates.data.firebase.database.result.FirestoreResult
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.data.model.model.user.User

class DatabaseValidator : Validator {
    companion object {
        const val EMAIL_ALREADY_REGISTERED = "Email jest już zarejestrowany"
        const val EMAIL_CHECK_ERROR = "Błąd podczas sprawdzania czy email jest zarejestrowany"

        const val PASSWORD_INCORRECT = "Nieprawidłowe email lub hasło"

        const val USERNAME_ALREADY_TAKEN = "Nazwa użytkownika jest już zajęta"
        const val USERNAME_CHECK_ERROR =
            "Błąd podczas sprawdzania czy nazwa użytkownika jest zarejestrowana"
    }


    override fun validate(value: String): Result<String> {
        return Result.Success
    }

    suspend fun validateEmailAndPasswordInDatabase(email: String): Result<String> {

        return when (val result = FirestoreRepository.getUserByEmail(email)) {

            is FirestoreResult.SuccessWithResult<*> -> {
                val user = result.data as? User

                if (user != null) {
                    Result.Success
                } else {
                    Result.Error(PASSWORD_INCORRECT)
                }
            }

            is FirestoreResult.Failure -> Result.Error(EMAIL_CHECK_ERROR)

            else -> Result.Error(EMAIL_CHECK_ERROR)


        }
    }

    suspend fun validateUsernameInDatabase(username: String): Result<String> {
        return when (val result = FirestoreRepository.getUserByUsername(username)) {

            is FirestoreResult.SuccessWithResult<*> -> {

                val isRegistered = result.data as? Boolean

                if (isRegistered == true) {
                    Result.Error(USERNAME_ALREADY_TAKEN)
                } else {
                    Result.Success
                }
            }

            is FirestoreResult.Failure -> Result.Error(USERNAME_CHECK_ERROR)

            else -> Result.Error(USERNAME_CHECK_ERROR)
        }
    }


    suspend fun validateEmailInDatabase(email: String): Result<String> {
        return when (val result = FirestoreRepository.getUserByEmail(email)) {

            is FirestoreResult.SuccessWithResult<*> -> Result.Error(EMAIL_ALREADY_REGISTERED)


            is FirestoreResult.Failure -> {

                result.exception.message?.let {
                    if (it.contains("not found")) {
                        Result.Success
                    } else {
                        Result.Error(EMAIL_CHECK_ERROR)
                    }
                } ?: Result.Error(EMAIL_CHECK_ERROR)

            }

            else -> Result.Error(EMAIL_CHECK_ERROR)

        }
    }
}