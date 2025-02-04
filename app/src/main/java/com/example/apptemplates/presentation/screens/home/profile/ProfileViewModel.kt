package com.example.apptemplates.presentation.screens.home.profile

import android.net.Uri
import com.example.apptemplates.domain.usecase.ActiveUser
import com.example.apptemplates.domain.usecase.DeleteAccountUseCase
import com.example.apptemplates.domain.usecase.ProfileImageUseCase
import com.example.apptemplates.domain.usecase.SignOutUseCase
import com.example.apptemplates.presentation.screens.home.start.ActiveReservations
import com.example.apptemplates.presentation.screens.home.start.ActiveRooms
import com.example.apptemplates.presentation.state.ScreenState
import com.example.apptemplates.presentation.state.UiError
import com.example.apptemplates.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.flow.update

class ProfileViewModel(
    private val signOutUseCase: SignOutUseCase = SignOutUseCase(),
    private val deleteAccountUseCase: DeleteAccountUseCase = DeleteAccountUseCase(),
    private val profileImageUseCase: ProfileImageUseCase = ProfileImageUseCase()
) : MainViewModel() {


    fun logout(onLogout: () -> Unit) {


        wrapWithLoadingState(
            successState = {
                _state.update { it.copy(screenState = ScreenState.Success) }

                onLogout()

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

                ActiveReservations.setReservations(emptyList())
                ActiveRooms.setRooms(emptyList())
                ActiveUser.clearUser()
            }
        )


    }

    fun deleteUserAccount(onLogout: () -> Unit) {
        wrapWithLoadingState(
            successState = {
                _state.update { it.copy(screenState = ScreenState.Success) }
                onLogout()
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
                deleteAccountUseCase(
                    ActiveUser.getUid() ?: throw Exception("Nie udało się usunąć użytkownika!"),
                    onLogout
                )

                ActiveReservations.setReservations(emptyList())
                ActiveRooms.setRooms(emptyList())
                ActiveUser.clearUser()

            }
        )
    }


    fun uploadProfileImage(imageUri: Uri) {
        wrapWithLoadingState(
            successState = { downloadUrl ->
                _state.update {
                    it.copy(
                        profileImageUrl = downloadUrl, screenState = ScreenState.Success
                    )
                }
            },
            errorState = { message -> handleError(UiError.UnknownError(message)) },
            {
                profileImageUseCase(imageUri)
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
