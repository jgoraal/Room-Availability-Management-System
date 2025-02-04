package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Reservation

class FetchReservationsUseCase {

    suspend operator fun invoke(userId: String): List<Reservation> {
        return when (val result = ReservationsRepositoryImpl.fetchReservations(userId)) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }
}