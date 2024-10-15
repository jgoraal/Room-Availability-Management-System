package com.example.apptemplates.form.validation

import com.example.apptemplates.result.Result

class PasswordValidator : Validator {
    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private const val ERROR_BLANK_PASSWORD = "Proszę wypełnić pole hasło"
        private const val ERROR_PASSWORD_LENGTH =
            "Hasło musi zawierać więcej niż $PASSWORD_MIN_LENGTH znaków"
        private const val ERROR_PASSWORD_COMPLEXITY =
            "Hasło musi zawierać co najmniej jedną dużą literę, cyfrę i znak specjalny\nDozwolone znaki: @\$!#%*?&'\""
        private const val ERROR_PASSWORD_CONFIRM_BLANK = "Proszę powtórzyć hasło"
        private const val ERROR_PASSWORD_MISMATCH = "Hasła nie są zgodne"

        private val PASSWORD_REGEX =
            Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@^\"'$!#%*?&])[A-Za-z\\d@^\"'$!#%*?&]{8,}$")
    }

    override fun validate(value: String): Result<String> {
        return when {
            value.isBlank() -> Result.Error(ERROR_BLANK_PASSWORD)
            value.length < PASSWORD_MIN_LENGTH -> Result.Error(ERROR_PASSWORD_LENGTH)
            !value.matches(PASSWORD_REGEX) ->
                Result.Error(ERROR_PASSWORD_COMPLEXITY)

            else -> Result.Success
        }
    }


    fun validatePasswordConfirm(password: String, passwordConfirm: String): Result<String> {
        return when {
            passwordConfirm.isBlank() -> Result.Error(ERROR_PASSWORD_CONFIRM_BLANK)
            password != passwordConfirm -> Result.Error(ERROR_PASSWORD_MISMATCH)
            else -> Result.Success
        }

    }
}