package com.example.apptemplates.firebase.auth

sealed class AuthResult {
    data object Success : AuthResult()
    data class Failure(val exception: Exception) : AuthResult()
}