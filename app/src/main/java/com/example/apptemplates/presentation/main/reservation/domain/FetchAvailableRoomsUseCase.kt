package com.example.apptemplates.presentation.main.reservation.domain


import com.example.apptemplates.data.room.Room
import com.example.apptemplates.firebase.database.RoomRepositoryImpl
import com.example.apptemplates.result.Result

class FetchAvailableRoomsUseCase {

    suspend operator fun invoke(lessons: List<String>): List<Room> {
        return when (val result = RoomRepositoryImpl.fetchAvailableRooms(lessons)) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }


    }

}
