package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.user.User
import com.example.apptemplates.firebase.database.result.FirestoreResult
import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreService {
    val database: FirebaseFirestore

    suspend fun addUser(user: User): FirestoreResult

    suspend fun getUser(uid: String): FirestoreResult

    suspend fun updateUser(user: User): FirestoreResult

    suspend fun deleteUser(email: String): FirestoreResult

    suspend fun getUserByEmail(email: String): FirestoreResult

    suspend fun getUserByUsername(username: String): FirestoreResult

    suspend fun getUserByUid(uid: String): FirestoreResult

}