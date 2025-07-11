package com.example.apptemplates.presentation.login.sign_in

import android.content.SharedPreferences
import com.example.apptemplates.firebase.auth.AuthService
import com.example.apptemplates.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.firebase.auth.operation.AuthOperationType
import com.example.apptemplates.firebase.database.FirestoreRepository
import com.example.apptemplates.firebase.database.FirestoreService
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.form.UIState
import com.example.apptemplates.navigation.event.NavigationEvent
import com.example.apptemplates.viewmodel.BaseLoginViewModel


class SignInViewModel(
    private val sharedPreferences: SharedPreferences,
    private val repository: AuthService = FirebaseAuthManager,
    private val database: FirestoreService = FirestoreRepository
) : BaseLoginViewModel(sharedPreferences) {

    init {
        validation
            .addValidator(FormKey.EMAIL)
            .addValidator(FormKey.PASSWORD)
            .addValidator(FormKey.DATABASE_EMAIL_PASSWORD)
            .addValidator(FormKey.ATTEMPTS)

        authManager.addOperation(AuthOperationType.SIGN_IN)
    }


    override fun onSuccess() {
        _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
        clearState()
    }

    override fun <T> onSuccessWithData(result: T) {
        _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
    }

    override fun onError(error: String) {
        if (state.value.uiState == UIState.Loading) {
            _state.value = _state.value.copy(uiState = UIState.Idle)
            _navigationEvent.tryEmit(NavigationEvent.ShowError(error))
        }


        if (state.value.attempts == 3)
            _navigationEvent.tryEmit(NavigationEvent.ShowError(error))
    }


}