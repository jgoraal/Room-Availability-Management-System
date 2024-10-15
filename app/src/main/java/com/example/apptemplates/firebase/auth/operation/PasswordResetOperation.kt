package com.example.apptemplates.firebase.auth.operation

import com.example.apptemplates.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.firebase.auth.result.AuthResult
import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class PasswordResetOperation : AuthOperation {

    companion object {
        const val ERROR_PASSWORD_RESET =
            "Wysłanie wiadomości z linkiem do zresetowania hasła nie powiodło się."
    }

    override suspend fun performAuthAction(state: FormState): Result<*> {
        return resetPassword(state.email)
    }

    private suspend fun resetPassword(email: String): Result<*> {
        return when (FirebaseAuthManager.sendPasswordResetEmail(email)
        ) {
            is AuthResult.Success -> Result.Success
            is AuthResult.Failure -> Result.Error(ERROR_PASSWORD_RESET)
        }
    }

}