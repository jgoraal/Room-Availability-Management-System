package com.example.apptemplates.firebase.database

import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Equipment
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.presentation.main.reservation.generator.isRecurringReservationOverlapping
import com.example.apptemplates.presentation.main.reservation.generator.isRecurringReservationsOverlapping
import com.example.apptemplates.result.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object ReservationsRepositoryImpl : ReservationsRepository {

    private const val UNKNOWN_ERROR = "Unknown error"
    private const val RESERVATION_NOT_FOUND = "Reservation not found"

    private const val COLLECTION_NAME = "reservations"


    override val database: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()


    override suspend fun fetchReservations(userId: String): Result<List<Reservation>> {
        return try {

            val snapshot = database.collection(COLLECTION_NAME)
                //.whereNotEqualTo("status", "CANCELED")
                .whereEqualTo("recurring", false)
                .whereEqualTo("userId", userId)
                //.whereEqualTo("dayOfWeek", today.dayOfWeek)
                /*.whereLessThanOrEqualTo(
                    "startTime",
                    todayEnd.toInstant(ZoneOffset.UTC).toEpochMilli()
                )
                .whereGreaterThanOrEqualTo(
                    "endTime",
                    today.toInstant(ZoneOffset.UTC).toEpochMilli()
                )*/
                .get().await()

            val reservations = snapshot.toObjects(Reservation::class.java)
                .filter { reservation -> reservation.recurrencePattern == null }


            val secondSnapshot = database.collection(COLLECTION_NAME)
                //.whereNotEqualTo("status", "CANCELED")
                .whereEqualTo("recurring", true)
                .whereEqualTo("userId", userId)
                //.whereEqualTo("dayOfWeek", today.dayOfWeek)
                /*.whereGreaterThanOrEqualTo(
                    "recurrencePattern.endDate",
                    today.toInstant(ZoneOffset.UTC).toEpochMilli()
                )*/
                .get().await()

            val recurringReservations = secondSnapshot.toObjects(Reservation::class.java)
                .filter { reservation ->
                    reservation.recurrencePattern != null &&
                            reservation.startTime < reservation.recurrencePattern.endDate /*&&
                            isOverlapping(
                                reservation,
                                today.toInstant(ZoneOffset.UTC).toEpochMilli()
                            )*/
                }


            val allReservations = (reservations + recurringReservations)
                .sortedWith(compareBy<Reservation> { it.status }.thenBy { it.startTime }
                    .thenBy { it.createdAt })


            Result.SuccessWithResult(allReservations)

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
                .whereEqualTo("recurring", false) // Standard reservations only
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
                .whereEqualTo("recurring", true) // recurring reservations only
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
                .whereEqualTo("recurring", false) // Standard reservations only
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
                .whereEqualTo("recurring", true) // recurring reservations only
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
            database.collection(COLLECTION_NAME).document(reservationId)
                .update("status", "CANCELED").await()
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


    // non recurring
    suspend fun getReservationsByRoomId(
        roomId: String,
        dayOfWeek: DayOfWeek,
        timeStart: Long,
        timeEnd: Long
    ): Result<List<Reservation>> {
        return try {
            val snapshot = database.collection(COLLECTION_NAME)
                .whereNotEqualTo("status", "CANCELED") // No canceled reservations
                .whereEqualTo("recurring", false) // recurring reservations only
                .whereEqualTo("roomId", roomId)
                .whereEqualTo("dayOfWeek", dayOfWeek)
                .whereLessThanOrEqualTo("startTime", timeEnd)
                .whereGreaterThanOrEqualTo("endTime", timeStart)
                .get().await()

            val reservation = snapshot.toObjects(Reservation::class.java)
                .filter { reservation -> reservation.recurrencePattern == null }


            Result.SuccessWithResult(reservation)

        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    // recurring
    suspend fun getRecurringReservationsByRoomId(
        roomId: String,
        dayOfWeek: DayOfWeek,
        timeStart: Long,
    ): Result<List<Reservation>> {
        return try {
            val snapshot = database.collection(COLLECTION_NAME)
                .whereNotEqualTo("status", "CANCELED")
                .whereEqualTo("recurring", true)
                .whereEqualTo("roomId", roomId)
                .whereEqualTo("dayOfWeek", dayOfWeek)
                .whereGreaterThanOrEqualTo("recurrencePattern.endDate", timeStart)
                .get().await()

            val reservation = snapshot.toObjects(Reservation::class.java)
                .filter { reservation ->
                    reservation.recurrencePattern != null &&
                            reservation.startTime < reservation.recurrencePattern.endDate &&
                            isOverlapping(reservation, timeStart)
                }

            Result.SuccessWithResult(reservation)

        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }


    suspend fun deleteUserReservations(userId: String): Result<Boolean> {
        return try {
            // Query all reservations with the specified userId
            val snapshot = database.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Iterate over the results and delete each document
            for (document in snapshot.documents) {
                database.collection(COLLECTION_NAME).document(document.id).delete().await()
            }

            Result.SuccessWithResult(true) // All deletions were successful
        } catch (e: Exception) {
            Result.Error("Nie udało się usunąć użytkownika!") // Return the error message
        }
    }


    suspend fun updateReservationEquipment(
        id: String,
        equipment: List<EquipmentType>
    ): Result<Nothing> {
        return try {
            val reservation = when (val result = getReservationById(id)) {
                is Result.SuccessWithResult -> {
                    (result.data as Reservation).copy(additionalEquipment = equipment.map {
                        Equipment(
                            it
                        )
                    })
                }

                else -> throw Exception("Nie znaleziono rezerwacji!")
            }

            updateReservation(reservation)
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: UNKNOWN_ERROR)
        }
    }


    private fun isOverlapping(reservation: Reservation, timeStart: Long): Boolean {
        val recurrencePattern = reservation.recurrencePattern ?: return false

        // Convert times to LocalDateTime for comparison
        val reservationStart =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(reservation.startTime), ZoneOffset.UTC)
        val startDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStart), ZoneOffset.UTC)

        // Determine recurrence interval in days based on frequency
        val recurrenceIntervalDays = when (recurrencePattern.frequency) {
            RecurrenceFrequency.WEEKLY -> 7
            RecurrenceFrequency.BIWEEKLY -> 14
            RecurrenceFrequency.MONTHLY -> 28
        }

        // Calculate days difference and check if it aligns with recurrence
        val daysDifference = Duration.between(
            reservationStart.toLocalDate().atStartOfDay(),
            startDateTime.toLocalDate().atStartOfDay()
        ).toDays()

        return daysDifference % recurrenceIntervalDays == 0L
    }


}