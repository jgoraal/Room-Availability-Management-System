package com.example.apptemplates.presentation.login.sign_up_confirm

import android.util.Log
import com.example.apptemplates.data.firebase.auth.operation.AuthOperationType
import com.example.apptemplates.domain.usecase.ActiveUser
import com.example.apptemplates.presentation.navigation.event.NavigationEvent
import com.example.apptemplates.presentation.state.FormKey
import com.example.apptemplates.presentation.viewmodel.BaseLoginViewModel
import kotlinx.coroutines.flow.update

class SignUpConfirmViewModel(
) : BaseLoginViewModel() {


    init {
        validation
            .addValidator(FormKey.PASSWORD)
            .addValidator(FormKey.CONFIRM_PASSWORD)

        authManager
            .addOperation(AuthOperationType.SIGN_UP)


        try {

            _state.update {
                it.copy(
                    username = ActiveUser.getUser()!!.username,
                    email = ActiveUser.getUser()!!.email
                )
            }


            ActiveUser.clearUser()

        } catch (e: Exception) {
            Log.e("SignUpConfirmViewModel", "Error during initialization: ${e.message}")
        }


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

}