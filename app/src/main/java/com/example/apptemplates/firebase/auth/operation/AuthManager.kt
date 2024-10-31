package com.example.apptemplates.firebase.auth.operation

import com.example.apptemplates.form.FormState
import com.example.apptemplates.form.UIState
import com.example.apptemplates.result.Result

class AuthManager {

    companion object {
        private const val STATE_ERROR = "Poczekaj na zakończenie operacji"
        private const val OPERATION_ERROR = "Tylko jedna operacja może być wykonywana na raz"
    }


    private var operation: AuthOperation? = null

    // Add an operation but allow only one to be added
    fun addOperation(type: AuthOperationType): AuthManager {
        operation?.let {
            throw IllegalStateException(OPERATION_ERROR)
        }
        operation = getOperation(type)
        return this
    }

    private fun getOperation(type: AuthOperationType): AuthOperation {
        return when (type) {
            AuthOperationType.SIGN_IN -> SignInOperation()
            AuthOperationType.SIGN_UP -> SignUpOperation()
            AuthOperationType.SIGN_OUT -> SignOutOperation()
            AuthOperationType.PASSWORD_RESET -> PasswordResetOperation()
        }
    }

    // Perform the added operation
    suspend fun performAuthAction(state: FormState): Result<*> {
        val currentOperation = operation ?: return Result.Success
        return if (state.uiState == UIState.Loading) currentOperation.performAuthAction(state)
        else Result.Error(STATE_ERROR)
    }

    // Clear the current operation
    fun clearOperation() {
        this.operation = null
    }
}