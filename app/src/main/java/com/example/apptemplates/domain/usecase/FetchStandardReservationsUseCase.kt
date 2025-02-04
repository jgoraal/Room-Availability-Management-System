package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Reservation

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