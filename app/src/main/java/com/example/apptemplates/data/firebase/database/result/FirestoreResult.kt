package com.example.apptemplates.data.firebase.database.result

sealed class FirestoreResult {
    data object Success : FirestoreResult()
    data class SuccessWithResult<T>(val data: T?) : FirestoreResult()
    data class Failure(val exception: Exception) : FirestoreResult()
}

