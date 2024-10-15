package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.FirebaseFirestore

interface ReservationsRepository {
    val database: FirebaseFirestore


    suspend fun fetchReservations(): Result<List<Reservation>>
    suspend fun addReservation(reservation: Reservation): Result<Nothing>
    suspend fun updateReservation(reservation: Reservation): Result<Nothing>
    suspend fun deleteReservation(reservationId: String): Result<Nothing>
    suspend fun getReservationById(reservationId: String): Result<Reservation>
    suspend fun getReservationsByUser(userId: String): Result<List<Reservation>>
    suspend fun getReservationsByRoom(roomId: String): Result<List<Reservation>>
    suspend fun getReservationsByDate(date: String): Result<List<Reservation>>
    suspend fun isRoomAvailable(roomId: String, date: String, timeSlot: String): Result<Boolean>


}