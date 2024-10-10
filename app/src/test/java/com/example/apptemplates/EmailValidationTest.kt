package com.example.apptemplates

import com.example.apptemplates.firebase.auth.AuthResponseCollector
import com.example.apptemplates.firebase.auth.AuthResult
import com.example.apptemplates.firebase.auth.SignUpOperation
import com.example.apptemplates.form.FormState
import com.example.apptemplates.validation.EmailValidation
import junit.framework.Assert.assertTrue
import org.junit.Test
import com.example.apptemplates.result.Result
import kotlinx.coroutines.runBlocking
import org.junit.Before

class EmailValidationTest {

    private val emailValidator = EmailValidation()

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