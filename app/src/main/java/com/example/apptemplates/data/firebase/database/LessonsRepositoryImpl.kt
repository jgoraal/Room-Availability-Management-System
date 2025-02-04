package com.example.apptemplates.data.firebase.database


import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Lesson
import com.example.apptemplates.domain.model.RecurrenceFrequency
import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.domain.repository.LessonsRepository
import com.example.apptemplates.presentation.screens.home.reservation.gen.isLessonOverlapping
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
                .whereEqualTo("day", newReservation.dayOfWeek)
                .whereGreaterThanOrEqualTo(
                    "lessonEndDate",
                    newReservation.startTime
                )


            val lessonsSnapshot = query.get().await()


            val overlappingLessons = lessonsSnapshot.toObjects(Lesson::class.java)
                .filter { lesson ->
                    lesson.roomId !in roomIds
                    isLessonOverlapping(lesson, newReservation) &&
                            lesson.roomId !in roomIds
                }
                .distinctBy { lesson -> lesson.roomId }
                .map { lesson -> lesson.roomId }
                .toSet()


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



    val recurrenceIntervalDays = when (lessonRecurrenceFrequency) {
        RecurrenceFrequency.WEEKLY -> 7
        RecurrenceFrequency.BIWEEKLY -> 14
        RecurrenceFrequency.MONTHLY -> 28
    }


    val differenceInDays = Duration.between(
        lessonStartTime.toLocalDate().atStartOfDay(), roomStartTime.toLocalDate().atStartOfDay()
    ).toDays()


    return differenceInDays % recurrenceIntervalDays == 0L

}