package com.example.apptemplates.firebase.database

import android.util.Log
import com.example.apptemplates.data.User

suspend fun createUser(user: User) {

    when (val result = Database.addUser(user)) {
        is FirestoreResult.Success -> {
            // Pomyślnie dodano użytkownika
            Log.i("Firestore", "Użytkownik został pomyślnie dodany.")
        }

        is FirestoreResult.Failure -> {
            // Obsługa błędu
            Log.e("Firestore", "Błąd podczas dodawania użytkownika: ${result.exception.message}")
        }

        else -> {
            // Dodatkowe przypadki, jeśli są potrzebne
        }
    }
}


suspend fun fetchUser(uid: String) {
    when (val result = Database.getUser(uid)) {
        is FirestoreResult.SuccessWithResult<*> -> {
            val user = result.data as? User
            user?.let {
                Log.i(
                    "Firestore",
                    "Dane użytkownika: uid:${user.uid}, username: ${user.username}, email: ${user.email}"
                )
            }
        }

        is FirestoreResult.Failure -> {
            // Obsługa błędu
            Log.e("Firestore", "Błąd podczas pobierania użytkownika: ${result.exception.message}")
        }

        else -> {
            // Inne przypadki
        }
    }
}


suspend fun updateUser(updatedUser: User) {

    when (val result = Database.updateUser(updatedUser)) {
        is FirestoreResult.Success -> {
            // Pomyślnie zaktualizowano dane użytkownika
            Log.i("Firestore", "Dane użytkownika zostały zaktualizowane.")
        }

        is FirestoreResult.Failure -> {
            // Obsługa błędu
            Log.e(
                "Firestore",
                "Błąd podczas aktualizowania użytkownika: ${result.exception.message}"
            )
        }

        else -> {
            // Inne przypadki
        }
    }
}

suspend fun deleteUser(email: String) {
    when (val result = Database.deleteUser(email)) {
        is FirestoreResult.Success -> {
            Log.i("Firestore", "Użytkownik został pomyślnie usunięty.")
        }

        is FirestoreResult.Failure -> {
            // Obsługa błędu
            Log.e("Firestore", "Błąd podczas usuwania użytkownika: ${result.exception.message}")
        }

        else -> {
            // Inne przypadki
        }
    }
}


suspend fun isEmailRegistered(email: String): Boolean {
    return when (val result = Database.getUserByEmail(email)) {
        is FirestoreResult.SuccessWithResult<*> -> {
            val isRegistered = result.data as? Boolean
            isRegistered ?: false
        }

        is FirestoreResult.Failure -> {
            // Obsługa błędu
            Log.e(
                "Firestore",
                "Błąd podczas sprawdzania czy email jest zarejestrowany: ${result.exception.message}"
            )
            false
        }

        else -> {
            false
        }
    }
}

suspend fun isUsernameRegistered(username: String): Boolean {
    return when (val result = Database.getUserByUsername(username)) {
        is FirestoreResult.SuccessWithResult<*> -> {
            val isRegistered = result.data as? Boolean
            isRegistered ?: false
        }

        is FirestoreResult.Failure -> {
            // Obsługa błędu
            Log.e(
                "Firestore",
                "Błąd podczas sprawdzania czy username jest zarejestrowany: ${result.exception.message}"
            )
            false
        }

        else -> {
            false
        }
    }

}

suspend fun isUidRegistered(uid: String): Boolean {
    return when (val result = Database.getUserByUid(uid)) {
        is FirestoreResult.SuccessWithResult<*> -> {
            val isRegistered = result.data as? Boolean
            isRegistered ?: false
        }

        is FirestoreResult.Failure -> {
            // Obsługa błędu
            Log.e(
                "Firestore",
                "Błąd podczas sprawdzania czy uid jest zarejestrowany: ${result.exception.message}"
            )
            false
        }

        else -> {
            false
        }
    }
}


