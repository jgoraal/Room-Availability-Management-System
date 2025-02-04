package com.example.apptemplates.domain.repository

import com.example.apptemplates.domain.model.Lesson
import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.data.firebase.database.result.Result
import com.google.firebase.firestore.FirebaseFirestore

interface LessonsRepository {
    val database: FirebaseFirestore

    suspend fun fetchLessons(): Result<List<Lesson>>

    suspend fun getOverlappingLessonsRoomsId(
        roomIds: List<String>,
        newReservation: Reservation,
    ): Result<Set<String>>


}