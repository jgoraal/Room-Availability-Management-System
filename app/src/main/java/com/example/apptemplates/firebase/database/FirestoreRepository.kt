package com.example.apptemplates.firebase.database

import android.util.Log
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.data.user.User
import com.example.apptemplates.firebase.database.result.FirestoreResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


object FirestoreRepository : FirestoreService {

    override val database: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    private val usersCollection = database.collection("users")


    override suspend fun addUser(user: User): FirestoreResult {
        return try {
            val newUserDoc = usersCollection.document(user.uid)
            newUserDoc.set(user).await()
            FirestoreResult.Success
        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }
    }


    override suspend fun getUser(uid: String): FirestoreResult {
        return try {
            val userDocumentSnapshot = usersCollection.document(uid).get().await()
            if (userDocumentSnapshot.exists()) {
                val user = userDocumentSnapshot.toObject(User::class.java)
                FirestoreResult.SuccessWithResult(user)
            } else {
                FirestoreResult.Failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }
    }


    override suspend fun getUserByEmail(email: String): FirestoreResult {
        return try {
            // Znajdź dokument użytkownika za pomocą zapytania na podstawie pola "email"
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
            if (!querySnapshot.isEmpty) {
                val user = querySnapshot.documents.first().toObject(User::class.java)

                user.let {
                    FirestoreResult.SuccessWithResult(it)
                }

            } else {
                FirestoreResult.Failure(Exception("Email not found"))
            }
        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }
    }

    override suspend fun getUserByUsername(username: String): FirestoreResult {
        return try {
            // Znajdź dokument użytkownika za pomocą zapytania na podstawie pola "email"
            val querySnapshot = usersCollection.whereEqualTo("username", username).get().await()
            if (!querySnapshot.isEmpty) {
                FirestoreResult.SuccessWithResult(true)
            } else {
                FirestoreResult.SuccessWithResult(false)
            }
        } catch (e: Exception) {
            Log.i("FirestoreRepository Exception", e.toString())
            FirestoreResult.Failure(e)
        }
    }

    override suspend fun getUserByUid(uid: String): FirestoreResult {
        return try {
            // Znajdź dokument użytkownika za pomocą zapytania na podstawie pola "email"
            val querySnapshot = usersCollection.whereEqualTo("uid", uid).get().await()
            if (!querySnapshot.isEmpty) {
                FirestoreResult.SuccessWithResult(true)
            } else {
                FirestoreResult.Failure(Exception("User with $uid not found"))
            }
        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }
    }


    override suspend fun updateUser(user: User): FirestoreResult {
        return try {
            usersCollection.document(user.uid).set(user).await()
            FirestoreResult.Success
        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }
    }

    suspend fun updateVerifiedStatus(): FirestoreResult {
        return try {

            usersCollection.document(
                ActiveUser.getUid() ?: throw Exception("Nie ma takiego użytkownika!")
            ).update("verified", true).await()

            FirestoreResult.Success

        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }

    }


    override suspend fun deleteUser(userId: String): FirestoreResult {
        return try {
            // Znajdź dokument użytkownika za pomocą zapytania na podstawie pola "email"
            val querySnapshot = usersCollection.whereEqualTo("uid", userId).get().await()
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    document.reference.delete().await()
                }
                FirestoreResult.Success
            } else {
                FirestoreResult.Failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }
    }


    suspend fun updateProfileImage(downloadUrl: String): FirestoreResult {
        return try {

            usersCollection.document(
                ActiveUser.getUid() ?: throw Exception("Nie ma takiego użytkownika!")
            ).update("profileImageUrl", downloadUrl).await()

            FirestoreResult.Success

        } catch (e: Exception) {
            FirestoreResult.Failure(e)
        }

    }


}
