package com.example.apptemplates.presentation.main.profile

import android.net.Uri
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.home.ActiveReservations
import com.example.apptemplates.presentation.main.home.ActiveRooms
import com.example.apptemplates.presentation.main.profile.domain.DeleteAccountUseCase
import com.example.apptemplates.presentation.main.profile.domain.ProfileImageUseCase
import com.example.apptemplates.presentation.main.profile.domain.SignOutUseCase
import com.example.apptemplates.viewmodel.MainViewModel
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
