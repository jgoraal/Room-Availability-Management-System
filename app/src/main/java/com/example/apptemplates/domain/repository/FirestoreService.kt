package com.example.apptemplates.domain.repository

import com.example.apptemplates.data.firebase.database.result.FirestoreResult
import com.example.apptemplates.data.model.model.user.User
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