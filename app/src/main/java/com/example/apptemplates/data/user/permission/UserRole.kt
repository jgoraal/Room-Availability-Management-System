package com.example.apptemplates.data.user.permission

sealed class UserRole(val permissions: List<Permission>) {

    // Default role
    data object Guest : UserRole(
        listOf(
            Permission.CannotReserve,
            Permission.CanSeeRoomAvailability
        )
    )


    // Student role
    data object Student : UserRole(
        listOf(
            Permission.CanReserveRoom(
                maxReservationCount = 1,
                allowedReservationTypes = listOf(
                    ReservationType.STANDARD,
                    ReservationType.PROVISIONAL
                )
            ),
            Permission.MaxReservationTime(1),
            Permission.CanCancelReservation,
            Permission.CanSeeRoomAvailability,
            Permission.MaxAdvanceBookingPeriod(14), // 2 weeks advance booking
            Permission.RequiresAdminApproval
        )
    )

    data object Employee : UserRole(
        listOf(
            Permission.CanReserveRoom(
                maxReservationCount = 3,
                allowedReservationTypes = listOf(
                    ReservationType.STANDARD,
                    ReservationType.RECURRING,
                    ReservationType.URGENT
                )
            ),
            Permission.MaxReservationTime(2),
            Permission.CanApproveReservation,
            Permission.CanReserveImmediately,
            Permission.CanCancelReservation,
            Permission.CanViewAllReservations,
            Permission.CanRequestAdditionalEquipment,
            Permission.CanSeeRoomAvailability,
            Permission.MaxAdvanceBookingPeriod(30) // 1 month advance booking
        )
    )


    // Admin role
    data object Admin : UserRole(
        listOf(
            Permission.CanApproveReservation
        )
    )
}

fun UserRole.hasPermission(permission: Permission): Boolean {
    return permissions.contains(permission)
}