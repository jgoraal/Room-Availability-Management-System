package com.example.apptemplates.presentation.main.room_availability.domain

import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Lesson
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.firebase.database.LessonsRepositoryImpl
import com.example.apptemplates.firebase.database.ReservationsRepositoryImpl
import com.example.apptemplates.firebase.database.RoomRepositoryImpl
import com.example.apptemplates.result.Result
import java.time.DayOfWeek

class RoomFetchRepository {

    suspend operator fun invoke(floor: Int?): List<Room> {
        return when (val result = RoomRepositoryImpl.fetchRoomsByFloor(floor)) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }


    suspend operator fun invoke(roomId: String, dayOfWeek: DayOfWeek, time: Long): List<Lesson> {
        return when (val result =
            LessonsRepositoryImpl.getLessonsByRoomId(roomId, dayOfWeek, time)) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }


    // Non Recurring Reservations
    suspend operator fun invoke(
        dayOfWeek: DayOfWeek,
        roomId: String,
        timeStart: Long,
        timeEnd: Long
    ): List<Reservation> {
        return when (val result =
            ReservationsRepositoryImpl.getReservationsByRoomId(
                roomId,
                dayOfWeek,
                timeStart,
                timeEnd
            )) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }


    // Recurring Reservations
    suspend operator fun invoke(
        timeStart: Long,
        dayOfWeek: DayOfWeek,
        roomId: String,
    ): List<Reservation> {
        return when (val result =
            ReservationsRepositoryImpl.getRecurringReservationsByRoomId(
                roomId,
                dayOfWeek,
                timeStart
            )) {
            is Result.SuccessWithResult -> result.data ?: emptyList()
            is Result.Error -> throw Exception(result.error)
            else -> emptyList()
        }
    }

}