package com.example.apptemplates.presentation.main.profile

import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.profile.domain.SignOutUseCase
import com.example.apptemplates.viewmodel.MainViewModel
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val signOutUseCase: SignOutUseCase = SignOutUseCase()
) : MainViewModel() {


    fun signOut() {


        wrapWithLoadingState(
            successState = {
                _state.update { it.copy(screenState = ScreenState.Success) }


            },
            errorState = { message ->
                _state.update {
                    it.copy(
                        screenState = ScreenState.Error(
                            UiError.UnknownError(
                                message
                            )
                        )
                    )
                }
            },
            {
                signOutUseCase()
            }
        )


    }


}

data class ProfileState(
    val username: String = "jgoraal",
    val role: String = "Guest",
    val email: String = "jakubgorskki@gmail.com",
    val isEmailVerified: Boolean = true,
    val overallReservationCount: Int = 2,
    val lastSeen: Long = 1,
    val screenState: ScreenState = ScreenState.Idle,
)
