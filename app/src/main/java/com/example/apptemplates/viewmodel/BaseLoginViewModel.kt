package com.example.apptemplates.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.firebase.auth.AuthService
import com.example.apptemplates.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.firebase.auth.operation.AuthManager
import com.example.apptemplates.firebase.database.FirestoreRepository
import com.example.apptemplates.firebase.database.FirestoreService
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.form.FormState
import com.example.apptemplates.form.UIState
import com.example.apptemplates.form.validation.Validation
import com.example.apptemplates.navigation.event.NavigationEvent
import com.example.apptemplates.result.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


abstract class BaseLoginViewModel(
    private val sharedPreferences: SharedPreferences? = null,
    protected val validation: Validation = Validation(),
    protected val authManager: AuthManager = AuthManager(),
    private val repository: AuthService = FirebaseAuthManager,
    private val database: FirestoreService = FirestoreRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_START_KEY = "timeout_start_key"
        private const val ATTEMPTS_KEY = "attempts_key"
        private const val MAX_ATTEMPTS = 3
        private const val TIMEOUT_DURATION_MS = 60000L // 60 seconds timeout
    }


    //  State for form and navigation events
    protected val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state.asStateFlow()

    protected val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()


    init {
        loadTimeoutData()
    }


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


    // SharedPreferences methods for timeout data
    private fun saveTimeoutData(timeoutStart: Long, attempts: Int) {


        sharedPreferences?.edit()?.apply {
            putLong(TIMEOUT_START_KEY, timeoutStart)
            putInt(ATTEMPTS_KEY, attempts)
            apply()
        }


    }

    private fun loadTimeoutData() {

        if (sharedPreferences != null) {

            viewModelScope.launch {

                val timeoutStart = sharedPreferences.getLong(TIMEOUT_START_KEY, 0L)
                val attempts = sharedPreferences.getInt(ATTEMPTS_KEY, 0)

                if (timeoutStart > 0 && attempts >= MAX_ATTEMPTS) {
                    val remainingTime = getRemainingTimeInSeconds(timeoutStart)
                    if (remainingTime > 0) {
                        _state.value = _state.value.copy(
                            attempts = attempts,
                            uiState = UIState.Timeout("Zbyt wiele prób logowania, spróbuj ponownie za $remainingTime sekund"),
                            timeoutStart = timeoutStart,
                            timeoutRemaining = remainingTime
                        )
                        viewModelScope.launch { updateRemainingTime() }  // Kontynuowanie timeoutu po restarcie
                    } else {
                        resetAttempts()  // Jeśli timeout wygasł, resetujemy próby
                    }
                }

            }

        }
    }


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

        val currentTime = System.currentTimeMillis()
        saveTimeoutData(currentTime, MAX_ATTEMPTS)

        _state.value = _state.value.copy(
            attempts = MAX_ATTEMPTS,
            errors = emptyMap(),
            uiState = UIState.Timeout("Zbyt wiele prób logowania, spróbuj ponownie za ${getRemainingTimeInSeconds()} sekund"),
            timeoutStart = System.currentTimeMillis(),
            timeoutRemaining = timeoutDuration / 1000
        )

        updateRemainingTime()
    }

    private suspend fun updateRemainingTime() {

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


    private fun getRemainingTimeInSeconds(timeoutStart: Long = _state.value.timeoutStart): Long {
        val currentTime = System.currentTimeMillis()
        val timePassed = currentTime - timeoutStart
        val remainingTime = 60000L - timePassed
        return if (remainingTime > 0) remainingTime / 1000 else 0
    }


    //  Reset functions
    private fun resetAttempts() {

        val attemptsValidatorExists = validation.getValidators()[FormKey.ATTEMPTS] != null
        if (!attemptsValidatorExists) return

        _state.value = _state.value.copy(
            attempts = 0,
            uiState = UIState.Idle,
            timeoutStart = 0L
        )


        sharedPreferences?.edit()?.apply {
            remove(TIMEOUT_START_KEY)
            remove(ATTEMPTS_KEY)
            apply()
        }


    }

    fun resetNavigation() {
        _navigationEvent.value = null
    }

    fun clearState() {
        _state.value = FormState()
    }


    protected abstract fun onSuccess()
    protected abstract fun <T> onSuccessWithData(result: T)
    protected abstract fun onError(error: String)

}