package com.example.apptemplates.validation

import com.example.apptemplates.data.User
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreResult
import com.example.apptemplates.result.Result

class DatabaseValidator : Validator {
    override fun validate(value: String): Result<String> {
        return Result.Success
    }

    suspend fun validateEmailAndPasswordInDatabase(
        email: String,
        password: String
    ): Result<String> {

        return when (val result = Database.getUserByEmail(email)) {

            is FirestoreResult.SuccessWithResult<*> -> {
                val user = result.data as? User

                if (user != null && user.password == password) {
                    Result.Success
                } else {
                    Result.Error("Nieprawidłowe email lub hasło")
                }
            }

            is FirestoreResult.Failure -> {
                Result.Error("Błąd podczas sprawdzania czy email jest zarejestrowany")
            }


            else -> {
                Result.Error("Błąd podczas sprawdzania czy email jest zarejestrowany:")
            }

        }
    }

    suspend fun validateUsernameInDatabase(username: String): Result<String> {
        return when (val result = Database.getUserByUsername(username)) {

            is FirestoreResult.SuccessWithResult<*> -> {

                val isRegistered = result.data as? Boolean

                if (isRegistered == true) {
                    Result.Error("Nazwa użytkownika jest już zajęta")
                } else {
                    Result.Success
                }
            }

            is FirestoreResult.Failure -> {
                Result.Error("Błąd podczas sprawdzania czy nazwa użytkownika jest zarejestrowana")
            }

            else -> Result.Error("Błąd podczas sprawdzania czy nazwa użytkownika jest zarejestrowana")
        }
    }


    suspend fun validateEmailInDatabase(email: String): Result<String> {
        return when (Database.getUserByEmail(email)) {

            is FirestoreResult.SuccessWithResult<*> -> {
                Result.Success
            }

            is FirestoreResult.Failure -> {
                Result.Error("Błąd podczas sprawdzania czy email jest zarejestrowany")
            }


            else -> {
                Result.Error("Błąd podczas sprawdzania czy email jest zarejestrowany:")
            }

        }
    }
}