package com.example.apptemplates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.firebase.auth.AuthManager
import com.example.apptemplates.firebase.auth.AuthResponse
import com.example.apptemplates.firebase.auth.AuthResponseCollector
import com.example.apptemplates.firebase.database.Database
import com.example.apptemplates.firebase.database.FirestoreDatabase
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.form.FormState
import com.example.apptemplates.presentation.login.sign_in.validation.SignInValidation.Companion.MAX_ATTEMPTS
import com.example.apptemplates.presentation.login.sign_in.validation.UIState
import com.example.apptemplates.result.Result
import com.example.apptemplates.validation.Validation
import kotlinx.coroutines.delay
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

    private val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state.asStateFlow()

    fun onStateChange(updatedState: FormState) {
        _state.value = validation.validateForm(updatedState)
    }

    fun authenticate() {
        viewModelScope.launch {

            _state.value = validation.validateAuthentication(_state.value)

            when (val result = authManager.performAuthAction(_state.value)) {
                is Result.Success -> {
                    resetAttempts()
                    onSuccess()
                }

                is Result.Error -> {
                    increaseAttempts()
                    onError(result.error)
                }

                is Result.SuccessWithResult<*> -> {
                    resetAttempts()
                    onSuccessWithData(result.data)
                }
            }


        }
    }


    protected abstract fun onSuccess()
    protected abstract fun <T> onSuccessWithData(result: T)
    protected abstract fun onError(error: String)


    private suspend fun increaseAttempts() {

        val attemptsValidatorExists = validation.getValidators()[FormKey.ATTEMPTS] != null
        if (!attemptsValidatorExists || _state.value.uiState is UIState.Timeout) return

        val currentAttempts = _state.value.attempts + 1
        if (currentAttempts >= MAX_ATTEMPTS) {
            startTimeout()
        } else {
            _state.value = _state.value.copy(attempts = currentAttempts)
        }
    }

    private suspend fun startTimeout() {
        val timeoutDuration = 60000L // ustawiamy na 60 sekund

        _state.value = _state.value.copy(
            attempts = MAX_ATTEMPTS,
            errors = emptyMap(),
            uiState = UIState.Timeout("Zbyt wiele prób logowania, spróbuj ponownie za ${getRemainingTimeInSeconds()} sekund"),
            timeoutStart = System.currentTimeMillis(),
            timeoutRemaining = timeoutDuration / 1000
        )

        updateRemainingTime()
    }

    private fun resetAttempts() {

        val attemptsValidatorExists = validation.getValidators()[FormKey.ATTEMPTS] != null
        if (!attemptsValidatorExists) return

        _state.value = _state.value.copy(
            attempts = 0,
            uiState = UIState.Idle,
            timeoutStart = 0L
        )
    }

    private fun getRemainingTimeInSeconds(): Long {
        val currentTime = System.currentTimeMillis()
        val timePassed = currentTime - _state.value.timeoutStart
        val remainingTime = 60000L - timePassed
        return if (remainingTime > 0) remainingTime / 1000 else 0
    }

    private suspend fun updateRemainingTime() {
        viewModelScope.launch {
            var remainingTime = getRemainingTimeInSeconds()

            // Wyświetlamy od razu pełne 60 sekund (jeśli tyle zostało)
            _state.value = _state.value.copy(timeoutRemaining = remainingTime)

            while (_state.value.uiState is UIState.Timeout && remainingTime > 0) {
                delay(1000L) // Czekamy 1 sekundę
                remainingTime = getRemainingTimeInSeconds() // Przeliczamy czas po opóźnieniu
                _state.value =
                    _state.value.copy(timeoutRemaining = remainingTime) // Aktualizujemy stan
            }

            resetAttempts() // Resetowanie prób po zakończeniu timeoutu
        }
    }


}