package com.example.apptemplates.firebase.auth

import com.example.apptemplates.data.ActiveUser
import com.example.apptemplates.data.User
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreResult
import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class SignUpOperation : AuthOperation {
    override suspend fun performAuthAction(state: FormState): Result<*> {
        return authSignUp(state)
    }


    private suspend fun authSignUp(state: FormState): Result<*> {
        return when (val result = AuthResponseCollector.signUpWithEmailAndPassword(
            state.email,
            state.password
        )) {
            is AuthResult.Success -> authSendVerificationEmail(state)
            is AuthResult.Failure -> Result.Error("Bład podczas autentykacji")
        }
    }

    private suspend fun authSendVerificationEmail(state: FormState): Result<*> {
        return when (val result = AuthResponseCollector.sendEmailVerification()) {
            is AuthResult.Success -> addUserToDatabase(state)
            is AuthResult.Failure -> Result.Error("Nie udało się wysłać maila weryfikacyjnego")
        }
    }

    private suspend fun addUserToDatabase(state: FormState): Result<*> {

        val currentUser = AuthResponseCollector.currentUser ?: return Result.Error("User not found")

        val registeredUser = User(
            uid = currentUser.uid,
            username = state.username,
            email = state.email,
            password = state.password,
            lastSeen = System.currentTimeMillis()
        )

        return when (val result = Database.addUser(registeredUser)) {
            is FirestoreResult.Success -> {
                ActiveUser.setUser(registeredUser)
                Result.Success
            }

            is FirestoreResult.Failure -> Result.Error("Nie udało się stworzyć użytkownika")
            else -> Result.Error("Nie udało się dodać użytkownika do bazy danych")
        }
    }
}