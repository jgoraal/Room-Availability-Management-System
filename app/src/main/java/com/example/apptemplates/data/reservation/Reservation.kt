package com.example.apptemplates.data.reservation

import com.example.apptemplates.data.room.Equipment
import java.time.DayOfWeek
import java.time.Instant
import java.util.UUID

data class Reservation(
    val id: String = UUID.randomUUID().toString(),
    val roomId: String = "",
    val userId: String = "",

    val createdAt: Long = Instant.now().epochSecond,
    val startTime: Long = Instant.now().epochSecond,  // Użycie Instant dla uniwersalnego formatu czasu
    val endTime: Long = Instant.now()
        .plusSeconds(7200).epochSecond,  // Domyślne ustawienie 1-godzinnej rezerwacji
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY, // Na przykład Calendar.MONDAY

    val participants: Int? = null,
    val status: ReservationStatus = ReservationStatus.PENDING,
    val additionalEquipment: List<Equipment> = emptyList(),

    val isRecurring: Boolean = false,
    val recurrencePattern: RecurrencePattern? = null
)


// Statusy rezerwacji
enum class ReservationStatus {
    PENDING,    // Oczekująca na zatwierdzenie
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



