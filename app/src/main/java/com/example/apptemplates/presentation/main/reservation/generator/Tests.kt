package com.example.apptemplates.presentation.main.reservation.generator

import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Lesson
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


//  Check if recurring reservation overlaps with test reservation
fun isRecurringReservationOverlapping(
    testReservation: Reservation, recurringReservation: Reservation
): Boolean {
    val recurrencePattern = recurringReservation.recurrencePattern ?: return false

    // Convert test and recurring reservation times to LocalDateTime
    val testStartTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(testReservation.startTime), ZoneOffset.UTC)
    val testEndTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(testReservation.endTime), ZoneOffset.UTC)

    val recurringStartTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(recurringReservation.startTime), ZoneOffset.UTC
    )
    val recurringEndTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(recurringReservation.endTime), ZoneOffset.UTC)

    // Check if the dates matches if yes return true (same date)
    if (recurringStartTime == testStartTime) return true


    // Determine the recurrence interval in days
    val recurrenceIntervalDays = when (recurrencePattern.frequency) {
        RecurrenceFrequency.WEEKLY -> 7 // One week in days
        RecurrenceFrequency.BIWEEKLY -> 14 // Two weeks in days
        RecurrenceFrequency.MONTHLY -> 28 // 4 weeks (month) in days
    }

    // Calculate the difference in days between the recurring reservation's first occurrence and the test reservation
    val differenceInDays = Duration.between(
        recurringStartTime.toLocalDate().atStartOfDay(), testStartTime.toLocalDate().atStartOfDay()
    ).toDays()

    // If the difference in days is divisible by the interval, it means the test date falls on a valid recurring cycle
    val isValidCycle = differenceInDays % recurrenceIntervalDays == 0L

    val isTimeOverlapping = recurringStartTime.toLocalTime()
        .isBefore(testEndTime.toLocalTime()) && recurringEndTime.toLocalTime()
        .isAfter(testStartTime.toLocalTime())

    // Finally, check if the times overlap
    return isValidCycle && isTimeOverlapping
}


// Check if two recurring reservations overlap
fun isRecurringReservationsOverlapping(
    testReservation: Reservation, recurringReservation: Reservation
): Boolean {
    val recurrencePattern = recurringReservation.recurrencePattern ?: return false
    val testPattern = testReservation.recurrencePattern ?: return false

    // Convert to LocalDateTime
    val testStartTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(testReservation.startTime), ZoneOffset.UTC)
    val testEndTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(testReservation.endTime), ZoneOffset.UTC)

    val recurringStartTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(recurringReservation.startTime),
        ZoneOffset.UTC
    )
    val recurringEndTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(recurringReservation.endTime), ZoneOffset.UTC)

    // Ensure the days of the week align
    if (testPattern == recurrencePattern) return true

    // Determine recurrence intervals
    val recurrenceIntervalDays = when (recurrencePattern.frequency) {
        RecurrenceFrequency.WEEKLY -> 7
        RecurrenceFrequency.BIWEEKLY -> 14
        RecurrenceFrequency.MONTHLY -> 28

    }

    val testIntervalDays = when (testPattern.frequency) {
        RecurrenceFrequency.WEEKLY -> 7
        RecurrenceFrequency.BIWEEKLY -> 14
        RecurrenceFrequency.MONTHLY -> 28

    }

    // Calculate difference in days and validate cycles
    val daysBetween = Duration.between(
        recurringStartTime.toLocalDate().atStartOfDay(),
        testStartTime.toLocalDate().atStartOfDay()
    ).toDays()
    val isValidCycle =
        daysBetween % recurrenceIntervalDays == 0L || daysBetween % testIntervalDays == 0L

    // Check time overlap on valid cycles
    val isTimeOverlapping = recurringStartTime.toLocalTime().isBefore(testEndTime.toLocalTime()) &&
            recurringEndTime.toLocalTime().isAfter(testStartTime.toLocalTime())

    return isValidCycle && isTimeOverlapping
}


//  Check if lesson overlaps with test reservation
fun isLessonOverlapping(lesson: Lesson, reservation: Reservation): Boolean {

    // Convert test and recurring reservation times to LocalDateTime
    val reservationStartTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(reservation.startTime), ZoneOffset.UTC)
    val reservationEndTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(reservation.endTime), ZoneOffset.UTC)


    val lessonStartTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(lesson.lessonStart), ZoneOffset.UTC)
    val lessonEndTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(lesson.lessonEnd), ZoneOffset.UTC)

    if (reservationStartTime == lessonStartTime) return true

    val lessonFrequency = lesson.frequency

    // Calculate recurrence intervals
    val intervalDays = when (lessonFrequency) {
        RecurrenceFrequency.WEEKLY -> 7
        RecurrenceFrequency.BIWEEKLY -> 14
        RecurrenceFrequency.MONTHLY -> 28  // Approximate monthly as 28 days
    }

    // Calculate the difference in days between lesson and reservation
    val differenceInDays = Duration.between(
        lessonStartTime.toLocalDate().atStartOfDay(),
        reservationStartTime.toLocalDate().atStartOfDay()
    ).toDays()

    // Check if the difference matches the recurrence cycle
    val isValidCycle = differenceInDays % intervalDays == 0L

    // Check for time overlap
    val isTimeOverlapping = lessonStartTime.toLocalTime()
        .isBefore(reservationEndTime.toLocalTime()) && lessonEndTime.toLocalTime()
        .isAfter(reservationStartTime.toLocalTime())

    // If both valid recurrence and time overlap, room is not available
//    if (isValidCycle && isTimeOverlapping) return true

    // If no overlap found, room is available
    return (isValidCycle && isTimeOverlapping)
}


fun Long.toLocalDateTime(): String {

    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC).format(
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    )

}