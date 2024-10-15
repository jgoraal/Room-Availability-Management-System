package com.example.apptemplates.data.room

data class Room(
    val id: String,
    val name: String,
    /*    val location: String,*/
    val capacity: Int,
    /*    val features: List<String>,*/
    val availability: Boolean = true,
    val isReservable: Boolean = true,
    /*  val schedule: Map<String, List<Reservation>> = emptyMap(),
      val floor: Int? = null,
      val equipment: List<String>? = null,
      val contactPerson: String? = null,
      val notes: String? = null*/
)

