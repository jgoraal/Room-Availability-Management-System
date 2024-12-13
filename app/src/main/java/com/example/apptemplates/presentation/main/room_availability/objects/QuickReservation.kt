package com.example.apptemplates.presentation.main.room_availability.objects

import com.example.apptemplates.data.room.Room
import com.example.apptemplates.presentation.main.room_availability.TimeSlot
import com.example.apptemplates.presentation.main.temp.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

object QuickReservation {

    private val selectedDate = MutableStateFlow<LocalDate?>(null)
    private val startTime = MutableStateFlow<LocalTime?>(null)
    private val endTime = MutableStateFlow<LocalTime?>(null)
    private val selectedRoom = MutableStateFlow<Room?>(null)


    fun copy(state: MainUiState, timeSlot: TimeSlot) {
        selectedDate.value = state.selectedDate
        startTime.value =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timeSlot.startTime), ZoneOffset.UTC)
                .toLocalTime()
        endTime.value =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timeSlot.endTime), ZoneOffset.UTC)
                .toLocalTime()
        selectedRoom.value = state.selectedRoom
    }


    fun clear() {
        selectedDate.value = null
        startTime.value = null
        endTime.value = null
    }

    fun getSelectedDate(): LocalDate? {
        return selectedDate.value
    }

    fun getStartTime(): LocalTime? {
        return startTime.value
    }

    fun getEndTime(): LocalTime? {
        return endTime.value
    }

    fun getSelectedRoom(): Room? {
        return selectedRoom.value
    }

}