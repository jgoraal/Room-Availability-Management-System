package com.example.apptemplates.firebase.auth

import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class SignOutOperation : AuthOperation {
    override suspend fun performAuthAction(state: FormState): Result<*> {
        return authSignOut()
    }

    private fun authSignOut(): Result<*> {
        AuthResponseCollector.signOut()
        return Result.Success
    }
}