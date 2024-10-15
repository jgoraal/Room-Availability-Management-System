package com.example.apptemplates.presentation.login.sign_up_confirm

import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.firebase.auth.operation.AuthOperationType
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.navigation.event.NavigationEvent
import com.example.apptemplates.viewmodel.BaseLoginViewModel

class SignUpConfirmViewModel(
) : BaseLoginViewModel() {


    init {
        validation
            .addValidator(FormKey.PASSWORD)
            .addValidator(FormKey.CONFIRM_PASSWORD)

        authManager
            .addOperation(AuthOperationType.SIGN_UP)

        onStateChange(
            state.value.copy(
                username = ActiveUser.getUser()!!.username,
                email = ActiveUser.getUser()!!.email
            )
        )
        ActiveUser.clearUser()
    }


    override fun onSuccess() {
        _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
    }

    override fun <T> onSuccessWithData(result: T) {
        _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
    }

    override fun onError(error: String) {
        _navigationEvent.tryEmit(NavigationEvent.ShowError(error))
    }


    /*private val _signUpFormState = MutableStateFlow(
        SignUpFormState().copy(
            username = ActiveUser.getUser()!!.username,
            email = ActiveUser.getUser()!!.email
        )
    )
    val signUpFormState: StateFlow<SignUpFormState> = _signUpFormState.asStateFlow()


    // Passwords change handler and button handler
    fun onFormChange(updatedState: SignUpFormState) {
        _signUpFormState.value = signUpUseCase.validateSignUpForm(updatedState)
    }

    // Main sign-up function
    fun signUp(
        state: SignUpFormState,
        onConfirm: () -> Unit,
        onNavigateBack: () -> Unit
    ) {

        viewModelScope.launch {
            _signUpFormState.value = signUpUseCase.validateUsernameAndEmail(state)

            if (_signUpFormState.value.isUsernameValid && _signUpFormState.value.isEmailValid) {


                _signUpFormState.value = signUpUseCase.validateSignUpForm(state)


                if (_signUpFormState.value.isValid) {
                    onConfirm()
                } else {
                    Log.i(DEBUG_VALIDATION, "Form validation failed")
                }
            } else {
                onNavigateBack()
            }

        }
    }

    // Submitting the form and triggering sign-up logic
    fun onSignUpFormSubmit(
        onNavigateConfirm: () -> Unit,
        onDismiss: (String?) -> Unit
    ) {
        val currentState = _signUpFormState.value
        if (currentState.isValid) {

            viewModelScope.launch {

                when (val result = repository.signUpWithEmailAndPassword(
                    currentState.email,
                    currentState.password
                )) {
                    is AuthResult.Success -> {
                        Log.i("AUTH", "Sign up successful")
                        ActiveUser.updateUid(currentUser!!.uid)
                        ActiveUser.updatePassword(currentState.password)
                        sendVerificationEmail(onNavigateConfirm, onDismiss)
                    }

                    is AuthResult.Failure -> {
                        Log.e("AUTH", "Sign up failed: ${result.exception.message}")
                        onDismiss("Błąd podczas autentykacji")
                    }
                }

            }

        }

    }

    // Helper function to send verification email
    private suspend fun sendVerificationEmail(
        onNavigateConfirm: () -> Unit,
        onDismiss: (String?) -> Unit
    ) {
        when (val result = repository.sendEmailVerification()) {
            is AuthResult.Success -> {
                Log.i("AUTH", "Verification email sent")
                val user = ActiveUser.getUser() ?: return
                addUserToDatabase(user, onNavigateConfirm, onDismiss)

            }

            is AuthResult.Failure -> {
                Log.e("AUTH", "Failed to send verification email: ${result.exception.message}")
                onDismiss("Nie udało się wysłać maila weryfikacyjnego")
            }
        }
    }


    private suspend fun addUserToDatabase(
        user: User,
        onNavigateConfirm: () -> Unit,
        onDismiss: (String?) -> Unit
    ) {
        when (val result = database.addUser(user)) {
            is FirestoreResult.Success -> {
                Log.i("AUTH", "User added to database")
                onDismiss("Rejestracja przebiegła pomyślnie")
                onNavigateConfirm()
            }

            is FirestoreResult.Failure -> {
                Log.e("AUTH", "Failed to add user to database: ${result.exception.message}")
                onDismiss("Nie udało się stworzyć użytkownika")
            }

            else -> {
                onDismiss("Failed to add user to database")
            }
        }
    }*/
}