package com.example.apptemplates.utils.validation

import com.example.apptemplates.data.firebase.database.result.Result

class AttemptsValidator : Validator {
    companion object {
        private const val MAX_ATTEMPTS = 3
        private const val TIMEOUT_DURATION = 60000L
        private const val MAX_ATTEMPTS_ERROR = "Liczba prób przekroczona"
    }

    override fun validate(value: String): Result<String> {
        return when {
            value.toInt() >= MAX_ATTEMPTS -> Result.Error(MAX_ATTEMPTS_ERROR)
            else -> Result.Success
        }
    }

    fun isTimedOut(timeoutStart: Long?): Boolean {
        if (timeoutStart == null) return false
        val currentTime = System.currentTimeMillis()
        return currentTime - timeoutStart < TIMEOUT_DURATION
    }

    fun getTimeoutDuration(): Long {
        return TIMEOUT_DURATION
    }
}