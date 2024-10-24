package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Lesson
import com.example.apptemplates.presentation.main.reservation.generator.isLessonOverlapping
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object LessonsRepositoryImpl : LessonsRepository {

    private const val UNKNOWN_ERROR = "Unknown error"

    private const val LESSONS_COLLECTION = "lessons"


    override val database: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    override suspend fun fetchLessons(): Result<List<Lesson>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOverlappingLessonsRoomsId(
        roomIds: List<String>,
        newReservation: Reservation,
    ): Result<Set<String>> {
        return try {
            val snapshot = database.collection(LESSONS_COLLECTION)

            val query = if (roomIds.isNotEmpty()) {
                snapshot
                    .whereEqualTo("day", newReservation.dayOfWeek) // Match by day of the week
                    .whereGreaterThanOrEqualTo(
                        "lessonEndDate",
                        newReservation.startTime
                    ) // Lessons still active
                    .whereNotIn("roomId", roomIds) // Filter by specific rooms
            } else {
                snapshot
                    .whereEqualTo("day", newReservation.dayOfWeek) // Match by day of the week
                    .whereGreaterThanOrEqualTo(
                        "lessonEndDate",
                        newReservation.startTime
                    ) // Lessons still active
            }

            // Get the Firestore snapshot data
            val lessonsSnapshot = query.get().await()

            // Map the snapshot to Lesson objects
            val overlappingLessons = lessonsSnapshot.toObjects(Lesson::class.java)
                .filter { lesson ->
                    lesson.roomId !in roomIds
                    isLessonOverlapping(lesson, newReservation) &&
                            lesson.roomId !in roomIds
                }
                .distinctBy { lesson -> lesson.roomId } // Ensure room uniqueness
                .map { lesson -> lesson.roomId } // Collect room IDs
                .toSet() // Convert to a set to ensure uniqueness

            // Return successful result with the overlapping room IDs
            Result.SuccessWithResult(overlappingLessons)

        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }


    suspend fun addLesson(lesson: Lesson): Result<Nothing> {
        return try {
            ReservationsRepositoryImpl.database.collection(LESSONS_COLLECTION).document(lesson.id)
                .set(lesson).await()
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }
}