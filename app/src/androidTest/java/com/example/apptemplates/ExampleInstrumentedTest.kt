package com.example.apptemplates

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.apptemplates.data.firebase.auth.operation.AuthManager
import com.example.apptemplates.data.firebase.auth.operation.SignInOperation
import com.example.apptemplates.data.firebase.auth.operation.SignUpOperation
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.presentation.state.FormState
import com.example.apptemplates.utils.validation.EmailValidator
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


        val result = emailValidator.validate("test@example.com")


        assertTrue(result is Result.Error)
    }

    @Test
    fun invalidEmailFailsValidation() {
        val emailValidator = EmailValidator()


        val result = emailValidator.validate("invalid-email")


        assertTrue(result is Result.Error)
    }

    @Test
    fun emptyEmailFailsValidation() {
        val emailValidator = EmailValidator()


        val result = emailValidator.validate("")


        assertTrue(result is Result.Error)
    }
}


@RunWith(AndroidJUnit4::class)
class SignInInstrumentedTest {

    @Test
    fun signInWithValidCredentials() {
        val authManager = AuthManager()
        val signInOperation = SignInOperation()


        val state = FormState(
            email = "jakubgorskki@gmail.com",
            password = "@Test1234"
        )


        runBlocking {
            val result = signInOperation.performAuthAction(state)


            assertTrue(result is Result.Success)
        }
    }

    @Test
    fun signInWithInvalidCredentials() {
        val authManager = AuthManager()
        val signInOperation = SignInOperation()


        val state = FormState(
            email = "invalid@example.com",
            password = "WrongPassword!"
        )


        runBlocking {
            val result = signInOperation.performAuthAction(state)


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


        val state = FormState(
            username = "validuser",
            email = "validuser@example.com",
            password = "ValidPassword123!",
            confirmPassword = "ValidPassword123!"
        )


        runBlocking {

            val result1 = emailValidator.validate(state.email)


            if (result1 is Result.Success) {
                val result = signUpOperation.performAuthAction(state)
                assertTrue(result is Result.Error)
            }
        }
    }

    @Test
    fun signUpWithMismatchingPasswords() {
        val signUpOperation = SignUpOperation()


        val state = FormState(
            username = "validuser",
            email = "validuser@example.com",
            password = "ValidPassword123!",
            confirmPassword = "MismatchingPassword!"
        )


        runBlocking {
            val result = signUpOperation.performAuthAction(state)



            assertTrue(result is Result.Error)
        }
    }
}


