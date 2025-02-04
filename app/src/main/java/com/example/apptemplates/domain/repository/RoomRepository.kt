package com.example.apptemplates.domain.repository

import com.example.apptemplates.domain.model.Room
import com.example.apptemplates.data.firebase.database.result.Result
import com.google.firebase.firestore.FirebaseFirestore

interface RoomRepository {
    val database: FirebaseFirestore

    suspend fun fetchRooms(roomIds: Set<String>): Result<List<Room>>
    suspend fun addRoom(room: Room): Result<Void>
    suspend fun updateRoom(room: Room): Result<Void>
    suspend fun deleteRoom(roomId: String): Result<Void>
    suspend fun getRoomById(roomId: String): Result<Room>
    suspend fun getRoomsByLocation(location: String): Result<List<Room>>


}