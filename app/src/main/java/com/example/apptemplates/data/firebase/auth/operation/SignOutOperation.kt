package com.example.apptemplates.data.firebase.auth.operation

import com.example.apptemplates.data.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.presentation.state.FormState
import com.example.apptemplates.data.firebase.database.result.Result

class SignOutOperation : AuthOperation {
    override suspend fun performAuthAction(state: FormState): Result<*> {
        return authSignOut()
    }

    private fun authSignOut(): Result<*> {
        FirebaseAuthManager.signOut()
        return Result.Success
    }
}