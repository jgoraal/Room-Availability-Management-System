package com.example.apptemplates.presentation.screens.home.reservation.gen

import com.example.apptemplates.domain.model.Lesson
import com.example.apptemplates.domain.model.RecurrenceFrequency
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset


fun generateRealisticLessonsForRooms(roomIds: List<String>, userIds: List<String>): List<Lesson> {
    val lessons = mutableListOf<Lesson>()
    val lessonDuration = 1.5
    val lessonNames = listOf(
        "Podstawy Teleinformatyki",
        "Systemy Operacyjne",
        "Programowanie w Pythonie",
        "Algorytmy i Struktury Danych",
        "Sieci Komputerowe",
        "Bezpieczeństwo Informacji",
        "Bazy Danych",
        "Inżynieria Oprogramowania",
        "Technika Cyfrowa",
        "Grafika Komputerowa",
    )

    for (roomId in roomIds) {
        for (day in DayOfWeek.entries) {

            val numLessons = (2..5).random()


            var availableStartTimes = listOf(
                LocalTime.of(8, 0),
                LocalTime.of(9, 30),
                LocalTime.of(11, 0),
                LocalTime.of(12, 30),
                LocalTime.of(14, 0),
                LocalTime.of(15, 30),
                LocalTime.of(17, 0)
            ).shuffled().take(numLessons)

            availableStartTimes = availableStartTimes.sorted()

            for (startTime in availableStartTimes) {
                val lessonStart = LocalDateTime.of(LocalDate.now().with(day), startTime)


                val lessonEnd = lessonStart.plusMinutes((lessonDuration * 60).toLong())


                if (lessonEnd.toLocalTime().isAfter(LocalTime.of(20, 0))) break

                lessons.add(
                    Lesson(
                        day = lessonStart.dayOfWeek,
                        roomId = roomId,
                        name = lessonNames.random(),
                        userId = userIds.random(),
                        lessonStart = lessonStart.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        lessonEnd = lessonEnd.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        lessonEndDate = lessonStart.plusWeeks(24).toInstant(ZoneOffset.UTC)
                            .toEpochMilli(),
                        frequency = listOf(
                            RecurrenceFrequency.WEEKLY, RecurrenceFrequency.BIWEEKLY
                        ).random()
                    )
                )
            }
        }
    }

    return lessons
}

