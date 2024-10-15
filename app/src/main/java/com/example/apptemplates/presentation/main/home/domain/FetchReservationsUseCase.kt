package com.example.apptemplates.presentation.main.home.domain

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.result.Result

class FetchReservationsUseCase {

    suspend operator fun invoke(): List<Reservation> {
        return when (val result = ReservationsRepositoryImpl.fetchReservations()) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }
}