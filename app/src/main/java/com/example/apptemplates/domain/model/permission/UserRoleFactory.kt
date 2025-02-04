package com.example.apptemplates.data.model.model.user.permission

import com.example.apptemplates.data.model.model.user.Role

object UserRoleFactory {
    fun createUserRole(role: Role): UserRole {
        return when (role) {
            Role.ADMIN -> UserRole.Admin
            Role.STUDENT -> UserRole.Student
            Role.EMPLOYEE -> UserRole.Employee
            Role.GUEST -> UserRole.Guest
        }
    }
}