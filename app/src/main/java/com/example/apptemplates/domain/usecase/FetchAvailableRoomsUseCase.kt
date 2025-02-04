package com.example.apptemplates.domain.usecase


import com.example.apptemplates.data.firebase.database.RoomRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Room

class FetchAvailableRoomsUseCase {

    suspend operator fun invoke(lessons: List<String>): List<Room> {
        return when (val result = RoomRepositoryImpl.fetchAvailableRooms(lessons)) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }


    }

}
