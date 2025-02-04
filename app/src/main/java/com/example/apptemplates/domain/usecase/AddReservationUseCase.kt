package com.example.apptemplates.domain.usecase

import android.util.Log
import com.example.apptemplates.data.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Reservation

class AddReservationUseCase {

    suspend operator fun invoke(reservations: List<Reservation>) {
        reservations.forEach { reservation -> ReservationsRepositoryImpl.addReservation(reservation) }

        Log.i("AddReservationUseCase", "Dodano rezerwacje!!!")
    }

    suspend operator fun invoke(reservation: Reservation): Boolean {
        return when (val result = ReservationsRepositoryImpl.addReservation(reservation)) {
            is Result.Error -> throw Exception(result.error)
            is Result.Success -> true
            is Result.SuccessWithResult -> false
        }
    }
}