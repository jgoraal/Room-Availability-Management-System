package com.example.apptemplates.data.firebase.auth.result

sealed class AuthResult {
    data object Success : AuthResult()
    data class Failure(val exception: Exception) : AuthResult()
}