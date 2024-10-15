package com.example.apptemplates.firebase.auth.operation

import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.data.user.User
import com.example.apptemplates.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.firebase.auth.result.AuthResult
import com.example.apptemplates.firebase.database.FirestoreRepository
import com.example.apptemplates.firebase.database.result.FirestoreResult
import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class SignUpOperation : AuthOperation {

    companion object {
        private const val AUTHENTICATION_ERROR = "Błąd podczas autentykacji"
        private const val VERIFICATION_EMAIL_ERROR =
            "Nie udało się wysłać wiadomości weryfikacyjnej"
        private const val USER_NOT_FOUND_ERROR = "Użytkownik nie został znaleziony"
        private const val USER_CREATION_ERROR = "Nie udało się stworzyć użytkownika"
        private const val USER_DATABASE_ERROR = "Nie udało się dodać użytkownika"
    }


    override suspend fun performAuthAction(state: FormState): Result<*> {
        return authSignUp(state)
    }


    private suspend fun authSignUp(state: FormState): Result<*> {
        return when (FirebaseAuthManager.signUpWithEmailAndPassword(
            state.email,
            state.password
        )) {
            is AuthResult.Success -> authSendVerificationEmail(state)
            is AuthResult.Failure -> Result.Error(AUTHENTICATION_ERROR)
        }
    }

    private suspend fun authSendVerificationEmail(state: FormState): Result<*> {
        return when (FirebaseAuthManager.sendEmailVerification()) {
            is AuthResult.Success -> addUserToDatabase(state)
            is AuthResult.Failure -> Result.Error(VERIFICATION_EMAIL_ERROR)
        }
    }

    private suspend fun addUserToDatabase(state: FormState): Result<*> {

        val currentUser =
            FirebaseAuthManager.currentUser ?: return Result.Error(USER_NOT_FOUND_ERROR)

        val registeredUser = User(
            uid = currentUser.uid,
            username = state.username,
            email = state.email,
            lastSeen = System.currentTimeMillis()
        )

        return when (val result = FirestoreRepository.addUser(registeredUser)) {
            is FirestoreResult.Success -> {
                ActiveUser.setUser(registeredUser)
                Result.Success
            }

            is FirestoreResult.Failure -> Result.Error(USER_CREATION_ERROR)
            else -> Result.Error(USER_DATABASE_ERROR)
        }
    }
}