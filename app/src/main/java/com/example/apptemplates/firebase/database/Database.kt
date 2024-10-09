package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


object Database : FirestoreDatabase {

    override val database: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    private val usersCollection = database.collection("users")


    override suspend fun addUser(user: User): FirestoreResult {
        return try {
            val newUserDoc =
                usersCollection.document(user.uid)
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

                if (user != null) {
                    FirestoreResult.SuccessWithResult(user)
                } else {
                    FirestoreResult.Failure(Exception("Failed to parse user data"))
                }

            } else {
                FirestoreResult.Failure(Exception("User with $email not found"))
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
                FirestoreResult.Failure(Exception("User with $username not found"))
            }
        } catch (e: Exception) {
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

    override suspend fun deleteUser(email: String): FirestoreResult {
        return try {
            // Znajdź dokument użytkownika za pomocą zapytania na podstawie pola "email"
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
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


}
