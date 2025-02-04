package com.example.apptemplates.presentation.login.test

import android.content.SharedPreferences
import com.example.apptemplates.data.firebase.auth.operation.AuthOperationType
import com.example.apptemplates.presentation.state.FormKey
import com.example.apptemplates.presentation.viewmodel.BaseLoginViewModel

class TestViewModel(
    private val sharedPreferences: SharedPreferences,
) : BaseLoginViewModel(sharedPreferences) {


    init {
        validation
            .addValidator(FormKey.USERNAME)
            .addValidator(FormKey.EMAIL)
            .addValidator(FormKey.PASSWORD)
            .addValidator(FormKey.CONFIRM_PASSWORD)
            .addValidator(FormKey.ATTEMPTS)
            .addValidator(FormKey.DATABASE_EMAIL)
            .addValidator(FormKey.DATABASE_USERNAME)

        authManager.addOperation(AuthOperationType.SIGN_UP)
    }


    override fun onSuccess() {

    }

    override fun <T> onSuccessWithData(result: T) {

    }

    override fun onError(error: String) {

    }

}