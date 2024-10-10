package com.example.apptemplates

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.apptemplates.firebase.auth.AuthManager
import com.example.apptemplates.firebase.auth.SignInOperation
import com.example.apptemplates.firebase.auth.SignUpOperation
import com.example.apptemplates.form.FormState
import com.example.apptemplates.validation.EmailValidation
import com.example.apptemplates.result.Result
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.apptemplates", appContext.packageName)
    }
}


@RunWith(AndroidJUnit4::class)
class EmailValidationInstrumentedTest {

    @Test
    fun validEmailPassesValidation() {
        val emailValidator = EmailValidation()

        // Provide a valid email address
        val result = emailValidator.validate("test@example.com")

        // Assert that validation passed
        assertTrue(result is Result.Success)
    }

    @Test
    fun invalidEmailFailsValidation() {
        val emailValidator = EmailValidation()

        // Provide an invalid email address
        val result = emailValidator.validate("invalid-email")

        // Assert that validation failed
        assertTrue(result is Result.Error)
    }

    @Test
    fun emptyEmailFailsValidation() {
        val emailValidator = EmailValidation()

        // Provide an empty email address
        val result = emailValidator.validate("")

        // Assert that validation failed
        assertTrue(result is Result.Error)
    }
}


@RunWith(AndroidJUnit4::class)
class SignInInstrumentedTest {

    @Test
    fun signInWithValidCredentials() {
        val authManager = AuthManager()
        val signInOperation = SignInOperation()

        // Setup a valid FormState
        val state = FormState(
            email = "jakubgorskki@gmail.com",
            password = "@Testpass23"
        )

        // Perform the sign-in operation
        runBlocking {
            val result = signInOperation.performAuthAction(state)

            // Assert that the result is success
            assertTrue(result is Result.Success)
        }
    }

    @Test
    fun signInWithInvalidCredentials() {
        val authManager = AuthManager()
        val signInOperation = SignInOperation()

        // Setup an invalid FormState
        val state = FormState(
            email = "invalid@example.com",
            password = "WrongPassword!"
        )

        // Perform the sign-in operation
        runBlocking {
            val result = signInOperation.performAuthAction(state)

            // Assert that the result is an error
            assertTrue(result is Result.Error)
        }
    }
}


@RunWith(AndroidJUnit4::class)
class SignUpInstrumentedTest {

    @Test
    fun signUpWithValidCredentials() {
        val signUpOperation = SignUpOperation()

        // Setup a valid FormState for sign-up
        val state = FormState(
            username = "validuser",
            email = "validuser@example.com",
            password = "ValidPassword123!",
            confirmPassword = "ValidPassword123!"
        )

        // Perform the sign-up operation
        runBlocking {
            val result = signUpOperation.performAuthAction(state)

            // Assert that the result is success
            assertTrue(result is Result.Success)
        }
    }

    @Test
    fun signUpWithMismatchingPasswords() {
        val signUpOperation = SignUpOperation()

        // Setup a FormState with mismatching passwords
        val state = FormState(
            username = "validuser",
            email = "validuser@example.com",
            password = "ValidPassword123!",
            confirmPassword = "MismatchingPassword!"
        )

        // Perform the sign-up operation
        runBlocking {
            val result = signUpOperation.performAuthAction(state)

            // Assert that the result is an error
            assertTrue(result is Result.Error)
        }
    }
}


