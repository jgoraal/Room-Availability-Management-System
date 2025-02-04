package com.example.apptemplates.domain.usecase

import android.util.Log
import com.example.apptemplates.data.firebase.database.FirestoreRepository
import com.example.apptemplates.data.firebase.database.result.FirestoreResult
import com.example.apptemplates.data.model.model.user.User

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