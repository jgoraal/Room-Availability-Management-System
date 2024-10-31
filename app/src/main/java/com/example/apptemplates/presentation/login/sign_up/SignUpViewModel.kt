package com.example.apptemplates.presentation.login.sign_up

import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.data.user.User
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
            ActiveUser.setUser(User())
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


}

