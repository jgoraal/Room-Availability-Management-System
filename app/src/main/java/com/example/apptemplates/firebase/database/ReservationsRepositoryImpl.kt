package com.example.apptemplates.firebase.database

import android.util.Log
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.presentation.main.reservation.generator.isRecurringReservationOverlapping
import com.example.apptemplates.presentation.main.reservation.generator.isRecurringReservationsOverlapping
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

object ReservationsRepositoryImpl : ReservationsRepository {

    private const val UNKNOWN_ERROR = "Unknown error"
    private const val RESERVATION_NOT_FOUND = "Reservation not found"

    private const val COLLECTION_NAME = "reservations"


    override val database: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()


    override suspend fun fetchReservations(userId: String): Result<List<Reservation>> {
        return try {
            val today = LocalDate.now()
            val milis =
                System.currentTimeMillis()//today.atStartOfDay().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
            Log.i("TIME", milis.toString())
            val snapshot = database.collection(COLLECTION_NAME)
                .where(Filter.equalTo("userId", userId))
                .where(Filter.greaterThanOrEqualTo("startTime", milis))
                .where(Filter.notEqualTo("status", "CANCELED"))
                .orderBy("startTime")
                .get().await()
            val reservations = snapshot.toObjects(Reservation::class.java)
            //.filter { it.userId == userId && it.startTime >= milis && it.status != ReservationStatus.CANCELED }
            Result.SuccessWithResult(reservations)
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    //take
    suspend fun getConflictingStandardReservations(newReservation: Reservation): Result<Set<String>> {

        return try {
            val snapshot = database.collection(COLLECTION_NAME)
                .whereNotEqualTo("status", "CANCELED") // No canceled reservations
                .whereEqualTo("dayOfWeek", newReservation.dayOfWeek) // Same day of the week
                .whereEqualTo("isRecurring", false) // Standard reservations only
                .whereLessThanOrEqualTo("startTime", newReservation.endTime)
                .whereGreaterThanOrEqualTo("endTime", newReservation.startTime)
                .get().await()

            val reservations = snapshot.toObjects(Reservation::class.java)
                .filter { reservation -> reservation.recurrencePattern == null }
                .distinctBy { reservation -> reservation.roomId }
                .map { reservation -> reservation.roomId }
                .toSet()

            Result.SuccessWithResult(reservations)

        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    suspend fun getConflictingRecurringReservations(
        newReservation: Reservation,
        excludedRoomIds: Set<String>
    ): Result<Set<String>> {

        return try {

            val snapshot = database.collection(COLLECTION_NAME)
                .whereNotEqualTo("status", "CANCELED") // No canceled reservations
                .whereEqualTo("dayOfWeek", newReservation.dayOfWeek) // Same day of the week
                .whereEqualTo("isRecurring", true) // recurring reservations only
                .whereGreaterThanOrEqualTo(
                    "recurrencePattern.endDate",
                    newReservation.startTime
                ) // Only get recurring reservations that end after the start time of the new reservation
                .whereLessThanOrEqualTo(
                    "startTime",
                    newReservation.endTime
                ) // Only get recurring reservations that start before the end time of the new reservation)
                .get().await()

            val reservations = snapshot.toObjects(Reservation::class.java)
                .filter { reservation ->
                    reservation.recurrencePattern != null &&
                            reservation.startTime < reservation.recurrencePattern.endDate &&
                            reservation.roomId !in excludedRoomIds &&
                            isRecurringReservationOverlapping(newReservation, reservation)

                }
                .distinctBy { reservation -> reservation.roomId }
                .map { reservation -> reservation.roomId }.toSet()

            Result.SuccessWithResult(reservations)

        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    //take
    suspend fun getConflictingReservationsByNewRecurringReservation(
        newReservation: Reservation
    ): Result<Set<String>> {
        return try {
            val snapshot = database.collection(COLLECTION_NAME)
                .whereNotEqualTo("status", "CANCELED") // No canceled reservations
                .whereEqualTo("dayOfWeek", newReservation.dayOfWeek) // Same day of the week
                .whereEqualTo("isRecurring", false) // Standard reservations only
                .whereEqualTo("recurrencePattern", null)
                .whereLessThanOrEqualTo("startTime", newReservation.recurrencePattern!!.endDate)
                .get().await()

            val reservations = snapshot.toObjects(Reservation::class.java)
                .filter { reservation ->
                    reservation.recurrencePattern == null &&
                            isRecurringReservationOverlapping(
                                reservation,
                                newReservation
                            )
                }
                .distinctBy { reservation -> reservation.roomId }
                .map { reservation -> reservation.roomId }
                .toSet()

            Result.SuccessWithResult(reservations)
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }


    suspend fun getConflictingRecurringReservationsByNewRecurringReservation(
        newReservation: Reservation,
        excludedRoomIds: Set<String>
    ): Result<Set<String>> {
        return try {

            val snapshot = database.collection(COLLECTION_NAME)
                .whereNotEqualTo("status", "CANCELED") // No canceled reservations
                .whereEqualTo("dayOfWeek", newReservation.dayOfWeek) // Same day of the week
                .whereEqualTo("isRecurring", true) // recurring reservations only
                .whereLessThanOrEqualTo(
                    "startTime",
                    newReservation.recurrencePattern!!.endDate
                )
                .whereGreaterThanOrEqualTo(
                    "recurrencePattern.endDate",
                    newReservation.endTime
                )

                .get().await()

            val reservations = snapshot.toObjects(Reservation::class.java)
                .filter { reservation ->
                    reservation.recurrencePattern != null &&
                            reservation.startTime < reservation.recurrencePattern.endDate &&
                            reservation.roomId !in excludedRoomIds &&
                            isRecurringReservationsOverlapping(newReservation, reservation)

                }
                .distinctBy { reservation -> reservation.roomId }
                .map { reservation -> reservation.roomId }.toSet()

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