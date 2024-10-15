package com.example.apptemplates.firebase.auth

import android.util.Log
import com.example.apptemplates.firebase.auth.result.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager : AuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signUpWithEmailAndPassword(
        email: String, password: String
    ): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }

    override suspend fun sendEmailVerification(): AuthResult {
        return currentUser?.let { user ->
            try {
                user.sendEmailVerification().await()
                AuthResult.Success
            } catch (e: Exception) {
                AuthResult.Failure(e)
            }
        } ?: AuthResult.Failure(Exception("No current user"))
    }

    override suspend fun signInWithEmailAndPassword(
        email: String, password: String
    ): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }

    override suspend fun reloadFirebaseUser(): AuthResult {
        return try {
            auth.currentUser?.reload()?.await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun revokeAccess(): AuthResult {
        return try {
            currentUser?.delete()?.await()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                trySend(true)  // valid user
            } else {
                trySend(false)  // Not valid
            }
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), auth.currentUser == null)


    override fun getEmailVerifiedState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // Ręcznie odśwież użytkownika, aby uzyskać najnowszy stan weryfikacji
                Log.i("AUTH", "Refreshing user data")
                currentUser.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (currentUser.isEmailVerified) {
                            Log.i("AUTH", "Email is verified")
                            trySend(true)  // E-mail zweryfikowany
                        } else {
                            Log.i("AUTH", "Email is not verified yet")
                            trySend(false)  // E-mail niezweryfikowany
                        }
                    } else {
                        Log.e("AUTH", "Error refreshing user data", task.exception)
                        trySend(false)  // Błąd podczas odświeżania
                    }
                }
            } else {
                trySend(false)  // Brak ważnego użytkownika
            }
        }

        // Dodaj i usuń nasłuchiwanie stanu uwierzytelnienia
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )


}
