package com.example.apptemplates.presentation.main.reservation.generator

import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.room.Lesson
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset


fun generateRealisticLessonsForRooms(roomIds: List<String>, userIds: List<String>): List<Lesson> {
    val lessons = mutableListOf<Lesson>()
    val lessonDuration = 1.5 // Each lesson lasts 1.5 hours
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
            // Random number of lessons for the day (between 2 and 7)
            val numLessons = (2..5).random()

            // Define start hour ranges for the day, ensuring realistic scheduling
            var availableStartTimes = listOf(
                LocalTime.of(8, 0),  // 8 AM
                LocalTime.of(9, 30), // 9:30 AM
                LocalTime.of(11, 0), // 11:00 AM
                LocalTime.of(12, 30), // 12:30 PM
                LocalTime.of(14, 0),  // 2:00 PM
                LocalTime.of(15, 30), // 3:30 PM
                LocalTime.of(17, 0)   // 5:00 PM
            ).shuffled().take(numLessons) // Shuffle start times and select random slots

            availableStartTimes = availableStartTimes.sorted() // Sort times for the day

            for (startTime in availableStartTimes) {
                val lessonStart = LocalDateTime.of(LocalDate.now().with(day), startTime)

                // Lesson ends after 1.5 hours
                val lessonEnd = lessonStart.plusMinutes((lessonDuration * 60).toLong())

                // Ensure lessons end by 20:00 (8 PM)
                if (lessonEnd.toLocalTime().isAfter(LocalTime.of(20, 0))) break

                lessons.add(
                    Lesson(
                        day = lessonStart.dayOfWeek,
                        roomId = roomId,
                        name = lessonNames.random(), // Random lesson name
                        userId = userIds.random(), // Assign random user ID
                        lessonStart = lessonStart.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        lessonEnd = lessonEnd.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        lessonEndDate = lessonStart.plusWeeks(24).toInstant(ZoneOffset.UTC)
                            .toEpochMilli(), // Recurs for 12 weeks
                        frequency = listOf(
                            RecurrenceFrequency.WEEKLY, RecurrenceFrequency.BIWEEKLY
                        ).random() // Random frequency
                    )
                )
            }
        }
    }

    return lessons
}

