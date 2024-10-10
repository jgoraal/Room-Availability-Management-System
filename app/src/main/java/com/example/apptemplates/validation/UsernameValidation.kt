package com.example.apptemplates.validation

import com.example.apptemplates.result.Result

class UsernameValidation : Validator {

    companion object {
        private const val USERNAME_MIN_LENGTH = 3
        private const val USERNAME_MAX_LENGTH = 15
        private val USERNAME_REGEX = Regex("^(?=.{3,15}$)(?![_.])[a-zA-Z0-9._]+(?<![_.])$")
    }


    override fun validate(value: String): Result<String> {
        return when {
            value.isBlank() -> Result.Error("Proszę wybrać nazwę użytkownika")
            value.length < USERNAME_MIN_LENGTH -> Result.Error("Nazwa użytkownika powinna być dłuższa niż $USERNAME_MIN_LENGTH znaków")
            value.length > USERNAME_MAX_LENGTH -> Result.Error("Nazwa użytkownika powinna być krótsza niż $USERNAME_MAX_LENGTH znaków")
            !value.matches(USERNAME_REGEX) -> Result.Error("Niepoprawny format nazwy użytkownika")
            else -> Result.Success
        }
    }
}