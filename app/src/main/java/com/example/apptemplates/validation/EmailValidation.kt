package com.example.apptemplates.validation

import android.util.Patterns
import com.example.apptemplates.result.Result

class EmailValidation : Validator {
    override fun validate(value: String): Result<String> {
        return when {
            value.isBlank() -> Result.Error("Proszę wypełnić pole email")
            !Patterns.EMAIL_ADDRESS.matcher(value)
                .matches() -> Result.Error("Niewłaściwy format email")

            else -> Result.Success
        }
    }
}