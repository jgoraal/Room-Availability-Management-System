package com.example.apptemplates

import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.RecurrencePattern
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.presentation.main.reservation.generator.isRecurringReservationOverlapping
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class RecurringReservationTest {

    // Helper function to create a reservation
    private fun createReservation(
        roomId: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        isRecurring: Boolean,
        recurrencePattern: RecurrencePattern? = null
    ): Reservation {
        return Reservation(
            roomId = roomId,
            userId = "User01",
            startTime = startTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
            endTime = endTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
            dayOfWeek = startTime.dayOfWeek,
            isRecurring = isRecurring,
            recurrencePattern = recurrencePattern
        )
    }

    // Test: No overlap between a non-recurring and a recurring reservation
    @Test
    fun `test no overlap between non-recurring and recurring reservation`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 9, 0),
            LocalDateTime.of(2024, 10, 10, 10, 0),
            false
        )

        val recurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 9, 11, 0),
            LocalDateTime.of(2024, 10, 9, 12, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertFalse(isRecurringReservationOverlapping(testReservation, recurringReservation))
    }

    // Test: Exact overlap between two recurring reservations
    @Test
    fun `test exact overlap between two recurring reservations`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 10, 0),
            LocalDateTime.of(2024, 10, 10, 11, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val recurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 10, 0),
            LocalDateTime.of(2024, 10, 10, 11, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertTrue(isRecurringReservationOverlapping(testReservation, recurringReservation))
    }

    // Test: Partial overlap between two recurring reservations
    @Test
    fun `test partial overlap between two recurring reservations`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 10, 0),
            LocalDateTime.of(2024, 10, 10, 11, 30),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val recurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 11, 0),
            LocalDateTime.of(2024, 10, 10, 12, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertTrue(isRecurringReservationOverlapping(testReservation, recurringReservation))
    }

    // Test: No overlap when reservations are on different days of the week
    @Test
    fun `test no overlap between reservations on different days`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 12, 9, 0), // Saturday
            LocalDateTime.of(2024, 10, 12, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val recurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 9, 9, 0), // Wednesday
            LocalDateTime.of(2024, 10, 9, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertFalse(isRecurringReservationOverlapping(testReservation, recurringReservation))
    }

    // Test: Overlap when two reservations span the same period on the same day
    @Test
    fun `test overlap when two reservations span same time on the same day`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 9, 0),
            LocalDateTime.of(2024, 10, 10, 10, 30),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val recurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 9, 30),
            LocalDateTime.of(2024, 10, 10, 11, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertTrue(isRecurringReservationOverlapping(testReservation, recurringReservation))
    }

    // Test: No overlap when one reservation ends before another starts
    @Test
    fun `test no overlap when one reservation ends before another starts`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 8, 0),
            LocalDateTime.of(2024, 10, 10, 9, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val recurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 10, 0),
            LocalDateTime.of(2024, 10, 10, 11, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertFalse(isRecurringReservationOverlapping(testReservation, recurringReservation))
    }
}
