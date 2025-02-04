package com.example.apptemplates.domain.repository

import com.example.apptemplates.domain.model.Reservation
import com.example.apptemplates.data.firebase.database.result.Result
import com.google.firebase.firestore.FirebaseFirestore

interface ReservationsRepository {
    val database: FirebaseFirestore


    suspend fun fetchReservations(userId: String): Result<List<Reservation>>
    suspend fun addReservation(reservation: Reservation): Result<Nothing>
    suspend fun updateReservation(reservation: Reservation): Result<Nothing>
    suspend fun deleteReservation(reservationId: String): Result<Nothing>
    suspend fun getReservationById(reservationId: String): Result<Reservation>
    suspend fun getReservationsByUser(userId: String): Result<List<Reservation>>
    suspend fun getReservationsByRoom(roomId: String): Result<List<Reservation>>
    suspend fun getReservationsByDate(date: String): Result<List<Reservation>>
    suspend fun isRoomAvailable(roomId: String, date: String, timeSlot: String): Result<Boolean>


}