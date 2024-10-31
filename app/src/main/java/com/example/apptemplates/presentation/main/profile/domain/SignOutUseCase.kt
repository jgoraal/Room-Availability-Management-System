package com.example.apptemplates.presentation.main.profile.domain

import com.example.apptemplates.firebase.auth.FirebaseAuthManager

class SignOutUseCase {

    operator fun invoke(): Boolean {
        return try {
            FirebaseAuthManager.signOut()
            true
        } catch (e: Exception) {
            false
        }
    }

}
