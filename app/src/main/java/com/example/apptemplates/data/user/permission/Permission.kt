package com.example.apptemplates.data.user.permission

sealed class Permission {
    data class CanReserveRoom(
        val maxReservationCount: Int,
        val allowedReservationTypes: List<ReservationType>
    ) : Permission()

    data object CanApproveReservation : Permission()
    data object CanReserveImmediately : Permission()

    data object CanMakeRecurringReservation : Permission()
    data class MaxReservationTime(val timeInTimeUnits: Int) :
        Permission() // 1 unit = 1 hour and 30 minutes

    data object CannotReserve : Permission()

    data object CanCancelReservation : Permission()
    data object CanViewAllReservations : Permission()
    data object CanRequestAdditionalEquipment : Permission()
    data object CanSeeRoomAvailability : Permission()
    data class MaxAdvanceBookingPeriod(val days: Int) : Permission()
    data object RequiresAdminApproval : Permission()
}

enum class ReservationType {
    STANDARD, RECURRING, PROVISIONAL, URGENT, PRIVATE, OPEN_ACCESS, EVENT
}