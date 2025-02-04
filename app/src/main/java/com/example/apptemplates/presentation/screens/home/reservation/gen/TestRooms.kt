package com.example.apptemplates.presentation.screens.home.reservation.gen

import com.example.apptemplates.domain.model.RecurrenceFrequency
import com.example.apptemplates.domain.model.RecurrencePattern
import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.domain.model.ReservationStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset


fun generateTestRecurringReservation(): Reservation {


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
        isRecurring = true,
        recurrencePattern = RecurrencePattern(
            frequency = RecurrenceFrequency.WEEKLY,
            endDate = startDateTime.plusYears(1).toInstant(ZoneOffset.UTC).toEpochMilli()
        )
    )
}
