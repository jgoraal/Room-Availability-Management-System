package com.example.apptemplates.data

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val lastSeen: Long = 0L,
    val isVerified: Boolean = false,
)

