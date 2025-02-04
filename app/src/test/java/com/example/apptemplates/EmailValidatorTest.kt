package com.example.apptemplates

import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.utils.validation.EmailValidator
import junit.framework.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {

    private val emailValidator = EmailValidator()

    @Test
    fun `valid email passes validation`() {
        val result = emailValidator.validate("test@example.com")
        assertTrue(result == Result.Success)
    }

    @Test
    fun `invalid email fails validation`() {
        val result = emailValidator.validate("invalid-email")
        assertTrue(result is Result.Error)
    }

    @Test
    fun `empty email fails validation`() {
        val result = emailValidator.validate("")
        assertTrue(result is Result.Error)
    }

}