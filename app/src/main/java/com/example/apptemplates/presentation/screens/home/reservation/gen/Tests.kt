package com.example.apptemplates.presentation.screens.home.reservation.gen


import com.example.apptemplates.domain.model.Lesson
import com.example.apptemplates.domain.model.RecurrenceFrequency
import com.example.apptemplates.domain.model.Reservation
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun isRecurringReservationOverlapping(
    new: Reservation, recurring: Reservation
): Boolean {
    val recurrencePattern = recurring.recurrencePattern ?: return false

    // Zamiana czasów z milisekund na LocalDateTime
    val (newStart, newEnd, recStart, recEnd) = listOf(
        new.startTime, new.endTime,
        recurring.startTime, recurring.endTime
    ).map { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC) }

    // Kontrola daty startu (true = takie same daty)
    if (newStart == recStart) return true

    // Określenie odstępu dni w zależności od częstotliwości
    val recurrenceIntervalDays = when (recurrencePattern.frequency) {
        RecurrenceFrequency.WEEKLY -> 7L // Tydzień w dniach
        RecurrenceFrequency.BIWEEKLY -> 14L // Dwa tygodnie w dniach
        RecurrenceFrequency.MONTHLY -> 28L // Miesiąc (4 tygodnie) w dniach
    }

    // Sprawdzenie, czy nowa rezerwacja wypada w cyklu rezerwacji cyklicznej
    val differenceInDays = Duration.between(
        recStart.toLocalDate().atStartOfDay(),
        newStart.toLocalDate().atStartOfDay()
    ).toDays()

    val validCycle = differenceInDays % recurrenceIntervalDays == 0L

    // Sprawdzenie konflitku godzinowego
    val overlap = recStart.toLocalTime().isBefore(newEnd.toLocalTime()) &&
            recEnd.toLocalTime().isAfter(newStart.toLocalTime())

    return validCycle && overlap
}


fun isRecurringReservationsOverlapping(
    testReservation: Reservation, recurringReservation: Reservation
): Boolean {
    val recurrencePattern = recurringReservation.recurrencePattern ?: return false
    val testPattern = testReservation.recurrencePattern ?: return false


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

    if (testPattern == recurrencePattern) return true

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

    val daysBetween = Duration.between(
        recurringStartTime.toLocalDate().atStartOfDay(),
        testStartTime.toLocalDate().atStartOfDay()
    ).toDays()
    val isValidCycle =
        daysBetween % recurrenceIntervalDays == 0L || daysBetween % testIntervalDays == 0L

    val isTimeOverlapping = recurringStartTime.toLocalTime().isBefore(testEndTime.toLocalTime()) &&
            recurringEndTime.toLocalTime().isAfter(testStartTime.toLocalTime())

    return isValidCycle && isTimeOverlapping
}


fun isLessonOverlapping(lesson: Lesson, reservation: Reservation): Boolean {


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


    val intervalDays = when (lessonFrequency) {
        RecurrenceFrequency.WEEKLY -> 7
        RecurrenceFrequency.BIWEEKLY -> 14
        RecurrenceFrequency.MONTHLY -> 28
    }


    val differenceInDays = Duration.between(
        lessonStartTime.toLocalDate().atStartOfDay(),
        reservationStartTime.toLocalDate().atStartOfDay()
    ).toDays()


    val isValidCycle = differenceInDays % intervalDays == 0L


    val isTimeOverlapping = lessonStartTime.toLocalTime()
        .isBefore(reservationEndTime.toLocalTime()) && lessonEndTime.toLocalTime()
        .isAfter(reservationStartTime.toLocalTime())

    return (isValidCycle && isTimeOverlapping)
}


fun Long.toLocalDateTime(): String {

    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC).format(
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    )

}