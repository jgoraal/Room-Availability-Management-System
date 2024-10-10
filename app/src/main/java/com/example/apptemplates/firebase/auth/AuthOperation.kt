package com.example.apptemplates.firebase.auth

import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

fun interface AuthOperation {
    suspend fun performAuthAction(state: FormState): Result<*>
}