package com.example.apptemplates.presentation.main.reservation.domain

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.result.Result

class FetchStandardReservationsUseCase {

    private val repository = ReservationsRepositoryImpl

    suspend operator fun invoke(newReservation: Reservation): Set<String> {
        return when (newReservation.isRecurring) {
            true -> getStandardReservationsWithRecurringReservation(newReservation)
            false -> getStandardReservations(newReservation)
        }
    }


    private suspend fun getStandardReservationsWithRecurringReservation(newReservation: Reservation): Set<String> {
        return when (val result =
            repository.getConflictingReservationsByNewRecurringReservation(newReservation)) {
            is Result.SuccessWithResult -> result.data ?: emptySet()
            is Result.Error -> throw Exception(result.error)
            else -> emptySet()
        }
    }


    private suspend fun getStandardReservations(newReservation: Reservation): Set<String> {
        return when (val result = repository.getConflictingStandardReservations(newReservation)) {
            is Result.SuccessWithResult -> result.data ?: emptySet()
            is Result.Error -> throw Exception(result.error)
            else -> emptySet()
        }
    }


}