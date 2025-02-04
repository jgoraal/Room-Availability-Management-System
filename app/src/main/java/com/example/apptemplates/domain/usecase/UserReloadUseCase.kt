package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.auth.FirebaseAuthManager
import com.example.apptemplates.data.firebase.auth.result.AuthResult
import com.example.apptemplates.data.firebase.database.FirestoreRepository
import com.example.apptemplates.data.firebase.database.result.FirestoreResult

class UserReloadUseCase {

    private val authRepository = FirebaseAuthManager
    private val firestoreRepository = FirestoreRepository
    private val errorMessage = "Nie znaleziono użytkownika!"


    suspend operator fun invoke() {

        if (ActiveUser.isUserVerified()) return

        return when (authRepository.reloadFirebaseUser()) {
            is AuthResult.Success -> updateUser()
            is AuthResult.Failure -> throw Exception(errorMessage)
        }

    }


    private suspend fun updateUser() {

        if (authRepository.currentUser?.isEmailVerified == false) return

        return when (firestoreRepository.updateVerifiedStatus()) {
            is FirestoreResult.Success -> ActiveUser.updateIsVerified(true)
            is FirestoreResult.Failure -> throw Exception(errorMessage)
            else -> throw Exception(errorMessage)
        }
    }
}