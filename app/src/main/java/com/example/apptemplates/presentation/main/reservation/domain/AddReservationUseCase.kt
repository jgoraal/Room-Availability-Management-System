package com.example.apptemplates.presentation.main.reservation.domain

import android.util.Log
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.firebase.database.ReservationsRepositoryImpl

class AddReservationUseCase {

    suspend operator fun invoke(reservations: List<Reservation>) {
        reservations.forEach { reservation -> ReservationsRepositoryImpl.addReservation(reservation) }

        Log.i("AddReservationUseCase", "Dodano rezerwacje!!!")
    }
}