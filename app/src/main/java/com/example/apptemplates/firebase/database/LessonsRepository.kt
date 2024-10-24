package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Lesson
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.FirebaseFirestore

interface LessonsRepository {
    val database: FirebaseFirestore

    suspend fun fetchLessons(): Result<List<Lesson>>

    suspend fun getOverlappingLessonsRoomsId(
        roomIds: List<String>,
        newReservation: Reservation,
    ): Result<Set<String>>


}