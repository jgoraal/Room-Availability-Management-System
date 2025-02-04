package com.example.apptemplates.presentation.login.login_menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.data.firebase.database.FirestoreRepository
import com.example.apptemplates.data.firebase.database.result.FirestoreResult
import com.example.apptemplates.data.model.model.user.User
import com.example.apptemplates.domain.usecase.ActiveUser
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


    private fun checkUserAuthentication() {
        viewModelScope.launch(SupervisorJob()) {
            val firebaseUser = FirebaseAuthManager.currentUser
            Log.e("AuthViewModel", "checkUserAuthentication: ${firebaseUser?.uid}")
            firebaseUser?.let { setUserAuthenticated(it) } ?: updateState(false)
        }
    }



    private suspend fun setUserAuthenticated(firebaseUser: FirebaseUser) {
        val user = fetchUserFromFirestore(firebaseUser.uid) ?: run {
            Log.e("AuthViewModel", "User not found in Firestore")
            return updateState(false)
        }

        val isEmailVerified = firebaseUser.isEmailVerified
        if (isEmailVerified && !user.verified) {
            val updatedUser = user.copy(verified = true)
            val updateResult = FirestoreRepository.updateUser(updatedUser)
            if (updateResult is FirestoreResult.Success) {
                ActiveUser.setUser(updatedUser)
                return updateState(true)
            }
        }

        ActiveUser.setUser(user.copy(verified = isEmailVerified))
        updateState(isEmailVerified)
    }


    fun logout() {
        FirebaseAuth.getInstance().signOut()
        ActiveUser.clearUser()
        updateState(false)
    }


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


    private fun updateState(isAuthenticated: Boolean?) {
        state.value = isAuthenticated
    }
}
