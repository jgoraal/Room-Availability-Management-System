package com.example.apptemplates.result

sealed class Result<out T> {
    data object Success : Result<Nothing>()
    data class SuccessWithResult<T>(val data: T?) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
}