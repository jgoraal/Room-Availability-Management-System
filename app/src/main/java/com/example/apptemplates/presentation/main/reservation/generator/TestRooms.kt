package com.example.apptemplates.presentation.main.reservation.generator

import android.util.Log
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.RecurrencePattern
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

fun roomAvailableStandardReservation() {

    val randomRoomIds = generateRandomRoomIds(40)

    val rooms = generateRandomRooms(randomRoomIds)

    val lessons = generateRealisticLessonsForRooms(randomRoomIds)

    val reservations = generateRandomReservations(randomRoomIds, lessons)

    val newTestReservation = generateTestReservation()


    val overlappingSimpleReservationsRoomIds = reservations.filter { reservation ->
        reservation.status != ReservationStatus.CANCELED && reservation.dayOfWeek == newTestReservation.dayOfWeek && !reservation.isRecurring && reservation.recurrencePattern == null && reservation.startTime <= newTestReservation.endTime && reservation.endTime >= newTestReservation.startTime
    }.distinctBy { r -> r.roomId }.map { r -> r.roomId }.toSet()

    Log.i(
        "ROOMS",
        "Liczba nachodzacych zwykłych rezerwacji: ${overlappingSimpleReservationsRoomIds.size}"
    )


    val overlappingReservationRoomIds = reservations.filter { reservation ->
        reservation.status != ReservationStatus.CANCELED && reservation.dayOfWeek == newTestReservation.dayOfWeek && reservation.isRecurring && reservation.recurrencePattern?.let { recurrencePattern ->
            recurrencePattern.endDate >= newTestReservation.startTime && reservation.startTime < recurrencePattern.endDate
        } ?: false && reservation.startTime <= newTestReservation.endTime && isRecurringReservationOverlapping(
            newTestReservation,
            reservation
        ) && reservation.roomId !in overlappingSimpleReservationsRoomIds
    }.distinctBy { r -> r.roomId }.map { r -> r.roomId }.toSet()

    Log.i("ROOMS", "Liczba nachodzacych rezerwacji: ${overlappingReservationRoomIds.size}")

    val overlappingStandardAndRecurringReservationRoomIds =
        overlappingSimpleReservationsRoomIds + overlappingReservationRoomIds


    val overlappingLessonRoomIds = lessons.filter { lesson ->
        lesson.day == newTestReservation.dayOfWeek &&
                lesson.lessonEndDate >= newTestReservation.startTime &&
                lesson.roomId !in overlappingStandardAndRecurringReservationRoomIds &&
                isLessonOverlapping(
                    lesson,
                    newTestReservation
                )
    }.distinctBy { l -> l.roomId }.map { r -> r.roomId }.toSet()


    Log.i("ROOMS", "Liczba nachodzacych lekcji: ${overlappingLessonRoomIds.size}")


    val allOverlappingRoomIds =
        overlappingSimpleReservationsRoomIds + overlappingReservationRoomIds + overlappingLessonRoomIds


    val availableRooms = rooms.filter { room ->
        room.id !in allOverlappingRoomIds
    }



    Log.i("ROOMS", "Liczba wygenerowanych pokoi: ${rooms.size}")
    Log.i("ROOMS", "Liczba znalezionych wolnych pokoi: ${availableRooms.size}")
}


