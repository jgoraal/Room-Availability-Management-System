package com.example.apptemplates.data.user.permission

sealed class Permission {
    // Permission to approve reservations
    data object CanApproveReservation : Permission()

    // Permission for employee to reserve without admin approval
    data object CanReserveImmediately : Permission()

    // Permission for employee to make recurring reservations
    data object CanMakeRecurringReservation : Permission()

    // Permission to block making reservations
    data object CannotReserve : Permission()

    // Permission to cancel reservations
    data object CanCancelReservation : Permission()

    // Permission to view all reservations
    data object CanViewAllReservations : Permission()

    // Permission to request additional equipment
    data object CanRequestAdditionalEquipment : Permission()

    // Permission to see room availability
    data object CanSeeRoomAvailability : Permission()

    // Permission to reserve room
    data class CanReserveRoom(
        val maxReservationCount: Int,
        val allowedReservationTypes: List<ReservationType>
    ) : Permission()

    // Permission to set max reservation time
    data class MaxReservationTime(val timeInTimeUnits: Int) : Permission()

    // Permission to set max advance booking period
    data class MaxAdvanceBookingPeriod(val days: Int) : Permission()

    data class MaxAttendeeCount(val count: Int) : Permission()

    // Permission to require admin approval
    data object RequiresAdminApproval : Permission()
}

enum class ReservationType {
    STANDARD, RECURRING
}
