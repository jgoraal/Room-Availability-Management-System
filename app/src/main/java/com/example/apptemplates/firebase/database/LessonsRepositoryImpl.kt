package com.example.apptemplates.firebase.database

import android.util.Log
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Lesson
import com.example.apptemplates.presentation.main.reservation.generator.isLessonOverlapping
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

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

            val query = snapshot
                .whereEqualTo("day", newReservation.dayOfWeek) // Match by day of the week
                .whereGreaterThanOrEqualTo(
                    "lessonEndDate",
                    newReservation.startTime
                ) // Lessons still active


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


    suspend fun getLessonsByRoomId(
        roomId: String,
        dayOfWeek: DayOfWeek,
        time: Long
    ): Result<List<Lesson>> {
        return try {

            val query = database.collection(LESSONS_COLLECTION)
                .whereEqualTo("day", dayOfWeek)
                .whereEqualTo("roomId", roomId)
                .whereGreaterThanOrEqualTo("lessonEndDate", time)

            val lessonsSnapshot = query.get().await()

            val lessons = lessonsSnapshot.toObjects(Lesson::class.java)
                .filter { lesson ->
                    isLessonOverlapping(lesson, time)
                }

            Log.e("LESSONS", roomId)
            Log.e("LESSONS", lessons.toString())

            Result.SuccessWithResult(lessons)
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }
}


private fun isLessonOverlapping(lesson: Lesson, time: Long): Boolean {

    val lessonRecurrenceFrequency = lesson.frequency

    val lessonStartTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(lesson.lessonStart),
        ZoneOffset.UTC
    )

    val roomStartTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(time),
        ZoneOffset.UTC
    )


    // Determine the recurrence interval in days
    val recurrenceIntervalDays = when (lessonRecurrenceFrequency) {
        RecurrenceFrequency.WEEKLY -> 7 // One week in days
        RecurrenceFrequency.BIWEEKLY -> 14 // Two weeks in days
        RecurrenceFrequency.MONTHLY -> 28 // 4 weeks (month) in days
    }

    // Calculate the difference in days between the recurring reservation's first occurrence and the test reservation
    val differenceInDays = Duration.between(
        lessonStartTime.toLocalDate().atStartOfDay(), roomStartTime.toLocalDate().atStartOfDay()
    ).toDays()


    return differenceInDays % recurrenceIntervalDays == 0L

}