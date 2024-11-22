package com.example.apptemplates.presentation.main.reservation.domain

import android.util.Log
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.result.Result

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