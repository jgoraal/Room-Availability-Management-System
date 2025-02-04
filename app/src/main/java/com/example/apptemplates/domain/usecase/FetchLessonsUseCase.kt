package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.database.LessonsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Reservation

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
