package com.example.apptemplates.domain.model

import java.time.DayOfWeek
import java.time.Instant
import java.util.UUID

data class Reservation(
    val id: String = UUID.randomUUID().toString(),
    val roomId: String = "",
    val userId: String = "",

    val createdAt: Long = Instant.now().epochSecond,
    val startTime: Long = Instant.now().epochSecond,
    val endTime: Long = Instant.now()
        .plusSeconds(7200).epochSecond,
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,

    val participants: Int? = null,
    val status: ReservationStatus = ReservationStatus.PENDING,
    val additionalEquipment: List<Equipment> = emptyList(),

    val isRecurring: Boolean = false,
    val recurrencePattern: RecurrencePattern? = null
)


// Statusy rezerwacji
enum class ReservationStatus {
    PENDING,    // Oczekująca
    CONFIRMED,  // Potwierdzona
    CANCELED  // Anulowana
}


// Klasa do zarządzania powtarzalnością rezerwacji
data class RecurrencePattern(
    val frequency: RecurrenceFrequency = RecurrenceFrequency.WEEKLY,
    val endDate: Long = Instant.now().epochSecond, // Do kiedy rezerwacja jest powtarzalna
)

enum class RecurrenceFrequency {
    WEEKLY,
    BIWEEKLY,
    MONTHLY
}



