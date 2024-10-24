package com.example.apptemplates.presentation.main.home.domain

import android.util.Log
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.firebase.database.RoomRepositoryImpl
import com.example.apptemplates.result.Result

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