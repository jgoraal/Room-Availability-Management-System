package com.example.apptemplates.data.user

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val lastSeen: Long = 0L,
    val created: String = UserCreatedDate.getInstance().getFormattedCreatedDate(),
    val verified: Boolean = false,
    val role: Role = Role.GUEST,
    val profileImageUrl: String = ""
)


enum class Role {
    ADMIN, STUDENT, EMPLOYEE, GUEST
}


class UserCreatedDate {

    private var createdDate: Long = System.currentTimeMillis()

    // Zwraca datę w formacie "dd/MM/yyyy HH:mm"
    fun getFormattedCreatedDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date(createdDate))
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

