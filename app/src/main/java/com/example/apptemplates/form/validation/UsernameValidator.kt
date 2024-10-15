package com.example.apptemplates.form.validation

import com.example.apptemplates.result.Result

class UsernameValidator : Validator {

    companion object {
        private const val USERNAME_MIN_LENGTH = 3
        private const val USERNAME_MAX_LENGTH = 15
        private val USERNAME_REGEX = Regex("^(?=.{3,15}$)(?![_.])[a-zA-Z0-9._]+(?<![_.])$")

        private const val ERROR_BLANK_USERNAME = "Proszę wybrać nazwę użytkownika"
        private const val ERROR_USERNAME_TOO_SHORT =
            "Nazwa użytkownika powinna być dłuższa niż $USERNAME_MIN_LENGTH znaków"
        private const val ERROR_USERNAME_TOO_LONG =
            "Nazwa użytkownika powinna być krótsza niż $USERNAME_MAX_LENGTH znaków"
        private const val ERROR_USERNAME_INVALID = "Niepoprawny format nazwy użytkownika"
    }


    override fun validate(value: String): Result<String> {
        return when {
            value.isBlank() -> Result.Error(ERROR_BLANK_USERNAME)
            value.length < USERNAME_MIN_LENGTH -> Result.Error(ERROR_USERNAME_TOO_SHORT)
            value.length > USERNAME_MAX_LENGTH -> Result.Error(ERROR_USERNAME_TOO_LONG)
            !value.matches(USERNAME_REGEX) -> Result.Error(ERROR_USERNAME_INVALID)
            else -> Result.Success
        }
    }
}