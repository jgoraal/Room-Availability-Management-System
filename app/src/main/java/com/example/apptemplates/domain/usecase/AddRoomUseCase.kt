package com.example.apptemplates.domain.usecase

import android.util.Log
import com.example.apptemplates.data.firebase.database.RoomRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Room

class AddRoomUseCase {

    suspend operator fun invoke(rooms: List<Room>) {
        for (room in rooms) {
            when (val result = RoomRepositoryImpl.addRoom(room)) {
                is Result.Success -> continue
                is Result.Error -> throw Exception(result.error)
                else -> return
            }
        }

        Log.i("AddRoomUseCase", "Dodano pokoje")
    }
}