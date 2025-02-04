package com.example.apptemplates.data.firebase.auth.operation

import com.example.apptemplates.presentation.state.FormState
import com.example.apptemplates.data.firebase.database.result.Result

fun interface AuthOperation {
    suspend fun performAuthAction(state: FormState): Result<*>
}