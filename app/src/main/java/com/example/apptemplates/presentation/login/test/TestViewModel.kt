package com.example.apptemplates.presentation.login.test

import com.example.apptemplates.firebase.auth.AuthOperationType
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.viewmodel.BaseLoginViewModel

class TestViewModel : BaseLoginViewModel() {


    init {
        validation
            .addValidator(FormKey.USERNAME)
            .addValidator(FormKey.EMAIL)
            .addValidator(FormKey.PASSWORD)
            .addValidator(FormKey.CONFIRM_PASSWORD)
            .addValidator(FormKey.ATTEMPTS)
            .addValidator(FormKey.DATABASE_EMAIL)
            .addValidator(FormKey.DATABASE_USERNAME)

        authManager.addOperation(AuthOperationType.SIGN_IN)
    }


    override fun onSuccess() {
        _state.value = _state.value.copy(attempts = 0)
    }

    override fun <T> onSuccessWithData(result: T) {
        TODO("Not yet implemented")
    }

    override fun onError(error: String) {
        _state.value = _state.value.copy(attempts = _state.value.attempts + 1)
    }

}