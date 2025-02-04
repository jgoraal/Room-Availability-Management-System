package com.example.apptemplates

import com.example.apptemplates.domain.model.RecurrenceFrequency
import com.example.apptemplates.domain.model.RecurrencePattern
import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.presentation.screens.home.reservation.gen.isRecurringReservationOverlapping
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class ReservationTest {


    private fun createReservation(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        isRecurring: Boolean,
        frequency: RecurrenceFrequency,
        endRecurrence: LocalDateTime
    ): Reservation {
        val recurrencePattern = if (isRecurring) {
            RecurrencePattern(
                frequency,
                endRecurrence.toEpochSecond(ZoneOffset.UTC) * 1000
            )
        } else null

        return Reservation(
            roomId = "Room001",
            userId = "User001",
            startTime = startTime.toEpochSecond(ZoneOffset.UTC) * 1000,
            endTime = endTime.toEpochSecond(ZoneOffset.UTC) * 1000,
            dayOfWeek = startTime.dayOfWeek,
            isRecurring = isRecurring,
            recurrencePattern = recurrencePattern
        )
    }

    @Test
    fun `test no overlap non-recurring reservation`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = false,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 9, 10, 0),
            endTime = LocalDateTime.of(2024, 10, 9, 12, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertFalse("Expected no overlap", result)
    }

    @Test
    fun `test exact overlap recurring reservation`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertTrue("Expected exact overlap", result)
    }

    @Test
    fun `test partial overlap recurring reservation`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 19, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 21, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertTrue("Expected partial overlap", result)
    }

    @Test
    fun `test no overlap with different day recurring reservation`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 11, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 11, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertFalse("Expected no overlap with different day", result)
    }

    @Test
    fun `test overlap biweekly recurring reservation`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 24, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 24, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.BIWEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.BIWEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertTrue("Expected overlap for biweekly event", result)
    }

    @Test
    fun `test no overlap after recurrence ends`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2025, 1, 1, 18, 0),
            endTime = LocalDateTime.of(2025, 1, 1, 20, 0),
            isRecurring = false,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2024, 12, 31, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2024, 12, 31, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertFalse("Expected no overlap after recurrence ends", result)
    }


    @Test
    fun `test start time overlap`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 17, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 19, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertTrue("Expected start time overlap", result)
    }

    @Test
    fun `test end time overlap`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(
                2024,
                10,
                10,
                19,
                0
            ),
            endTime = LocalDateTime.of(2024, 10, 10, 21, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertTrue("Expected end time overlap", result)
    }

    @Test
    fun `test monthly recurring reservation overlap`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 11, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 11, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.MONTHLY,
            endRecurrence = LocalDateTime.of(2025, 6, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.MONTHLY,
            endRecurrence = LocalDateTime.of(2025, 6, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertFalse("Expected monthly recurrence overlap", result)
    }

    @Test
    fun `test reservation spanning multiple times`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 16, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 22, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 20, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertTrue("Expected overlapping reservation within test reservation", result)
    }

    @Test
    fun `test edge case same start and end time`() {
        val testReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val recurringReservation = createReservation(
            startTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            endTime = LocalDateTime.of(2024, 10, 10, 18, 0),
            isRecurring = true,
            frequency = RecurrenceFrequency.WEEKLY,
            endRecurrence = LocalDateTime.of(2025, 1, 1, 0, 0)
        )

        val result = isRecurringReservationOverlapping(testReservation, recurringReservation)
        assertTrue("Expected overlap for exact same start and end times", result)
    }


}