package com.example.apptemplates.presentation.main.reservation.domain

import android.util.Log
import com.example.apptemplates.data.room.Lesson
import com.example.apptemplates.firebase.database.LessonsRepositoryImpl
import com.example.apptemplates.result.Result

class AddLessonUseCase {
    suspend operator fun invoke(lessons: List<Lesson>) {

        for (lesson in lessons) {
            when (val result = LessonsRepositoryImpl.addLesson(lesson)) {
                is Result.Success -> continue
                is Result.Error -> throw Exception(result.error)
                else -> return
            }
        }

        Log.i("AddLessonUseCase", "Dodano lekcje!!")

    }
}
