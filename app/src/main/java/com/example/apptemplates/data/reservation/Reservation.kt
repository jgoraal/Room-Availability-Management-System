package com.example.apptemplates.data.reservation

import java.util.Date
import java.util.UUID

data class Reservation(
    val id: String = UUID.randomUUID().toString(),
    val roomId: String,
    val userId: String,
    val startTime: Date,
    val endTime: Date,
    val purpose: String,
    val status: ReservationStatus,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val participants: List<String>? = null,
    val equipment: List<String>? = null,
    val notes: String? = null,
    val isRecurring: Boolean = false
)

enum class ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELED
}
