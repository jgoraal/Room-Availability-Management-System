package com.example.apptemplates.presentation.main.room_availability

import androidx.lifecycle.viewModelScope
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.Lesson
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.presentation.main.room_availability.domain.RoomFetchRepository
import com.example.apptemplates.presentation.main.room_availability.domain.UserUseCase
import com.example.apptemplates.viewmodel.MainViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class RoomAvailabilityViewModel(
    private val roomFetchRepository: RoomFetchRepository = RoomFetchRepository(),
    private val userUseCase: UserUseCase = UserUseCase()
) : MainViewModel() {

    init {

    }


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

        wrapWithLoadingState(
            successState = { updateLessonsState(it) },
            errorState = { updateErrorState(it) },
            block = {
                val roomId = _state.value.selectedRoom?.id
                    ?: throw IllegalArgumentException("Nie wybrano sali")
                val dayOfWeek = _state.value.selectedDate?.dayOfWeek
                    ?: throw IllegalArgumentException("Nie wybrano daty")
                val time = _state.value.selectedDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)
                    ?.toEpochMilli()
                    ?: throw IllegalArgumentException("Nie wybrano daty")

                val endTime =
                    _state.value.selectedDate?.atTime(23, 59)?.toInstant(ZoneOffset.UTC)
                        ?.toEpochMilli()
                        ?: throw IllegalArgumentException("Nie wybrano daty")

                /*roomFetchRepository(roomId, dayOfWeek, time)



                .map {
                    Triple(
                        it.lessonStart to it.lessonEnd,
                        ActiveUser.getUid()!!*//*it.userId*//*,
                                it.name
                            )
                        }*/

                // Reservations
                /*val nonRecurringReservations =
                    roomFetchRepository(dayOfWeek, roomId, time, endTime)
                val recurringReservations = roomFetchRepository(time, dayOfWeek, roomId)

                // Wszystkie rezerwacje
                val allReservations = nonRecurringReservations + recurringReservations

                // Mapowanie do par start-end dla getAvailableTimes
                val reservationsTimePairs = allReservations
                    .map { (it.startTime to it.endTime) }*/

                // Pobierz lekcje i rezerwacje
                val lessons = fetchLessons(roomId, dayOfWeek, time)
                val reservations = fetchReservations(roomId, dayOfWeek, time, endTime)

                // Połącz rezerwacje i lekcje w jeden harmonogram
                val roomTimeTable = buildRoomTimeTable(lessons, reservations)

                // Pobierz unikalne identyfikatory użytkowników
                val userIds =
                    (reservations.map { it.userId } + lessons.map { it.userId }).distinct()
                /*allReservations.map { it.userId } + lessons.map { it.second }
                    .distinctBy { it }*/

                // Pobierz dane użytkowników
                val userMap = fetchUserDetails(userIds)


                // Utwórz listę rezerwacji użytkowników
                val userBookedSlots = buildUserBookedSlots(reservations, userMap)

                // Utwórz listę zajęć
                val lessonBookedSlots = buildLessonBookedSlots(lessons, userMap)


                // Aktualizuj stan
                updateUserReservationTime(userBookedSlots)
                updateLessonBookedSlot(lessonBookedSlots)

                /*// Tworzenie roomTimeTable
                *//*val roomTimeTable =
                    (lessons.map { it.first } + reservationsTimePairs).sortedBy { it.first }*//*

                // Załóżmy, że usernames to List<Pair<String, String>>, gdzie first to userId, second to username
                val users = usernames.flatMap { (userId, username, email) ->
                    // Znajdź wszystkie rezerwacje dla danego userId
                    allReservations.filter { it.userId == userId }
                        .map {
                            UserBookedSlot(
                                userId = userId,
                                username = username,
                                email = email,
                                startTime = it.startTime,
                                endTime = it.endTime
                            )
                        }
                }

                val lessonsBookedSlot = usernames.flatMap { (userId, _, email) ->
                    lessons.filter { it.second == userId }
                        .map { r ->
                            LessonBookedSlot(
                                userId = userId,
                                email = email,
                                lessonName = r.third,
                                startTime = r.first.first,
                                endTime = r.first.second
                            )
                        }
                }*/
                /*

                                Log.i(
                                    "Room Time Table",
                                    roomTimeTable.joinToString(separator = "\n") {
                                        "${it.first.convertToLocalTime()} - ${it.second.convertToLocalTime()}"
                                    }
                                )

                                Log.i(
                                    "User Book Time Table",
                                    users.joinToString(separator = "\n") {
                                        "${it.startTime.convertToLocalTime()} - ${it.endTime.convertToLocalTime()}"
                                    }
                                )*/

                getAvailableTimes(roomTimeTable)


            }
        )

    }

    private suspend fun fetchLessons(
        roomId: String,
        dayOfWeek: DayOfWeek,
        time: Long
    ): List<Lesson> {
        return roomFetchRepository(roomId, dayOfWeek, time)
    }

    private suspend fun fetchReservations(
        roomId: String,
        dayOfWeek: DayOfWeek,
        time: Long,
        endTime: Long
    ): List<Reservation> {
        val nonRecurringReservations =
            roomFetchRepository(dayOfWeek, roomId, time, endTime)
        val recurringReservations = roomFetchRepository(time, dayOfWeek, roomId)

        // Wszystkie rezerwacje
        return (nonRecurringReservations + recurringReservations)
            .sortedBy { it.startTime }
    }

    private fun buildRoomTimeTable(
        lessons: List<Lesson>,
        reservations: List<Reservation>
    ): List<TimeSlot> {
        val lessonSlots = lessons.map { TimeSlot(it.lessonStart, it.lessonEnd) }
        val reservationSlots = reservations.map { TimeSlot(it.startTime, it.endTime) }

        return (lessonSlots + reservationSlots).sortedBy { it.startTime }
    }

    // Funkcja do pobierania szczegółów użytkowników
    private suspend fun fetchUserDetails(userIds: List<String>): Map<String, UserDetails> {
        val userDetailsList = userUseCase(userIds)
        return userDetailsList.associateBy { it.userId }
    }

    // Funkcja do tworzenia listy rezerwacji użytkowników
    private fun buildUserBookedSlots(
        reservations: List<Reservation>,
        userMap: Map<String, UserDetails>
    ): List<UserBookedSlot> {
        return reservations.mapNotNull { reservation ->
            val user = userMap[reservation.userId]
            user?.let {
                UserBookedSlot(
                    userId = it.userId,
                    username = it.username,
                    email = it.email,
                    startTime = reservation.startTime,
                    endTime = reservation.endTime
                )
            }
        }
    }

    // Funkcja do tworzenia listy zajęć
    private fun buildLessonBookedSlots(
        lessons: List<Lesson>,
        userMap: Map<String, UserDetails>
    ): List<LessonBookedSlot> {
        return lessons.mapNotNull { lesson ->
            val user = userMap[lesson.userId]
            user?.let {
                LessonBookedSlot(
                    userId = it.userId,
                    email = it.email,
                    lessonName = lesson.name,
                    startTime = lesson.lessonStart,
                    endTime = lesson.lessonEnd
                )
            }
        }
    }


    fun canSeeRoomAvailability(): Boolean {
        return permission.canSeeRoomAvailability()
    }


    private fun getAvailableTimes(reservedTimeTable: List<TimeSlot>): List<Triple<Long, Long, Boolean>> {
        val fullDaySlots = generateFullDaySlots()

        val timeSlots = fullDaySlots.map { slot ->
            val isReserved =
                reservedTimeTable.any {
                    it.startTime.isBetween(
                        it.endTime,
                        slot.first,
                        slot.second
                    )
                }

            val date = _state.value.selectedDate!!
            val first = slot.first.convertToLocalTime().setNormalTimeSlotFormat()
            val second = slot.second.convertToLocalTime().setNormalTimeSlotFormat()
            Triple(date.atTime(first).toMillis(), date.atTime(second).toMillis(), isReserved)
        }

        return timeSlots
    }

    private fun generateFullDaySlots(): List<Pair<Long, Long>> {
        if (_state.value.selectedDate == null) return emptyList()

        /*val timeSlots = listOf(
            Pair(LocalTime.of(8, 0), LocalTime.of(9, 30)),
            Pair(LocalTime.of(9, 31), LocalTime.of(11, 0)),
            Pair(LocalTime.of(11, 1), LocalTime.of(12, 30)),
            Pair(LocalTime.of(12, 31), LocalTime.of(14, 0)),
            Pair(LocalTime.of(14, 1), LocalTime.of(15, 30)),
            Pair(LocalTime.of(15, 31), LocalTime.of(17, 0)),
            Pair(LocalTime.of(17, 1), LocalTime.of(18, 30)),
            Pair(LocalTime.of(18, 31), LocalTime.of(20, 0)),
            Pair(LocalTime.of(20, 1), LocalTime.of(21, 30)),
            Pair(LocalTime.of(21, 31), LocalTime.of(23, 0)),
        )*/


        val timeSlots = listOf(
            slot(8, 0, 9, 30),      // 8.00 - 9.30
            slot(9, 31, 11, 0),     // 9.31 - 11.00
            slot(11, 1, 12, 30),    // 11.01 - 12.30
            slot(12, 31, 14, 0),    // 12.31 - 14.00
            slot(14, 1, 15, 30),    // 14.01 - 15.30
            slot(15, 31, 17, 0),    // 15.31 - 17.00
            slot(17, 1, 18, 30),    // 17.01 - 18.30
            slot(18, 31, 20, 0),    // 18.31 - 20.00
            slot(20, 1, 21, 30),    // 20.01 - 21.30
            slot(21, 31, 23, 0)     // 21.31 - 23.00
        )

        val date = _state.value.selectedDate!!

        return timeSlots.map { (start, end) ->
            date.atTime(start).toMillis() to date.atTime(end).toMillis()
        }

        /*val startHour = 7 // Start time
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
        return slots*/
    }

    private fun LocalTime.setNormalTimeSlotFormat(): LocalTime {
        return when (this.minute) {
            0 -> this
            1 -> this.minusMinutes(1)
            31 -> this.minusMinutes(1)
            else -> this
        }
    }

    private fun slot(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int
    ): Pair<LocalTime, LocalTime> {
        return LocalTime.of(startHour, startMinute) to LocalTime.of(endHour, endMinute)
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

        val reservedStartTime = this.convertToLocalTime()
        val reservedEndTime = reservedEnd.convertToLocalTime()

        val availableStartTime = availableStart.convertToLocalTime()
        val availableEndTime = availableEnd.convertToLocalTime()


        return (reservedStartTime.isBefore(availableEndTime) && reservedEndTime.isAfter(
            availableStartTime
        ))
    }

    private fun Long.convertToLocalTime(): LocalTime {
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

    private fun updateUserReservationTime(userReservationTime: List<UserBookedSlot>) {
        _state.update {
            it.copy(userBookedSlots = userReservationTime)
        }
    }

    private fun updateLessonBookedSlot(lessonBookedSlot: List<LessonBookedSlot>) {
        _state.update {
            it.copy(lessonBookedSlots = lessonBookedSlot)
        }
    }

    fun updateShowFloorSelector(show: Boolean) {
        _state.update {
            it.copy(showFloorSelector = show)
        }
    }

    fun updateShowRoomSelector(show: Boolean) {
        _state.update {
            it.copy(showRoomSelector = show)
        }
    }

    fun updateShowButton(show: Boolean) {
        _state.update {
            it.copy(isButtonVisible = show)
        }
    }

    fun updateSelectedFloor(floor: String?) {
        _state.update {
            it.copy(selectedFloorName = floor)
        }
    }



    fun clearTimeSlots() {
        _state.update {
            it.copy(times = emptyList())
        }
    }

}

data class TimeSlot(
    val startTime: Long,
    val endTime: Long,
)

data class UserDetails(
    val userId: String,
    val username: String,
    val email: String
)


data class UserBookedSlot(
    val userId: String,
    val email: String,
    val username: String,
    val startTime: Long,
    val endTime: Long
)

data class LessonBookedSlot(
    val userId: String,
    val email: String,
    val lessonName: String,
    val startTime: Long,
    val endTime: Long
)

/*data class RoomAvailabilityState(
    val selectedFloor: String = "Parter",
    val selectedRoom: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val reservations: List<Reservation> = emptyList(),
    val screenState: ScreenState = ScreenState.Idle
)*/
