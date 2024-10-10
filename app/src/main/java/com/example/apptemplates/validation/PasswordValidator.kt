package com.example.apptemplates.validation

import com.example.apptemplates.result.Result

class PasswordValidator : Validator {
    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
        private val PASSWORD_REGEX =
            Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
    }

    override fun validate(value: String): Result<String> {
        return when {
            value.isBlank() -> Result.Error("Proszę wypełnić pole hasło")
            value.length < PASSWORD_MIN_LENGTH -> Result.Error("Hasło musi zawierać więcej niż $PASSWORD_MIN_LENGTH znaków")
            !value.matches(PASSWORD_REGEX) -> Result.Error("Hasło musi zawierać co najmniej jedną dużą literę, cyfrę i znak specjalny")
            else -> Result.Success
        }
    }


    fun validatePasswordConfirm(password: String, passwordConfirm: String): Result<String> {
        return when {
            passwordConfirm.isBlank() -> Result.Error("Proszę powtórzyć hasło")
            password != passwordConfirm -> Result.Error("Hasła nie są zgodne")
            else -> Result.Success
        }

    }
}