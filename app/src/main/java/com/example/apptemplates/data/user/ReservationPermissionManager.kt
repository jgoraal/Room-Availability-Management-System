package com.example.apptemplates.data.user

import com.example.apptemplates.data.user.permission.Permission
import com.example.apptemplates.data.user.permission.ReservationType
import com.example.apptemplates.data.user.permission.UserRole
import com.example.apptemplates.data.user.permission.UserRoleFactory
import com.example.apptemplates.presentation.main.home.ActiveReservations

class ReservationPermissionManager {

    private val userRole: UserRole =
        UserRoleFactory.createUserRole(ActiveUser.getUser()?.role ?: Role.GUEST)

    // Helper function to extract specific permission in constant time
    private inline fun <reified T : Permission> UserRole.getPermission(): T? {
        return permissions.filterIsInstance<T>().firstOrNull()
    }

    private fun UserRole.hasPermission(permission: Permission): Boolean {
        return permissions.contains(permission)
    }

    private fun hasAdminPermission(): Boolean {
        return userRole.hasPermission(Permission.CanApproveReservation)
    }

    fun canReserve(isRecurring: Boolean): Boolean {
        if (hasAdminPermission()) return true

        val canReservePermission =
            userRole.getPermission<Permission.CanReserveRoom>() ?: return false

        val maxReservationCount = canReservePermission.maxReservationCount
        val activeUserReservations = ActiveReservations.getReservations().value.size

        val allowedReservationTypes = canReservePermission.allowedReservationTypes
        val reservationType =
            if (isRecurring) ReservationType.RECURRING else ReservationType.STANDARD

        return (activeUserReservations + 1) <= maxReservationCount &&
                allowedReservationTypes.isNotEmpty() &&
                allowedReservationTypes.contains(reservationType)
    }

    fun hasExceededMaxReservationTime(currentTimePeriod: Int): Boolean {
        if (hasAdminPermission()) return true


        val maxReservationTime =
            userRole.getPermission<Permission.MaxReservationTime>()?.timeInTimeUnits ?: return false

        return when (maxReservationTime) {
            1 -> {
                currentTimePeriod == 90
            }

            else -> {
                currentTimePeriod % 90 == 0
            }
        }
    }

    fun canMakeRecurringReservation(): Boolean {
        if (hasAdminPermission()) return true

        return userRole.hasPermission(Permission.CanMakeRecurringReservation)
    }

    fun checkMaxAttendeeCount(attendees: Int): Boolean {
        if (hasAdminPermission()) return true

        val maxAttendees =
            userRole.getPermission<Permission.MaxAttendeeCount>()?.count ?: return false

        return attendees <= maxAttendees
    }

    fun getMaximumAttendees(): Int {
        if (hasAdminPermission()) return 150

        return userRole.getPermission<Permission.MaxAttendeeCount>()?.count ?: 0
    }

    fun getMaxReservationCount(): Int {
        if (hasAdminPermission()) return 150

        return userRole.getPermission<Permission.CanReserveRoom>()?.maxReservationCount ?: 0
    }

    fun canSeeRoomAvailability(): Boolean {
        if (hasAdminPermission()) return true

        return userRole.hasPermission(Permission.CanSeeRoomAvailability)
    }


    fun requiresAdminApproval(): Boolean {
        if (hasAdminPermission()) return false  // CONFIRMED

        return when {
            userRole.hasPermission(Permission.RequiresAdminApproval) -> true // PENDING
            userRole.hasPermission(Permission.CanReserveImmediately) -> {
                false
                /*val canReservePermission =
                    userRole.getPermission<Permission.CanReserveRoom>()
                        ?: throw Exception("Permission not found")

                val maxReservationCount = canReservePermission.maxReservationCount
                val activeUserReservations = ActiveReservations.getReservations().value.size

                activeUserReservations >= maxReservationCount*/
            }

            else -> throw Exception("Permission not found")
        }
    }

    fun cannotReserve(): Boolean {
        if (hasAdminPermission()) return false

        return userRole.hasPermission(Permission.CannotReserve)
    }


    // New function that centralizes user role creation and multiple permission checks
    /*fun checkUserPermissions(
        currentReservationCount: Int,
        reservationType: ReservationType
    ): Boolean {

        if (hasExceededMaxReservationCount(currentReservationCount)) return false
        if (!canReserveType(reservationType)) return false

        return true
    }*/
}

