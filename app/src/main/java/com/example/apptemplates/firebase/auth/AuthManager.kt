package com.example.apptemplates.firebase.auth

import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result

class AuthManager {

    private var operation: AuthOperation? = null

    // Add an operation but allow only one to be added
    fun addOperation(type: AuthOperationType): AuthManager {
        operation?.let {
            throw IllegalStateException("Only one operation can be added at a time")
        }
        operation = getOperation(type)
        return this
    }

    private fun getOperation(type: AuthOperationType): AuthOperation? {
        return when (type) {
            AuthOperationType.SIGN_IN -> SignInOperation()
            AuthOperationType.SIGN_UP -> SignUpOperation()
            AuthOperationType.SIGN_OUT -> SignOutOperation()
        }
    }

    // Perform the added operation
    suspend fun performAuthAction(state: FormState): Result<*> {
        val currentOperation = operation ?: return Result.Error("No operation added to AuthManager")
        return currentOperation.performAuthAction(state)
    }

    // Clear the current operation (optional)
    fun clearOperation() {
        this.operation = null
    }
}