package com.example.apptemplates.presentation.login.password_reset

import com.example.apptemplates.firebase.auth.operation.AuthOperationType
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.navigation.event.NavigationEvent
import com.example.apptemplates.viewmodel.BaseLoginViewModel

class ResetPasswordViewModel : BaseLoginViewModel() {


    init {
        validation
            .addValidator(FormKey.EMAIL)

        authManager.addOperation(AuthOperationType.PASSWORD_RESET)
    }


    override fun onSuccess() {
        _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
        clearState()
    }

    override fun <T> onSuccessWithData(result: T) {
        _navigationEvent.tryEmit(NavigationEvent.NavigateOnSuccess)
    }

    override fun onError(error: String) {
        _navigationEvent.tryEmit(NavigationEvent.ShowError(error))
    }


}