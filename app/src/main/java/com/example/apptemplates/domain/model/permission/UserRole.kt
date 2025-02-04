package com.example.apptemplates.data.model.model.user.permission

sealed class UserRole(val permissions: List<Permission>) {

    data object Student : UserRole(
        listOf(
            // Uprawnienia dotyczące rezerwacji
            Permission.CanReserveRoom(
                maxReservationCount = 3,
                allowedReservationTypes = listOf(
                    ReservationType.STANDARD
                )
            ),
            // Maksymalny czas rezerwacji: 1 jednostka = 90 minut
            Permission.MaxReservationTime(1),
            // Maksymalny okres rezerwacji z wyprzedzeniem: 28 dni
            Permission.MaxAdvanceBookingPeriod(28),
            // Maksymalna liczba uczestników: 20
            Permission.MaxAttendeeCount(20),

            // Uprawnienia dotyczące zarządzania rezerwacjami
            Permission.CanCancelReservation,
            Permission.CanSeeRoomAvailability,

            // Wymagane zatwierdzenie przez administratora
            Permission.RequiresAdminApproval
        )
    )


    // Employee role
    data object Employee : UserRole(
        listOf(
            Permission.CanReserveRoom(
                maxReservationCount = 100,
                allowedReservationTypes = listOf(
                    ReservationType.STANDARD,
                    ReservationType.RECURRING,
                )
            ),
            Permission.MaxReservationTime(2),
            Permission.CanMakeRecurringReservation,
            Permission.CanReserveImmediately,
            Permission.CanCancelReservation,
            Permission.CanRequestAdditionalEquipment,
            Permission.CanSeeRoomAvailability,
            Permission.MaxAttendeeCount(150),
            Permission.MaxAdvanceBookingPeriod(56)
        )
    )


    // Guest role
    data object Guest : UserRole(
        listOf(
            Permission.CannotReserve,
        )
    )


    // Admin role
    data object Admin : UserRole(
        listOf(
            Permission.CanApproveReservation,
        )
    )
}

