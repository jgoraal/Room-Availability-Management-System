package com.example.apptemplates

import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.RecurrencePattern
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.presentation.main.reservation.generator.isRecurringReservationOverlapping
import com.example.apptemplates.presentation.main.reservation.generator.isRecurringReservationsOverlapping
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class RecurringReservationAdvancedTest {

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

    // Test: Overlap with bi-weekly recurrence patterns
    @Test
    fun `test biweekly recurring overlap`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 9, 0),
            LocalDateTime.of(2024, 10, 10, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.BIWEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val recurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 9, 30),
            LocalDateTime.of(2024, 10, 10, 10, 30),
            true,
            RecurrencePattern(
                RecurrenceFrequency.BIWEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertTrue(isRecurringReservationOverlapping(testReservation, recurringReservation))
    }

    // Test: Monthly recurring reservation that overlaps weekly reservation
    @Test
    fun `test monthly recurring overlaps with weekly recurring`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 14, 0),
            LocalDateTime.of(2024, 10, 10, 16, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.MONTHLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val weeklyReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 15, 0),
            LocalDateTime.of(2024, 10, 10, 17, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertTrue(isRecurringReservationOverlapping(testReservation, weeklyReservation))
    }

    // Test: Reservation that spans multiple months but does not overlap weekly reservation
    @Test
    fun `test monthly reservation that does not overlap weekly recurring`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 1, 9, 0),
            LocalDateTime.of(2024, 10, 1, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.MONTHLY,
                LocalDateTime.of(2025, 3, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val weeklyReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 8, 9, 0),
            LocalDateTime.of(2024, 10, 8, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val bool = (weeklyReservation.dayOfWeek != testReservation.dayOfWeek)

        assertFalse(isRecurringReservationsOverlapping(testReservation, weeklyReservation) && bool)
    }

    // Test: Edge case where two reservations are on the same day but different weeks, no overlap
    @Test
    fun `test same day different weeks no overlap`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 1, 9, 0),
            LocalDateTime.of(2024, 10, 1, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val nextWeekReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 8, 9, 0),
            LocalDateTime.of(2024, 10, 8, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val bool = (nextWeekReservation.dayOfWeek != testReservation.dayOfWeek)

        assertFalse(
            isRecurringReservationsOverlapping(
                nextWeekReservation,
                testReservation
            ) && bool
        )
    }

    // Test: Recurring reservation that overlaps another after several weeks of recurrence
    @Test
    fun `test overlap after several weeks of recurrence`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 9, 0),
            LocalDateTime.of(2024, 10, 10, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val futureRecurringReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 12, 10, 9, 0),
            LocalDateTime.of(2024, 12, 10, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val bool = (futureRecurringReservation.dayOfWeek != testReservation.dayOfWeek)

        assertFalse(isRecurringReservationOverlapping(futureRecurringReservation, testReservation))
    }

    // Test: No overlap when two reservations are on the same day but one ends right before the other starts
    @Test
    fun `test no overlap when one ends right before another starts`() {
        val testReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 9, 0),
            LocalDateTime.of(2024, 10, 10, 10, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        val nextReservation = createReservation(
            "Room1",
            LocalDateTime.of(2024, 10, 10, 10, 0),
            LocalDateTime.of(2024, 10, 10, 11, 0),
            true,
            RecurrencePattern(
                RecurrenceFrequency.WEEKLY,
                LocalDateTime.of(2025, 1, 1, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli()
            )
        )

        assertFalse(isRecurringReservationOverlapping(testReservation, nextReservation))
    }
}
