package com.example.apptemplates.firebase.auth.operation

import com.example.apptemplates.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class SignOutOperation : AuthOperation {
    override suspend fun performAuthAction(state: FormState): Result<*> {
        return authSignOut()
    }

    private fun authSignOut(): Result<*> {
        FirebaseAuthManager.signOut()
        return Result.Success
    }
}