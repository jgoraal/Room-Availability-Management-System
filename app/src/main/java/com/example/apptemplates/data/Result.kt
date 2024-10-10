package com.example.apptemplates.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.firebase.auth.AuthResponse
import com.example.apptemplates.firebase.database.FirestoreDatabase
import com.example.apptemplates.presentation.login.sign_in.validation.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class Result<out T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
}

enum class FormKey {
    NAME,
    EMAIL,
    PASSWORD,
    CONFIRM_PASSWORD,
    ATTEMPTS,
    UI_STATE,
    AUTH,
    DATABASE,
}


data class FormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errors: Map<FormKey,String?> = emptyMap(),
    val uiState: UIState = UIState.Idle
)





abstract class BaseLoginViewModel(
    private val validation: Validation,
    private val repository: AuthResponse,
    private val database: FirestoreDatabase
): ViewModel() {

    private val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state.asStateFlow()

     fun onStateChange(updatedState: FormState) {
        _state.value = validation.validate(updatedState)
    }


    // General function for handling form validation and authentication
    fun authenticate() {
        viewModelScope.launch {
            // Here we would trigger validation and authentication flow
            when (val result = performAuthAction()) {
                is Result.Success<*> -> onSuccess(result.data)
                is Result.Error -> onError(result.error)
            }
        }
    }

    abstract fun performAuthAction(): Result<*>


    abstract fun onSuccess(data: Any?)
    abstract fun onError(error: String)

}



