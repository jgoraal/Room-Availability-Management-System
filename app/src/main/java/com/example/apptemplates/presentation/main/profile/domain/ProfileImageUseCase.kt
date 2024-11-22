package com.example.apptemplates.presentation.main.profile.domain

import android.net.Uri
import android.util.Log
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.firebase.database.FirestoreRepository
import com.example.apptemplates.firebase.database.result.FirestoreResult
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProfileImageUseCase {

    private val storage = StorageRepository.storage
    private val repository = FirestoreRepository
    private val errorMessage = "Nie udało się zaktualizować zdjęcia profilowego!"


    suspend operator fun invoke(imageUri: Uri): String {
        val userId = ActiveUser.getUid() ?: throw Exception(errorMessage)
        val storageRef = storage.reference.child("profile_images/$userId.jpg")

        try {
            // Prześlij obraz do Firebase Storage
            storageRef.putFile(imageUri).await()

            // Pobierz URL do pobierania
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Zaktualizuj URL w Firestore
            updateProfileImage(downloadUrl)

            return downloadUrl

        } catch (e: Exception) {
            Log.e("ProfileImageUseCase", "Error uploading profile image", e)
            throw Exception(errorMessage)
        }
    }


    private suspend inline fun updateProfileImage(downloadUrl: String) {
        when (repository.updateProfileImage(downloadUrl)) {
            is FirestoreResult.Success -> ActiveUser.updateProfileImageUrl(downloadUrl)
            is FirestoreResult.Failure -> throw Exception(errorMessage)
            else -> throw Exception(errorMessage)
        }
    }

}

object StorageRepository {
    val storage: FirebaseStorage
        get() = FirebaseStorage.getInstance()
}