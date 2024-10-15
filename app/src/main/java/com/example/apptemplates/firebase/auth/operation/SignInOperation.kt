package com.example.apptemplates.firebase.auth.operation

import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.data.user.User
import com.example.apptemplates.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.firebase.auth.result.AuthResult
import com.example.apptemplates.firebase.database.FirestoreRepository
import com.example.apptemplates.firebase.database.result.FirestoreResult
import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class SignInOperation : AuthOperation {

    companion object {
        private const val AUTHENTICATION_ERROR = "Niepoprawny email lub hasło"
        private const val USER_NOT_FOUND_ERROR = "Nie znaleziono użytkownika"
        private const val USER_UPDATE_ERROR = "Nie udało się zaktualizować danych użytkownika"
        private const val UNKNOWN_ERROR = "Nieznany błąd"
    }


    override suspend fun performAuthAction(state: FormState): Result<*> {
        return authSignIn(state.email, state.password)
    }


    private suspend fun authSignIn(email: String, password: String): Result<*> {

        return when (FirebaseAuthManager.signInWithEmailAndPassword(email, password)
        ) {
            is AuthResult.Success -> getUserFromDatabase(email, password)
            is AuthResult.Failure -> Result.Error(AUTHENTICATION_ERROR)
        }

    }


    private suspend fun getUserFromDatabase(email: String, password: String): Result<*> {
        return when (val result = FirestoreRepository.getUserByEmail(email)) {

            is FirestoreResult.SuccessWithResult<*> -> {
                val user = result.data as? User
                    ?: return Result.Error(USER_NOT_FOUND_ERROR)

                ActiveUser.setUser(user)

                updateUserData(user.copy(lastSeen = System.currentTimeMillis()))

            }

            is FirestoreResult.Failure -> Result.Error(USER_NOT_FOUND_ERROR)

            else -> Result.Error(USER_NOT_FOUND_ERROR)


        }
    }


    private suspend fun updateUserData(user: User): Result<*> {
        return when (FirestoreRepository.updateUser(user)) {
            is FirestoreResult.Success -> {

                ActiveUser.setUser(user)
                Result.Success
            }

            is FirestoreResult.Failure -> Result.Error(USER_UPDATE_ERROR)
            else -> Result.Error(UNKNOWN_ERROR)

        }
    }
}