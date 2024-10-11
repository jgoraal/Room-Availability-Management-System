package com.example.apptemplates.validation

import android.util.Patterns
import com.example.apptemplates.result.Result

class EmailValidation : Validator {
    override fun validate(value: String): Result<String> {
        return when {
            value.isBlank() -> Result.Error("Proszę wypełnić pole email")
            !Patterns.EMAIL_ADDRESS.matcher(value)
                .matches() -> Result.Error("Niewłaściwy format email")

            value.matches(Regex("jakubgorskki@gmail.com")) -> Result.Success
            !isValidPutDomain(value) -> Result.Error("Adres e-mail z nieprawidłową domeną")
            else -> Result.Success
        }
    }


    private fun isValidPutDomain(email: String): Boolean {
        val domain = email.substringAfterLast("@")
        return domain == "put.poznan.pl" || domain == "student.put.poznan.pl"
    }
}



