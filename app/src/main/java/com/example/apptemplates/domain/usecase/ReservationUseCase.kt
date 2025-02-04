package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.EquipmentType

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