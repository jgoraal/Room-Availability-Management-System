package com.example.apptemplates.validation

import com.example.apptemplates.result.Result

class AttemptsValidator : Validator {
    companion object {
        private const val MAX_ATTEMPTS = 2
    }

    override fun validate(value: String): Result<String> {
        return when {
            value.toInt() >= MAX_ATTEMPTS -> Result.Error("Liczba prób przekroczona")
            else -> Result.Success
        }
    }
}