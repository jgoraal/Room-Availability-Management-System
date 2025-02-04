package com.example.apptemplates.data.firebase.auth


import com.example.apptemplates.data.firebase.auth.result.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow


typealias AuthStateResponse = StateFlow<Boolean>

interface AuthService {

    val currentUser: FirebaseUser?

    suspend fun signUpWithEmailAndPassword(email: String, password: String): AuthResult

    suspend fun sendEmailVerification(): AuthResult

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult

    suspend fun reloadFirebaseUser(): AuthResult

    suspend fun sendPasswordResetEmail(email: String): AuthResult

    fun signOut()

    suspend fun revokeAccess(): AuthResult

    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse

    fun getEmailVerifiedState(viewModelScope: CoroutineScope): AuthStateResponse
}
