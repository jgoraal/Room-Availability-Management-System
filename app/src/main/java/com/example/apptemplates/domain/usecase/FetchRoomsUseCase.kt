package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.database.RoomRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Room

class FetchRoomsUseCase(private val roomRepository: RoomRepositoryImpl = RoomRepositoryImpl) {

    suspend operator fun invoke(roomIds: Set<String>): List<Room> {

        return when (val result = roomRepository.fetchRooms(roomIds)) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }
}