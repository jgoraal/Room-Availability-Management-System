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
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // StateFlow to observe if the user is authenticated
    private val _isUserAuthenticated = MutableStateFlow(false)
    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated.asStateFlow()

    init {
        checkUserAuthentication()
    }

    // Private function to check user authentication status
    private fun checkUserAuthentication() {
        viewModelScope.launch {
            try {
                val firebaseUser = FirebaseAuthManager.currentUser
                if (firebaseUser != null) {
                    setUserAuthenticated(firebaseUser)
                } else {
                    Log.i("AUTH", "No user authenticated")
                    _isUserAuthenticated.value = false
                }
            } catch (e: FirebaseAuthException) {
                Log.e("AUTH", "Firebase Authentication error: ${e.message}", e)
                _isUserAuthenticated.value = false
            } catch (e: Exception) {
                Log.e("AUTH", "Unexpected error: ${e.message}", e)
                _isUserAuthenticated.value = false
            }
        }
    }

    // Sets the authenticated user and updates the state
    private suspend fun setUserAuthenticated(firebaseUser: FirebaseUser) {
        try {
            Log.i("AUTH", "User authenticated")

            val user = when (val result = FirestoreRepository.getUser(firebaseUser.uid)) {
                is FirestoreResult.SuccessWithResult<*> -> {
                    result.data as User
                }

                is FirestoreResult.Failure -> {
                    throw result.exception
                }

                else -> {
                    null
                }
            }

            if (user != null) {
                ActiveUser.setUser(user)

                if (firebaseUser.isEmailVerified && !user.isVerified) {
                    when (FirestoreRepository.updateUser(user.copy(isVerified = true))) {
                        is FirestoreResult.Success -> {
                            ActiveUser.setUser(user.copy(isVerified = true))
                        }

                        is FirestoreResult.Failure -> {
                            ActiveUser.setUser(user.copy(isVerified = false))
                        }

                        else -> {}
                    }
                }

                _isUserAuthenticated.value = true
            } else {
                Log.e("AUTH", "User not found in Firestore")
                _isUserAuthenticated.value = false
            }

        } catch (e: Exception) {
            Log.e("AUTH", "Error retrieving user from Firestore: ${e.message}", e)
            _isUserAuthenticated.value = false
        }
    }

    // Logout function to clear user authentication
    fun logout() {
        FirebaseAuth.getInstance().signOut()
        ActiveUser.clearUser()
        _isUserAuthenticated.value = false
    }
}
