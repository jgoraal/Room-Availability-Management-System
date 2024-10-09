package com.example.apptemplates.presentation.login.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.core.Constants.DEBUG_AUTH
import com.example.apptemplates.data.ActiveUser
import com.example.apptemplates.data.User
import com.example.apptemplates.firebase.auth.AuthResponseCollector
import com.example.apptemplates.firebase.auth.AuthResponseCollector.currentUser
import com.example.apptemplates.firebase.auth.AuthResult
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpFormState
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase = SignUpUseCase(),
) : ViewModel() {

    private val _signUpFormState = MutableStateFlow(SignUpFormState())
    val signUpFormState: StateFlow<SignUpFormState> = _signUpFormState.asStateFlow()


    //  Change username and email state
    fun onUsernameAndEmailFormChange(updatedState: SignUpFormState) {
        _signUpFormState.value = signUpUseCase.validateUsernameAndEmailForm(updatedState)
    }


    // Button click handler
    fun goToPasswordFields(state: SignUpFormState, onNavigateConfirm: () -> Unit) {
        viewModelScope.launch {
            _signUpFormState.value = signUpUseCase.validateUsernameAndEmail(state)

            if (_signUpFormState.value.isUsernameValid && _signUpFormState.value.isEmailValid) {
                ActiveUser.setUser(User(username = state.username, email = state.email))
                onNavigateConfirm()
            }
        }
    }

    fun clearState() {
        _signUpFormState.value = SignUpFormState()
    }


    fun resendEmailVerification() {
        viewModelScope.launch {
            val result = AuthResponseCollector.sendEmailVerification()
            if (result is AuthResult.Success) {
                Log.i(DEBUG_AUTH, "Another verification email sent")
            } else {
                Log.i(DEBUG_AUTH, "Failed to send verification email")
            }
        }
    }


    private val _isEmailVerified = MutableStateFlow(false)
    val isEmailVerified: StateFlow<Boolean> = _isEmailVerified.asStateFlow()

    // Tworzymy CoroutineScope do zarządzania odświeżaniem statusu
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        startEmailVerificationChecker()
    }

    // Funkcja do regularnego sprawdzania statusu weryfikacji e-mail
    private fun startEmailVerificationChecker() {
        viewModelScope.launch {
            while (isActive) {
                checkEmailVerificationStatus()
                delay(5000) // Co 5 sekund odśwież status
            }
        }
    }

    private suspend fun checkEmailVerificationStatus() {
        try {
            if (currentUser != null) {
                currentUser?.reload()?.await()  // Odświeżenie danych użytkownika
                _isEmailVerified.value = currentUser?.isEmailVerified ?: false
            }
        } catch (exception: Exception) {
            Log.e("AUTH", "Error checking email verification status", exception)
        }

    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel() // Zatrzymanie cyklu, gdy ViewModel zostanie usunięty
    }


}

