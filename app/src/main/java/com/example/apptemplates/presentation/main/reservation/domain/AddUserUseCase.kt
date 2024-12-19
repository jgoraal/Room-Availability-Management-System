package com.example.apptemplates.presentation.main.reservation.domain

import android.util.Log
import com.example.apptemplates.data.user.User
import com.example.apptemplates.firebase.database.FirestoreRepository
import com.example.apptemplates.firebase.database.result.FirestoreResult

class AddUserUseCase {

    suspend operator fun invoke(users: List<User>) {
        for (user in users) {
            when (val result = FirestoreRepository.addUser(user)) {
                is FirestoreResult.Success -> continue
                is FirestoreResult.Failure -> throw Exception(result.exception)
                else -> return
            }
        }

        Log.i("AddRoomUseCase", "Dodano użytkowników")
    }
}