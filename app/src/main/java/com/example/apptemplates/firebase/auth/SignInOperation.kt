package com.example.apptemplates.firebase.auth

import com.example.apptemplates.data.ActiveUser
import com.example.apptemplates.data.User
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreResult
import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class SignInOperation : AuthOperation {
    override suspend fun performAuthAction(state: FormState): Result<*> {
        return authSignIn(state.email, state.password)
    }


    private suspend fun authSignIn(email: String, password: String): Result<*> {

        return when (AuthResponseCollector.signInWithEmailAndPassword(email, password)
        ) {
            is AuthResult.Success -> getUserFromDatabase(email)
            is AuthResult.Failure -> Result.Error("Błąd autentykacji")
        }

    }


    private suspend fun getUserFromDatabase(email: String): Result<*> {
        return when (val result = Database.getUserByEmail(email)) {

            is FirestoreResult.SuccessWithResult<*> -> {
                val user = result.data as User?

                user?.let {
                    ActiveUser.setUser(user)

                    Result.Success
                } ?: Result.Error("Brak użytkownika w bazie danych")


            }

            is FirestoreResult.Failure -> Result.Error("Nie znaleziono użytkownika w bazie danych")

            else -> Result.Error("Nie znaleziono użytkownika")


        }
    }
}