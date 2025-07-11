package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.room.Room
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object RoomRepositoryImpl : RoomRepository {
    override val database: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    override suspend fun fetchRooms(roomIds: Set<String>): Result<List<Room>> {
        return try {
            val snapshot = database.collection("rooms")
                .get().await()
            val rooms = snapshot.toObjects(Room::class.java)
                .filter { it.id in roomIds }
            Result.SuccessWithResult(rooms)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun addRoom(room: Room): Result<Nothing> {
        return try {
            database.collection("rooms").document(room.id).set(room).await()
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun updateRoom(room: Room): Result<Nothing> {
        return try {
            database.collection("rooms").document(room.id).set(room).await()
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun deleteRoom(roomId: String): Result<Nothing> {
        return try {
            database.collection("rooms").document(roomId).delete().await()
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getRoomById(roomId: String): Result<Room> {
        return try {
            val snapshot = database.collection("rooms").document(roomId).get().await()
            val room = snapshot.toObject(Room::class.java)
            if (room != null) {
                Result.SuccessWithResult(room)
            } else {
                Result.Error("Room not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getRoomsByLocation(location: String): Result<List<Room>> {
        return try {
            val snapshot = database.collection("rooms")
                .whereEqualTo("location", location).get().await()
            val rooms = snapshot.toObjects(Room::class.java)
            Result.SuccessWithResult(rooms)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun fetchAvailableRooms(lessons: List<String>): Result<List<Room>> {
        return try {

            val snapshot = database
                .collection("rooms")
                .get().await()


            val availableRooms = snapshot.toObjects(Room::class.java)
                .filter { it.id !in lessons }
                .sortedBy { it.name }

            Result.SuccessWithResult(availableRooms)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun fetchRoomsByFloor(floor: Int?): Result<List<Room>> {
        return try {

            val snapshot = database
                .collection("rooms")


            val rooms = if (floor != null) {
                snapshot.whereEqualTo("floor", floor).get().await()
            } else {
                snapshot.get().await()
            }


            val availableRooms = rooms.toObjects(Room::class.java)
                .sortedWith(compareBy<Room> { it.floor }.thenBy {
                    it.name.filter { char -> char.isDigit() }.toIntOrNull() ?: Int.MAX_VALUE
                })

            Result.SuccessWithResult(availableRooms)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }

    }

}