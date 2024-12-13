package com.example.apptemplates.data.user.permission

sealed class UserRole(val permissions: List<Permission>) {

    // Guest role
    data object Guest : UserRole(
        listOf(
            Permission.CannotReserve, // Done
        )
    )


    // Student role
    data object Student : UserRole(
        listOf(
            Permission.CanReserveRoom(
                maxReservationCount = 3,
                allowedReservationTypes = listOf(
                    ReservationType.STANDARD,
                )
            ), // Done
            Permission.MaxReservationTime(1), // 1 Unit stands for 1 hour 30 min = 90 min Done
            Permission.CanCancelReservation,
            Permission.CanSeeRoomAvailability,
            Permission.MaxAdvanceBookingPeriod(28), // 2 weeks advance booking
            Permission.MaxAttendeeCount(20),
            Permission.RequiresAdminApproval // Done
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
            Permission.CanMakeRecurringReservation, // Done
            Permission.CanReserveImmediately, // Done
            Permission.CanCancelReservation,
            Permission.CanRequestAdditionalEquipment,
            Permission.CanSeeRoomAvailability,
            Permission.MaxAttendeeCount(150),
            Permission.MaxAdvanceBookingPeriod(56) // 2 month advance booking
        )
    )


    // Admin role
    data object Admin : UserRole(
        listOf(
            Permission.CanApproveReservation,
        )
    )
}

