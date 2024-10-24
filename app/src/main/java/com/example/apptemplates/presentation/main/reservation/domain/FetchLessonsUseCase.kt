package com.example.apptemplates.presentation.main.reservation.domain

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.firebase.database.LessonsRepositoryImpl
import com.example.apptemplates.result.Result

class FetchLessonsUseCase {

    suspend operator fun invoke(roomIds: List<String>, newReservation: Reservation): Set<String> {
        return when (val result =
            LessonsRepositoryImpl.getOverlappingLessonsRoomsId(roomIds, newReservation)) {
            is Result.SuccessWithResult -> result.data ?: emptySet()
            is Result.Error -> throw Exception(result.error)
            else -> emptySet()
        }


    }

}
