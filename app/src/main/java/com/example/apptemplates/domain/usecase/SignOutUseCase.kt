package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.firebase.auth.FirebaseAuthManager

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
