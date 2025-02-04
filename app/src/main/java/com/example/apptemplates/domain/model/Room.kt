package com.example.apptemplates.domain.model

import java.time.DayOfWeek
import java.time.Instant
import java.util.UUID

data class Room(
    val id: String = UUID.randomUUID().toString(),

    val name: String = "",
    val contactEmail: String = "",
    val floor: Int = 1,
    val capacity: Int = 1,

    val equipment: List<Equipment> = emptyList(),

    val isAvailable: Boolean = true
)

data class Lesson(
    val id: String = UUID.randomUUID().toString(),
    val roomId: String = "",

    val name: String = "",
    val userId: String = "",

    val day: DayOfWeek = DayOfWeek.MONDAY,

    val lessonStart: Long = Instant.now().epochSecond,
    val lessonEnd: Long = Instant.now().plusSeconds(7200).epochSecond,
    val lessonEndDate: Long = Instant.now().epochSecond,

    val frequency: RecurrenceFrequency = RecurrenceFrequency.WEEKLY
)

enum class EquipmentType {
    COMPUTER, PROJECTOR, WHITEBOARD,
}

data class Equipment(
    val type: EquipmentType = EquipmentType.COMPUTER,
    val quantity: Int = 1
)
