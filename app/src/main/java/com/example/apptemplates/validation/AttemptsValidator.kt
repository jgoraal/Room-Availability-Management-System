package com.example.apptemplates.validation

import com.example.apptemplates.result.Result

class AttemptsValidator : Validator {
    companion object {
        private const val MAX_ATTEMPTS = 3
        private const val TIMEOUT_DURATION = 60000L // Timeout for 60 seconds
    }

    override fun validate(value: String): Result<String> {
        return when {
            value.toInt() >= MAX_ATTEMPTS -> Result.Error("Liczba prób przekroczona")
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