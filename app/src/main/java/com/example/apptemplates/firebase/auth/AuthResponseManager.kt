package com.example.apptemplates.firebase.auth

import android.util.Log
import com.example.apptemplates.data.User
import com.example.apptemplates.firebase.database.createUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//====================================================================================================
//
// Sign up functions
//
//====================================================================================================

suspend fun handleSignUp(email: String, password: String, onSignUpResult: (Boolean) -> Unit) {
    when (val result = AuthResponseCollector.signUpWithEmailAndPassword(email, password)) {
        is AuthResult.Success -> {
            Log.i("AUTH", "Sign up successful")
            sendVerificationEmail()

            onSignUpResult(true)
        }

        is AuthResult.Failure -> {
            Log.i("AUTH", "Sign up failed: ${result.exception.message}")
            onSignUpResult(false)
        }
    }
}


suspend fun sendVerificationEmail() {
    when (val result = AuthResponseCollector.sendEmailVerification()) {
        is AuthResult.Success -> {
            Log.i("AUTH", "Verification email sent")
        }

        is AuthResult.Failure -> {
            Log.i("AUTH", "Failed to send verification email: ${result.exception.message}")
        }
    }
}


//====================================================================================================
//
// Firebase user utils
//
//====================================================================================================


suspend fun reloadUserAndCheckIfEmailIsVerified() {
    when (val result = AuthResponseCollector.reloadFirebaseUser()) {
        is AuthResult.Success -> {
            Log.i("AUTH", "User state refreshed successfully.")



        }

        is AuthResult.Failure -> {
            Log.i("AUTH", "Failed to refresh user state: ${result.exception.message}")
        }
    }
}


//====================================================================================================
//
// Session timeout functions
//
//====================================================================================================

suspend fun handleSignUpSession(
    user: User,
    scope: CoroutineScope,
    onVerificationSuccess: (Boolean) -> Unit,
) {


    scope.launch {
        delay(60000) // Wait for 10 seconds
        if (!AuthResponseCollector.currentUser?.isEmailVerified!!) {
            Log.i("AUTH", "Session timed out, signing out")

            AuthResponseCollector.signOut()
        } else {
            this.cancel()
            Log.i("AUTH", "Session not timed out, continuing")
        }
    }

    startEmailVerificationCheck(user, scope, onVerificationSuccess)

}


suspend fun startEmailVerificationCheck(
    user: User,
    scope: CoroutineScope,
    onVerificationSuccess: (Boolean) -> Unit
) {
    // Refresh user state to ensure email verification status is up-to-date
    when (val result = AuthResponseCollector.reloadFirebaseUser()) {
        is AuthResult.Success -> {
            Log.i("AUTH", "User state refreshed successfully.")
        }

        is AuthResult.Failure -> {
            Log.i("AUTH", "Failed to refresh user state: ${result.exception.message}")
        }
    }

    AuthResponseCollector.getAuthState(scope).collect { isAuthenticated ->
        val currentUser = AuthResponseCollector.currentUser
        if (isAuthenticated && currentUser != null) {
            if (currentUser.isEmailVerified) {
                Log.i("AUTH", "User email verified")
                createUser(user)  // Add user to the database after verification
                onVerificationSuccess(true) // Continue to the next screen
            } else {
                Log.i("AUTH", "User email not verified, please verify")
            }
        } else {
            Log.i("AUTH", "User not authenticated")
            onVerificationSuccess(false)
        }

    }
}


suspend fun isSessionTimeOut() {
    val currentUser = AuthResponseCollector.currentUser

    if (currentUser != null && !currentUser.isEmailVerified) {
        val accountCreationTime = currentUser.metadata?.creationTimestamp ?: 0
        val currentTime = System.currentTimeMillis()

        val sessionDuration = currentTime - accountCreationTime

        // Check if the session duration exceeds the timeout limit (e.g., 60 seconds)
        if (sessionDuration >= 1000 * 60) {
            Log.i("AUTH", "Session timed out. Revoking access...")

            when (val result = AuthResponseCollector.revokeAccess()) {
                is AuthResult.Success -> {
                    Log.i("AUTH", "Access revoked successfully.")
                }

                is AuthResult.Failure -> {
                    Log.i("AUTH", "Failed to revoke access: ${result.exception.message}")
                }
            }
        } else {
            Log.i("AUTH", "Session not timed out yet.")
        }
    } else {
        Log.i("AUTH", "User is either null or has verified the email.")
    }
}
