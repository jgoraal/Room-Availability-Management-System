package com.example.apptemplates.presentation.main.home.domain

import com.example.apptemplates.data.room.Room
import com.example.apptemplates.firebase.database.RoomRepositoryImpl
import com.example.apptemplates.result.Result

class FetchRoomsUseCase(private val roomRepository: RoomRepositoryImpl = RoomRepositoryImpl) {

    suspend operator fun invoke(): List<Room> {

        return when (val result = roomRepository.fetchRooms()) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }
}