package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ReservationsRepositoryImpl : ReservationsRepository {

    private const val UNKNOWN_ERROR = "Unknown error"
    private const val RESERVATION_NOT_FOUND = "Reservation not found"

    private const val COLLECTION_NAME = "reservations"


    override val database: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()


    override suspend fun fetchReservations(): Result<List<Reservation>> {
        return try {
            val snapshot = database.collection(COLLECTION_NAME).get().await()
            val reservations = snapshot.toObjects(Reservation::class.java)
            Result.SuccessWithResult(reservations)
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun addReservation(reservation: Reservation): Result<Nothing> {
        return try {
            database.collection(COLLECTION_NAME).document(reservation.id).set(reservation).await()
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun updateReservation(reservation: Reservation): Result<Nothing> {
        return try {
            database.collection(COLLECTION_NAME).document(reservation.id).set(reservation).await()
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun deleteReservation(reservationId: String): Result<Nothing> {
        return try {
            database.collection(COLLECTION_NAME).document(reservationId).delete().await()
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun getReservationById(reservationId: String): Result<Reservation> {
        return try {
            val snapshot =
                database.collection(COLLECTION_NAME).document(reservationId).get().await()
            val reservation = snapshot.toObject(Reservation::class.java)
            if (reservation != null) {
                Result.SuccessWithResult(reservation)
            } else {
                Result.Error(RESERVATION_NOT_FOUND)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    override suspend fun getReservationsByUser(userId: String): Result<List<Reservation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReservationsByRoom(roomId: String): Result<List<Reservation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReservationsByDate(date: String): Result<List<Reservation>> {
        TODO("Not yet implemented")
    }

    override suspend fun isRoomAvailable(
        roomId: String,
        date: String,
        timeSlot: String
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }
}