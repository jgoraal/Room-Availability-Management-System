package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.database.FirestoreRepository
import com.example.apptemplates.data.firebase.database.result.FirestoreResult
import com.example.apptemplates.data.model.model.user.User
import com.example.apptemplates.presentation.screens.home.room_availability.UserDetails


class UserUseCase {

    private val repository = FirestoreRepository

    suspend operator fun invoke(userIds: List<String>): List<UserDetails> {
        return when (val result = repository.fetchUsers(userIds)) {
            is FirestoreResult.SuccessWithResult<*> -> {

                val users = result.data as List<User>
                users.filter { it.uid in userIds }
                    .map { UserDetails(it.uid, it.username, it.email) }
            }

            is FirestoreResult.Failure -> emptyList()
            else -> emptyList()
        }
    }

}