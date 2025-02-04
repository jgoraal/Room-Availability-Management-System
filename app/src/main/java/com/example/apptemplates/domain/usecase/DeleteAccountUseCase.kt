package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.data.firebase.auth.result.AuthResult
import com.example.apptemplates.data.firebase.database.FirestoreRepository
import com.example.apptemplates.data.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.FirestoreResult
import com.example.apptemplates.data.firebase.database.result.Result

class DeleteAccountUseCase {

    private val errorMessage = "Nie udało się usunąć użytkownika!"

    suspend operator fun invoke(userId: String, onLogout: () -> Unit) {

        return when (val result = ReservationsRepositoryImpl.deleteUserReservations(userId)) {
            is Result.SuccessWithResult -> deleteUserFromDataBase(userId, onLogout)
            is Result.Error -> throw Exception(result.error)
            else -> throw Exception(errorMessage)

        }
    }

    private suspend fun deleteUserFromDataBase(userId: String, onLogout: () -> Unit) {
        return when (FirestoreRepository.deleteUser(userId)) {
            is FirestoreResult.Success -> revokeUser(onLogout)
            is FirestoreResult.Failure -> throw Exception(errorMessage)
            else -> throw Exception(errorMessage)
        }
    }


    private suspend fun revokeUser(onLogout: () -> Unit) {
        when (FirebaseAuthManager.revokeAccess()) {
            is AuthResult.Success -> onLogout()
            is AuthResult.Failure -> throw Exception(errorMessage)
        }
    }


}