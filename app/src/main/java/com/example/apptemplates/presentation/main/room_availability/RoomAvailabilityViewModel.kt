package com.example.apptemplates.presentation.main.room_availability

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.room_availability.domain.RoomFetchRepository
import com.example.apptemplates.viewmodel.MainViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class RoomAvailabilityViewModel(
    private val roomFetchRepository: RoomFetchRepository = RoomFetchRepository(),
) : MainViewModel() {


    fun fetchRooms() {
        viewModelScope.launch {
            wrapWithLoadingState(
                successState = { updateRoomsState(it) },
                errorState = { updateErrorState(it) },
                block = {
                    roomFetchRepository.invoke(_state.value.selectedFloorNumber)
                }
            )
        }
    }

    fun checkAvailability() {
        viewModelScope.launch {
            wrapWithLoadingState(
                successState = { updateLessonsState(it) },
                errorState = { updateErrorState(it) },
                block = {
                    val roomId = _state.value.selectedRoom?.id
                        ?: throw IllegalArgumentException("No room selected")
                    val dayOfWeek = _state.value.selectedDate?.dayOfWeek
                        ?: throw IllegalArgumentException("No date selected")
                    val time = _state.value.selectedDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)
                        ?.toEpochMilli()
                        ?: throw IllegalArgumentException("No date selected")

                    val endTime =
                        _state.value.selectedDate?.atTime(23, 59)?.toInstant(ZoneOffset.UTC)
                            ?.toEpochMilli()
                            ?: throw IllegalArgumentException("No date selected")

                    val lessons = roomFetchRepository(roomId, dayOfWeek, time)
                        .map { lesson -> lesson.lessonStart to lesson.lessonEnd }

                    val nonRecurringReservations =
                        roomFetchRepository(dayOfWeek, roomId, time, endTime)
                            .map { reservation -> reservation.startTime to reservation.endTime }

                    val recurringReservations = roomFetchRepository(time, dayOfWeek, roomId)
                        .map { reservation -> reservation.startTime to reservation.endTime }


                    val roomTimeTable = lessons + nonRecurringReservations + recurringReservations
                        .sortedBy { it.first }


                    Log.i("dd", roomTimeTable.toString())

                    getAvailableTimes(roomTimeTable)


                }
            )
        }
    }

    fun canSeeRoomAvailability(): Boolean {
        return permission.canSeeRoomAvailability()
    }


    private fun getAvailableTimes(reservedTimeTable: List<Pair<Long, Long>>): List<Triple<Long, Long, Boolean>> {
        val fullDaySlots = generateFullDaySlots()

        val timeSlots = fullDaySlots.map { slot ->
            val isReserved =
                reservedTimeTable.any { it.first.isBetween(it.second, slot.first, slot.second) }
            Triple(slot.first, slot.second, isReserved)
        }

        return timeSlots
    }

    private fun generateFullDaySlots(): List<Pair<Long, Long>> {
        if (_state.value.selectedDate == null) return emptyList()

        val startHour = 7 // Start time
        val endHour = 22 // End time
        val intervalMinutes = 90 // Slot duration in minutes
        val slots = mutableListOf<Pair<Long, Long>>()

        var currentDateTime = _state.value.selectedDate!!.atTimeAndZone(startHour, 0, 0)

        while (currentDateTime.hour < endHour) {
            val startTime = currentDateTime.toMillis()
            currentDateTime = currentDateTime.plusMinutes(intervalMinutes.toLong())
            val endTime = currentDateTime.toMillis()
            slots.add(startTime to endTime)
        }
        return slots
    }

    private fun LocalDateTime.toMillis(): Long {
        return this.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    private fun LocalDate.atTimeAndZone(hour: Int, minute: Int, second: Int): LocalDateTime {
        val currentMillis = this.atTime(hour, minute, second).toInstant(
            ZoneOffset.UTC
        ).toEpochMilli()

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentMillis), ZoneOffset.UTC)
    }

    private fun Long.isBetween(
        reservedEnd: Long,
        availableStart: Long,
        availableEnd: Long
    ): Boolean {

        val reservedStartTime = this.convertToLocalDateTime()
        val reservedEndTime = reservedEnd.convertToLocalDateTime()

        val availableStartTime = availableStart.convertToLocalDateTime()
        val availableEndTime = availableEnd.convertToLocalDateTime()


        return (reservedStartTime.isBefore(availableEndTime) && reservedEndTime.isAfter(
            availableStartTime
        ))
    }

    private fun Long.convertToLocalDateTime(): LocalTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(this),
            ZoneOffset.UTC
        ).toLocalTime()
    }


    fun changeDate(date: LocalDate) {
        _state.value = _state.value.copy(selectedDate = date)
    }

    fun changeFloor(floor: Int?) {
        _state.value = _state.value.copy(selectedFloorNumber = floor)
    }

    fun changeRoom(room: Room?) {
        _state.update {
            it.copy(selectedRoom = room)
        }
    }

    private fun updateRoomsState(rooms: List<Room>) {
        _state.update {
            it.copy(screenState = ScreenState.Success, rooms = rooms)
        }
    }

    private fun updateErrorState(message: String) {
        _state.update {
            it.copy(screenState = ScreenState.Error(UiError.DatabaseError(message)))
        }
    }

    private fun updateLessonsState(times: List<Triple<Long, Long, Boolean>>) {
        _state.update {
            it.copy(screenState = ScreenState.Success, times = times)
        }
    }

}

/*data class RoomAvailabilityState(
    val selectedFloor: String = "Parter",
    val selectedRoom: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val reservations: List<Reservation> = emptyList(),
    val screenState: ScreenState = ScreenState.Idle
)*/
