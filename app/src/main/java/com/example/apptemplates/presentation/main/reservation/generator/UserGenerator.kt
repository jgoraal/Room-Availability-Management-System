package com.example.apptemplates.presentation.main.reservation.generator

import com.example.apptemplates.data.user.Role
import com.example.apptemplates.data.user.User
import java.time.Instant
import java.util.UUID

fun generateRandomUsers(numberOfUsers: Int): List<User> {
    val firstNames =
        listOf("Anna", "Adam", "Jakub", "Marta", "Kamil", "Paulina", "Michał", "Piotr", "Maciek")
    val lastNames =
        listOf("Kowalski", "Nowak", "Wróbel", "Zając", "Lis", "Duda", "Mazur", "Urbański", "Górski")

    return List(numberOfUsers) {
        val firstName = firstNames.random()
        val lastName = lastNames.random()
        val role = listOf(Role.STUDENT, Role.EMPLOYEE, Role.GUEST).random()
        val verified = role != Role.GUEST

        User(
            uid = UUID.randomUUID().toString(),
            username = "$firstName $lastName",
            email = generateEmail(firstName.lowercase(), lastName.lowercase(), role),
            lastSeen = Instant.now().epochSecond,
            verified = verified,
            role = role
        )
    }
}


private fun generateEmail(first: String, second: String, role: Role): String {
    val domain = if (role == Role.EMPLOYEE) "put.poznan.pl" else "student.put.poznan.pl"
    return "$first.$second@$domain"
}

fun List<User>.getUserIds(): List<String> {
    return this.map { it.uid }
}