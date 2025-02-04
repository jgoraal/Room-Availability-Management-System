package com.example.apptemplates.domain.usecase

import android.util.Log
import com.example.apptemplates.data.firebase.database.LessonsRepositoryImpl
import com.example.apptemplates.data.firebase.database.result.Result
import com.example.apptemplates.domain.model.Lesson

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
