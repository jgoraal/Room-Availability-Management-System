package com.example.apptemplates.data.user

import com.example.apptemplates.data.user.permission.Permission
import com.example.apptemplates.data.user.permission.ReservationType
import com.example.apptemplates.data.user.permission.UserRole
import com.example.apptemplates.data.user.permission.UserRoleFactory
import com.example.apptemplates.data.user.permission.hasPermission

class ReservationPermissionManager {

    // Helper function to extract specific permission in constant time
    private inline fun <reified T : Permission> UserRole.getPermission(): T? {
        return permissions.filterIsInstance<T>().firstOrNull()
    }

    fun canReserveType(userRole: UserRole, reservationType: ReservationType): Boolean {
        val canReservePermission = userRole.getPermission<Permission.CanReserveRoom>()
        return canReservePermission?.allowedReservationTypes?.contains(reservationType) ?: false
    }

    fun getMaxReservationTime(userRole: UserRole): Int {
        return userRole.getPermission<Permission.MaxReservationTime>()?.timeInTimeUnits ?: 0
    }

    fun canMakeRecurringReservation(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.CanMakeRecurringReservation)
    }

    fun canApproveReservation(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.CanApproveReservation)
    }

    fun hasExceededMaxReservationCount(userRole: UserRole, currentReservationCount: Int): Boolean {
        val canReservePermission = userRole.getPermission<Permission.CanReserveRoom>()
        return canReservePermission?.maxReservationCount?.let { currentReservationCount >= it }
            ?: true
    }

    fun canReserveImmediately(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.CanReserveImmediately)
    }

    fun canCancelReservation(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.CanCancelReservation)
    }

    fun canViewAllReservations(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.CanViewAllReservations)
    }


    fun canRequestAdditionalEquipment(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.CanRequestAdditionalEquipment)
    }

    fun canSeeRoomAvailability(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.CanSeeRoomAvailability)
    }

    fun getMaxAdvanceBookingPeriod(userRole: UserRole): Int {
        return userRole.getPermission<Permission.MaxAdvanceBookingPeriod>()?.days ?: 0
    }

    fun hasExceededMaxAdvanceBookingPeriod(userRole: UserRole, daysSinceReservation: Int): Boolean {
        val maxAdvanceBookingPeriod = getMaxAdvanceBookingPeriod(userRole)
        return maxAdvanceBookingPeriod in 1..<daysSinceReservation
    }


    fun requiresAdminApproval(userRole: UserRole): Boolean {
        return userRole.hasPermission(Permission.RequiresAdminApproval)
    }


    // New function that centralizes user role creation and multiple permission checks
    fun checkUserPermissions(
        user: User,
        currentReservationCount: Int,
        reservationType: ReservationType
    ): Boolean {
        val userRole = UserRoleFactory.createUserRole(user.role)

        if (hasExceededMaxReservationCount(userRole, currentReservationCount)) return false
        if (!canReserveType(userRole, reservationType)) return false

        return true
    }
}

