package com.example.apptemplates.presentation.screens.home.reservation.gen

import com.example.apptemplates.domain.model.Lesson
import com.example.apptemplates.domain.model.RecurrenceFrequency
import com.example.apptemplates.domain.model.RecurrencePattern
import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.domain.model.ReservationStatus
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.random.Random


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


            var startDateTime = LocalDateTime.now()
                .with(DayOfWeek.of((1..7).random()))
                .withHour((8..18).random())
                .withMinute(listOf(0, 15, 30, 45).random())

            val durationHours = (1..3).random()
            val endDateTime = startDateTime.plusHours(durationHours.toLong())


            val roomId = randomRoomIds.random()
            val isOverlapping = lessons.filter { it.roomId == roomId }.any { lesson ->
                isLessonOverlappingWithReservation(lesson, startDateTime, endDateTime)
            }

            if (!isOverlapping) {

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
                        createdAt = generateRandomDateWithinSixMonths(),
                        startTime = startDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        endTime = endDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        dayOfWeek = startDateTime.dayOfWeek,
                        participants = (1..100).random(),
                        status = ReservationStatus.entries.random(),
                        isRecurring = isRecurring,
                        recurrencePattern = if (isRecurring) recurrencePattern else null
                    )
                )
                reservationAdded = true
            } else {

                startDateTime = startDateTime.plusDays(1)
            }
        }
    }

    return reservations
}


fun generateRecurrencePattern(startTime: LocalDateTime): RecurrencePattern {
    val recurrenceFrequency = RecurrenceFrequency.entries.random()

    val endRecurrenceDate = when (recurrenceFrequency) {
        RecurrenceFrequency.WEEKLY -> startTime.toLocalDate()
            .plusWeeks((1..24).random().toLong())
            .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

        RecurrenceFrequency.BIWEEKLY -> startTime.toLocalDate()
            .plusWeeks(2 * (2..12).random().toLong())
            .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

        RecurrenceFrequency.MONTHLY -> startTime.toLocalDate()
            .plusWeeks(4 * (1..6).random().toLong())
            .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }


    return RecurrencePattern(
        frequency = recurrenceFrequency,
        endDate = endRecurrenceDate
    )
}



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


fun generateRandomDateWithinSixMonths(): Long {
    val now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    val sixMonthsAgo = LocalDateTime.now().minusMonths(12).toInstant(ZoneOffset.UTC).toEpochMilli()


    return Random.nextLong(sixMonthsAgo, now)
}


