package com.example.apptemplates.presentation.login.sign_in

import android.util.Log
import com.example.apptemplates.firebase.auth.AuthOperationType
import com.example.apptemplates.firebase.auth.AuthResponse
import com.example.apptemplates.firebase.auth.AuthResponseCollector
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreDatabase
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.presentation.login.sign_in.validation.SignInValidation
import com.example.apptemplates.viewmodel.BaseLoginViewModel


sealed class SignInResult {
    data object Success : SignInResult()
    data class Error(val message: String) : SignInResult()
}

class SignInViewModel(
    private val signInValidation: SignInValidation = SignInValidation(),
    private val repository: AuthResponse = AuthResponseCollector,
    private val database: FirestoreDatabase = Database
) : BaseLoginViewModel() {


    init {
        validation
            .addValidator(FormKey.EMAIL)
            .addValidator(FormKey.PASSWORD)
            .addValidator(FormKey.DATABASE_EMAIL_PASSWORD)
            .addValidator(FormKey.ATTEMPTS)

        authManager.addOperation(AuthOperationType.SIGN_IN)
    }


    override fun onSuccess() {
        Log.i("TAG", "onSuccess: ")
    }

    override fun <T> onSuccessWithData(result: T) {
        Log.i("TAG", "onSuccessWithData: $result")
    }

    override fun onError(error: String) {
        Log.d("TAG", "onError: $error")
    }


    /*private val _signInState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInState.asStateFlow()

    fun onStateChange(updatedState: SignInState) {
        _signInState.value = signInValidation.validateForm(updatedState)
    }


    // SignIn function with navigation callbacks
    fun signIn(
        navigateToHome: () -> Unit,
        navigateBack: () -> Unit
    ) {
        viewModelScope.launch {
            // Update attempts before validation
            val updatedState = _signInState.value.copy(attempts = _signInState.value.attempts + 1)
            _signInState.value = signInValidation.validateAndUpdateAttempts(updatedState)

            // Only proceed if form is valid (isLoading will be true when valid)
            if (_signInState.value.uiState == UIState.Loading) {
                // Sign in to Firebase
                when (val result = signInValidation.signInToFirebase(_signInState.value)) {
                    is SignInResult.Success -> onSuccess(navigateToHome)
                    is SignInResult.Error -> onError(result, navigateBack)
                }
            }
        }
    }


    // Called on successful sign-in
    private fun onSuccess(navigateToHome: () -> Unit) {
        // Update state to reflect success
        _signInState.value = _signInState.value.copy(
            attempts = 0,  // Reset attempts on success
            uiState = UIState.Success
        )
        navigateToHome() // Navigate to the home screen
    }

    // Called on sign-in error
    private fun onError(result: SignInResult.Error, navigateBack: () -> Unit) {
        _signInState.value = _signInState.value.copy(
            uiState = UIState.Error(result.message),
            errors = _signInState.value.errors
                .plus(ValidationKey.FIREBASE.name.lowercase() to result.message)
        )

        // Check if max attempts exceeded
        if (_signInState.value.attempts >= SignInValidation.MAX_ATTEMPTS) {
            navigateBack()  // Navigate back or show an error message
        }
    }*/


}