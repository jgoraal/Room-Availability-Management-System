package com.example.apptemplates.presentation.main.reservation.generator

import com.example.apptemplates.data.room.Equipment
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import java.util.UUID

fun generateRandomRoomIds(numberOfRooms: Int): List<String> {
    return List(numberOfRooms) { UUID.randomUUID().toString() }
}


// Random room generation
fun generateRandomRooms(roomIds: List<String>): List<Room> {
    val rooms = mutableListOf<Room>()

    roomIds.forEachIndexed { index, roomId ->
        rooms.add(
            Room(
                id = roomId,
                name = "Room ${index + 1}", // Assign room numbers
                floor = (1..3).random(), // Random floor (1-3)
                capacity = (10..100).random(), // Random capacity (10-100 seats)
                equipment = generateRandomEquipment(), // Random equipment
            )
        )
    }
    return rooms
}

// Random equipment generation
fun generateRandomEquipment(): List<Equipment> {
    val equipment = mutableListOf<Equipment>()

    if (listOf(true, false).random()) {
        equipment.add(Equipment(EquipmentType.COMPUTER, (10..15).random()))
    }
    if (listOf(true, false).random()) {
        equipment.add(Equipment(EquipmentType.PROJECTOR, (0..1).random()))
    }
    if (listOf(true, false).random()) {
        equipment.add(Equipment(EquipmentType.WHITEBOARD, (0..1).random()))
    }

    return equipment
}
