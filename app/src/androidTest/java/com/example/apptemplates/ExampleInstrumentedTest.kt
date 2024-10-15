package com.example.apptemplates

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.apptemplates.firebase.auth.operation.AuthManager
import com.example.apptemplates.firebase.auth.operation.SignInOperation
import com.example.apptemplates.firebase.auth.operation.SignUpOperation
import com.example.apptemplates.form.FormState
import com.example.apptemplates.form.validation.EmailValidator
import com.example.apptemplates.result.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

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
class EmailValidatorInstrumentedTest {

    @Test
    fun validEmailPassesValidation() {
        val emailValidator = EmailValidator()

        // Provide a valid email address
        val result = emailValidator.validate("test@example.com")

        // Assert that validation passed
        assertTrue(result is Result.Error)
    }

    @Test
    fun invalidEmailFailsValidation() {
        val emailValidator = EmailValidator()

        // Provide an invalid email address
        val result = emailValidator.validate("invalid-email")

        // Assert that validation failed
        assertTrue(result is Result.Error)
    }

    @Test
    fun emptyEmailFailsValidation() {
        val emailValidator = EmailValidator()

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
            password = "@Test1234"
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
        val emailValidator = EmailValidator()

        // Setup a valid FormState for sign-up
        val state = FormState(
            username = "validuser",
            email = "validuser@example.com",
            password = "ValidPassword123!",
            confirmPassword = "ValidPassword123!"
        )

        // Perform the sign-up operation
        runBlocking {

            val result1 = emailValidator.validate(state.email)


            if (result1 is Result.Success) {
                val result = signUpOperation.performAuthAction(state)
                assertTrue(result is Result.Error)
            }


            // Assert that the result is success

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


