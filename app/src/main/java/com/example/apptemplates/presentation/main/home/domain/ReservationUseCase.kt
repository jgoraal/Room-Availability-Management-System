package com.example.apptemplates.presentation.main.home.domain

import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.result.Result

class ReservationUseCase {

    private val repository = ReservationsRepositoryImpl
    private val errorMessage = "Nie udało się usunąć rezerwacji!"

    suspend operator fun invoke(reservationId: String) {
        when (repository.deleteReservation(reservationId)) {
            is Result.Success -> return
            is Result.Error -> throw Exception(errorMessage)
            else -> throw Exception(errorMessage)
        }
    }


    suspend operator fun invoke(reservationId: String, equipment: List<EquipmentType>) {
        when (repository.updateReservationEquipment(reservationId, equipment)) {
            is Result.Success -> return
            is Result.Error -> throw Exception(errorMessage)
            else -> throw Exception(errorMessage)
        }
    }
}