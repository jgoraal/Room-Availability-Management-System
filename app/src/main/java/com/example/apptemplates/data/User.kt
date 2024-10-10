package com.example.apptemplates.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val lastSeen: Long = 0L,
    val created: String = UserCreatedDate.getInstance().getFormattedCreatedDate(),
    val isVerified: Boolean = false,
)


class UserCreatedDate {

    private var createdDate: Long = System.currentTimeMillis()

    // Zwraca datę w formacie "dd/MM/yyyy HH:mm"
    fun getFormattedCreatedDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date(createdDate))
    }

    // Ustawia datę ręcznie (timestamp w milisekundach)
    fun setCreatedDate(date: Long) {
        createdDate = date
    }

    // Resetuje datę na bieżący czas
    fun resetCreatedDate() {
        createdDate = System.currentTimeMillis()
    }

    // Pobiera datę w formie timestampu
    fun getCreatedDate(): Long {
        return createdDate
    }

    companion object {
        private var instance: UserCreatedDate? = null

        // Zwraca singleton klasy UserCreatedDate
        fun getInstance(): UserCreatedDate {
            if (instance == null) {
                instance = UserCreatedDate()
            }
            return instance!!
        }
    }
}

