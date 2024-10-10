package com.example.apptemplates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.firebase.auth.AuthManager
import com.example.apptemplates.firebase.auth.AuthResponse
import com.example.apptemplates.firebase.auth.AuthResponseCollector
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreDatabase
import com.example.apptemplates.form.FormState
import com.example.apptemplates.result.Result
import com.example.apptemplates.validation.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class BaseLoginViewModel(
    protected val validation: Validation = Validation(),
    protected val authManager: AuthManager = AuthManager(),
    private val repository: AuthResponse = AuthResponseCollector,
    private val database: FirestoreDatabase = Database
) : ViewModel() {

    protected val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state.asStateFlow()

    fun onStateChange(updatedState: FormState) {
        _state.value = validation.validateForm(updatedState)
    }

    // General function for handling form validation and authentication
    fun authenticate() {
        viewModelScope.launch {

            _state.value =
                validation.validateAuthentication(_state.value)


            when (val result = authManager.performAuthAction(_state.value)) {
                is Result.Success -> onSuccess()
                is Result.Error -> onError(result.error)
                is Result.SuccessWithResult<*> -> onSuccessWithData(result.data)
            }
        }
    }


    protected abstract fun onSuccess()
    protected abstract fun <T> onSuccessWithData(result: T)
    protected abstract fun onError(error: String)


}