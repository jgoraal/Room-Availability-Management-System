package com.example.apptemplates.presentation.screens.home.reservation.gen

import com.example.apptemplates.domain.model.Equipment
import com.example.apptemplates.domain.model.EquipmentType
import com.example.apptemplates.domain.model.Room
import java.util.UUID

fun generateRandomRoomIds(numberOfRooms: Int): List<String> {
    return List(numberOfRooms) { UUID.randomUUID().toString() }
}



fun generateRandomRooms(roomIds: List<String>): List<Room> {
    val rooms = mutableListOf<Room>()

    roomIds.forEachIndexed { index, roomId ->
        rooms.add(
            Room(
                id = roomId,
                name = "Room ${index + 1}",
                contactEmail = generateRandomEmail(),
                floor = (1..3).random(),
                capacity = (10..100).random(),
                equipment = generateRandomEquipment(),
            )
        )
    }
    return rooms
}


fun generateRandomEmail(): String {
    val firstNames = listOf("anna", "adam", "jakub", "marta", "kamil", "paulina", "michal", "piotr")
    val lastNames =
        listOf("kowalski", "nowak", "wrobel", "zajac", "lis", "duda", "mazur", "urbanski")

    val firstName = firstNames.random().lowercase()
    val lastName = lastNames.random().lowercase()
    return "$firstName.$lastName@put.poznan.pl"
}


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