fun roomAvailableRecurringReservation() {

    val randomRoomIds = generateRandomRoomIds(40)

    val rooms = generateRandomRooms(randomRoomIds)

    val lessons = generateRealisticLessonsForRooms(randomRoomIds)

    val reservations = generateRandomReservations(randomRoomIds, lessons)

    val newTestReservation = generateTestRecurringReservation()


    val overlappingSimpleReservationsRoomIds = reservations.filter { reservation ->
        reservation.status != ReservationStatus.CANCELED &&
                reservation.dayOfWeek == newTestReservation.dayOfWeek &&
                !reservation.isRecurring &&
                reservation.recurrencePattern == null &&
                newTestReservation.recurrencePattern!!.endDate >= reservation.startTime &&
                isRecurringReservationOverlapping(
                    reservation,
                    newTestReservation
                )

    }.distinctBy { r -> r.roomId }.map { r -> r.roomId }.toSet()


    Log.i(
        "ROOMS",
        "Liczba nachodzacych zwykłych rezerwacji: ${overlappingSimpleReservationsRoomIds.size}"
    )

    val overlappingRecurringReservationRoomIds = reservations.filter { reservation ->
        reservation.status != ReservationStatus.CANCELED &&
                reservation.dayOfWeek == newTestReservation.dayOfWeek &&
                reservation.isRecurring && reservation.recurrencePattern != null &&
                reservation.startTime < reservation.recurrencePattern.endDate &&
                reservation.startTime <= newTestReservation.recurrencePattern!!.endDate &&
                reservation.recurrencePattern.endDate >= newTestReservation.endTime &&
                isRecurringReservationsOverlapping(
                    newTestReservation,
                    reservation
                ) && reservation.roomId !in overlappingSimpleReservationsRoomIds
    }.distinctBy { r -> r.roomId }.map { r -> r.roomId }.toSet()

    Log.i(
        "ROOMS",
        "Liczba nachodzacych cyklicznych rezerwacji: ${overlappingRecurringReservationRoomIds.size}"
    )


    val overlappingStandardAndRecurringReservationRoomIds =
        overlappingSimpleReservationsRoomIds + overlappingRecurringReservationRoomIds


    val overlappingLessonRoomIds = lessons.filter { lesson ->
        lesson.day == newTestReservation.dayOfWeek && lesson.lessonEndDate >= newTestReservation.startTime && lesson.roomId !in overlappingStandardAndRecurringReservationRoomIds && isLessonOverlapping(
            lesson,
            newTestReservation
        )
    }.distinctBy { l -> l.roomId }.map { r -> r.roomId }.toSet()


    Log.i("ROOMS", "Liczba nachodzacych lekcji: ${overlappingLessonRoomIds.size}")


    val allOverlappingRoomIds =
        overlappingStandardAndRecurringReservationRoomIds + overlappingLessonRoomIds


    val availableRooms = rooms.filter { room ->
        room.id !in allOverlappingRoomIds
    }

    Log.i("ROOMS", "Liczba wygenerowanych pokoi: ${rooms.size}")
    Log.i("ROOMS", "Liczba znalezionych wolnych pokoi: ${availableRooms.size}")


    for (room in availableRooms) {
        Log.i("Room", "niby wolny pokój: ${room.id}")
    }


    Log.i(
        "re",
        "Start: ${newTestReservation.startTime.toLocalDateTime()} Koniec: ${newTestReservation.endTime.toLocalDateTime()} Koniec Cyklu: ${newTestReservation.recurrencePattern?.endDate?.toLocalDateTime()}"
    )

    val f = reservations.filter { r ->
        r.status != ReservationStatus.CANCELED &&
                r.dayOfWeek == newTestReservation.dayOfWeek &&
                r.isRecurring && r.recurrencePattern != null &&
                r.startTime < r.recurrencePattern.endDate &&
                r.startTime <= newTestReservation.recurrencePattern!!.endDate &&
                r.recurrencePattern.endDate >= newTestReservation.endTime &&
                isRecurringReservationsOverlapping(
                    newTestReservation,
                    r
                ) && r.roomId !in overlappingSimpleReservationsRoomIds
    }.forEach { r ->
        Log.i(
            "re",
            "Start: ${r.startTime.toLocalDateTime()} Koniec: ${r.endTime.toLocalDateTime()} Koniec Cyklu: ${r.recurrencePattern?.endDate?.toLocalDateTime()}"
        )
    }


}

fun generateTestRecurringReservation(): Reservation {


    val today = LocalDate.now()

    val startTime = LocalTime.of(14, 0)
    val endTime = LocalTime.of(15, 30)


    val startDateTime = LocalDateTime.of(today, startTime)
    val endDateTime = LocalDateTime.of(today, endTime)



    return Reservation(
        roomId = "TestRoom01",
        userId = "TestUser01",
        createdAt = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli(),
        startTime = startDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
        endTime = endDateTime.toInstant(ZoneOffset.UTC).toEpochMilli(),
        dayOfWeek = startDateTime.dayOfWeek,
        participants = 10,
        status = ReservationStatus.CONFIRMED,
        isRecurring = true,
        recurrencePattern = RecurrencePattern(
            frequency = RecurrenceFrequency.WEEKLY,
            endDate = startDateTime.plusYears(1).toInstant(ZoneOffset.UTC).toEpochMilli()
        )
    )
}
