package com.example.apptemplates.presentation.login.login_menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.data.user.User
import com.example.apptemplates.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.firebase.database.FirestoreRepository
import com.example.apptemplates.firebase.database.result.FirestoreResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val state = MutableStateFlow<Boolean?>(null)
    val isUserAuthenticated: StateFlow<Boolean?> = state.asStateFlow()

    init {
        checkUserAuthentication()
    }

    /**
     * Checks if a user is already authenticated in FirebaseAuth.
     */
    private fun checkUserAuthentication() {
        viewModelScope.launch(SupervisorJob()) {
            val firebaseUser = FirebaseAuthManager.currentUser
            Log.e("AuthViewModel", "checkUserAuthentication: ${firebaseUser?.uid}")
            firebaseUser?.let { setUserAuthenticated(it) } ?: updateState(false)
        }
    }


    /**
     * Fetches and sets the authenticated user details from Firestore.
     */
    private suspend fun setUserAuthenticated(firebaseUser: FirebaseUser) {
        val user = fetchUserFromFirestore(firebaseUser.uid) ?: run {
            Log.e("AuthViewModel", "User not found in Firestore")
            return updateState(false)
        }

        val isEmailVerified = firebaseUser.isEmailVerified
        if (isEmailVerified && !user.isVerified) {
            val updatedUser = user.copy(isVerified = true)
            val updateResult = FirestoreRepository.updateUser(updatedUser)
            if (updateResult is FirestoreResult.Success) {
                ActiveUser.setUser(updatedUser)
                return updateState(true)
            }
        }

        ActiveUser.setUser(user.copy(isVerified = isEmailVerified))
        updateState(isEmailVerified)
    }

    /**
     * Logs out the current user by clearing FirebaseAuth and ActiveUser data.
     */
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        ActiveUser.clearUser()
        updateState(false)
    }

    /**
     * Retrieves user information from Firestore based on UID.
     */
    private suspend fun fetchUserFromFirestore(uid: String): User? {
        return when (val result = FirestoreRepository.getUser(uid)) {
            is FirestoreResult.SuccessWithResult<*> -> result.data as? User
            is FirestoreResult.Failure -> {
                Log.e("AuthViewModel", "Error retrieving user: ${result.exception}")
                null
            }

            else -> null
        }
    }

    /**
     * Updates the authentication state.
     */
    private fun updateState(isAuthenticated: Boolean?) {
        state.value = isAuthenticated
    }
}
