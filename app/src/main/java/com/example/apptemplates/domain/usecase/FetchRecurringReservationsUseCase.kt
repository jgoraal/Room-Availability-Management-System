package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Reservation

class FetchRecurringReservationsUseCase {

    private val repository = ReservationsRepositoryImpl

    suspend operator fun invoke(
        newReservation: Reservation,
        excludedRoomIds: Set<String>
    ): Set<String> {
        return when (newReservation.isRecurring) {
            true -> getRecurringReservationsWithRecurringReservation(
                newReservation,
                excludedRoomIds
            )

            false -> getRecurringReservationsWithStandardReservation(
                newReservation,
                excludedRoomIds
            )
        }
    }


    private suspend fun getRecurringReservationsWithRecurringReservation(
        newReservation: Reservation,
        excludedRoomIds: Set<String>
    ): Set<String> {
        return when (val result =
            repository.getConflictingRecurringReservationsByNewRecurringReservation(
                newReservation,
                excludedRoomIds
            )) {
            is Result.SuccessWithResult -> result.data ?: emptySet()
            is Result.Error -> throw Exception(result.error)
            else -> emptySet()
        }
    }


    private suspend fun getRecurringReservationsWithStandardReservation(
        newReservation: Reservation,
        excludedRoomIds: Set<String>
    ): Set<String> {
        return when (val result =
            repository.getConflictingRecurringReservations(newReservation, excludedRoomIds)) {
            is Result.SuccessWithResult -> result.data ?: emptySet()
            is Result.Error -> throw Exception(result.error)
            else -> emptySet()
        }
    }

}
