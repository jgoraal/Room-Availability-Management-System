package com.example.apptemplates.form.validation

import android.util.Patterns
import com.example.apptemplates.result.Result

class EmailValidator : Validator {

    companion object {
        const val ERROR_EMAIL_BLANK = "Proszę wypełnić pole email"
        const val ERROR_INVALID_EMAIL_FORMAT = "Niewłaściwy format email"
        const val ERROR_INVALID_DOMAIN = "Adres e-mail z nieprawidłową domeną"
        const val SUCCESS_EMAIL = "jakubgorskki@gmail.com"
        val VALID_DOMAINS =
            listOf("put.poznan.pl", "student.put.poznan.pl", "doctorate.put.poznan.pl")
    }


    override fun validate(value: String): Result<String> {
        return when {
            value.isBlank() -> Result.Error(ERROR_EMAIL_BLANK)
            !Patterns.EMAIL_ADDRESS.matcher(value)
                .matches() -> Result.Error(ERROR_INVALID_EMAIL_FORMAT)

            value.matches(Regex(SUCCESS_EMAIL)) -> Result.Success
            !isValidPutDomain(value) -> Result.Error(ERROR_INVALID_DOMAIN)
            else -> Result.Success
        }
    }


    private fun isValidPutDomain(email: String): Boolean {
        val domain = email.substringAfterLast("@")
        return VALID_DOMAINS.contains(domain)
    }
}



