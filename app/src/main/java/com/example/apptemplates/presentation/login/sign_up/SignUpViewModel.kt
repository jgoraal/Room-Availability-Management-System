package com.example.apptemplates.presentation.login.sign_up

import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.form.UIState
import com.example.apptemplates.navigation.event.NavigationEvent
import com.example.apptemplates.viewmodel.BaseLoginViewModel

class SignUpViewModel(

) : BaseLoginViewModel() {


    init {
        validation
            .addValidator(FormKey.USERNAME)
            .addValidator(FormKey.EMAIL)
            .addValidator(FormKey.DATABASE_USERNAME)
            .addValidator(FormKey.DATABASE_EMAIL)
    }


    override fun onSuccess() {

        if (state.value.errors.isEmpty()) {
            ActiveUser.updateUsername(state.value.username)
            ActiveUser.updateEmail(state.value.email)

            _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
            _state.value = _state.value.copy(uiState = UIState.Idle)
        }

    }

    override fun <T> onSuccessWithData(result: T) {
        _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
    }

    override fun onError(error: String) {
        _navigationEvent.tryEmit(NavigationEvent.ShowError(error))
    }


    /*private val _signUpFormState = MutableStateFlow(SignUpFormState())
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
            val result = FirebaseAuthManager.sendEmailVerification()
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
    }*/

}

