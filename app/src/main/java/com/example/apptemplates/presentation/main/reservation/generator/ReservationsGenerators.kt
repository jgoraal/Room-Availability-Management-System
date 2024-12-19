package com.example.apptemplates.presentation.main.reservation.generator

import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.RecurrencePattern
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.data.room.Lesson
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

//  Main Reservation to test overlapping
fun generateTestReservation(): Reservation {


    val today = LocalDate.now()

    val startTime = LocalTime.of(14, 0)
    val endTime = LocalTime.of(15, 30)


    val startDateTime = LocalDateTime.of(today, startTime)
    val endDateTime = LocalDateTime.of(today, endTime)



    return Reservation(
        roomId = "TestRoom01",
        userId = "TestUser01",
        createdAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
        startTime = startDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
        endTime = endDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
        dayOfWeek = startDateTime.dayOfWeek,
        participants = 10,
        status = ReservationStatus.CONFIRMED,
        isRecurring = false,
        recurrencePattern = RecurrencePattern(
            frequency = RecurrenceFrequency.WEEKLY,
            endDate = startDateTime.toEpochSecond(ZoneOffset.UTC)
        )
    )
}

fun generateRandomReservations(
    randomRoomIds: List<String>,
    userIds: List<String>,
    lessons: List<Lesson>
): List<Reservation> {
    val reservations = mutableListOf<Reservation>()

    for (i in 1..1_000) {
        var reservationAdded = false
        var retryCount = 0
        val maxRetries = 5

        while (!reservationAdded && retryCount < maxRetries) {
            retryCount++

            // Generate random start date (Monday to Sunday) and time
            var startDateTime = LocalDateTime.now()
                .with(DayOfWeek.of((1..7).random()))
                .withHour((8..18).random())  // Random start hour between 8 AM and 6 PM
                .withMinute(listOf(0, 15, 30, 45).random())  // Random 15-minute intervals

            val durationHours = (1..3).random()  // Duration between 1 to 3 hours
            val endDateTime = startDateTime.plusHours(durationHours.toLong())

            // Check if the reservation conflicts with any lessons in the same room
            val roomId = randomRoomIds.random()
            val isOverlapping = lessons.filter { it.roomId == roomId }.any { lesson ->
                isLessonOverlappingWithReservation(lesson, startDateTime, endDateTime)
            }

            if (!isOverlapping) {
                // If no conflict, add reservation
                val isRecurring = listOf(true, false).random()
                val recurrencePattern = if (isRecurring) {
                    generateRecurrencePattern(startDateTime)
                } else {
                    null
                }

                reservations.add(
                    Reservation(
                        roomId = roomId,
                        userId = userIds.shuffled().random(),
                        createdAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
                        startTime = startDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        endTime = endDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        dayOfWeek = startDateTime.dayOfWeek,
                        participants = (1..100).random(),
                        status = ReservationStatus.CONFIRMED,
                        isRecurring = isRecurring,
                        recurrencePattern = recurrencePattern
                    )
                )
                reservationAdded = true
            } else {
                // Retry with a new date and time
                startDateTime = startDateTime.plusDays(1)
            }
        }
    }

    return reservations
}


fun generateRecurrencePattern(startTime: LocalDateTime): RecurrencePattern {
    val recurrenceFrequency = RecurrenceFrequency.entries.random()

    // Get the day of the week based on the startTime (to match the reservation's start time)

    // Generate a random end date for when the recurring reservation stops, based on the frequency
    val endRecurrenceDate = when (recurrenceFrequency) {
        RecurrenceFrequency.WEEKLY -> startTime.toLocalDate()
            .plusWeeks((1..24).random().toLong())  // Recurs for up to 24 weeks
            .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

        RecurrenceFrequency.BIWEEKLY -> startTime.toLocalDate()
            .plusWeeks(2 * (2..12).random().toLong())  // Recurs for up to 24 weeks, every 2 weeks
            .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

        RecurrenceFrequency.MONTHLY -> startTime.toLocalDate()
            .plusWeeks(4 * (1..6).random().toLong())  // Recurs for up to 6 months
            .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }


    return RecurrencePattern(
        frequency = recurrenceFrequency,  // Match dayOfWeek to the startTime
        endDate = endRecurrenceDate  // Store the end of the recurrence as epoch millis
    )
}


// Helper function to check if a lesson overlaps with a reservation
fun isLessonOverlappingWithReservation(
    lesson: Lesson,
    reservationStart: LocalDateTime,
    reservationEnd: LocalDateTime
): Boolean {
    val lessonStart =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(lesson.lessonStart), ZoneOffset.UTC)
    val lessonEnd = LocalDateTime.ofInstant(Instant.ofEpochMilli(lesson.lessonEnd), ZoneOffset.UTC)

    return lessonStart.isBefore(reservationEnd) && lessonEnd.isAfter(reservationStart)
}


