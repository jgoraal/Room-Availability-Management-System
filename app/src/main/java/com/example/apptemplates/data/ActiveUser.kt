package com.example.apptemplates.data

import kotlinx.coroutines.flow.MutableStateFlow


object ActiveUser {

    private val _userState = MutableStateFlow<User?>(null)

    // Get the current user
    fun getUser(): User? {
        return _userState.value
    }

    // Set or update the current user
    fun setUser(newUser: User) {
        _userState.value = newUser
    }

    // Check if a user is currently set
    fun isUserLoggedIn(): Boolean {
        return _userState.value?.isVerified ?: false
    }

    // Get the UID of the current user
    fun getUid(): String? {
        return _userState.value?.uid
    }


    // Update the uid of the current user
    fun updateUid(newUid: String) {
        _userState.value = _userState.value?.copy(uid = newUid)
    }

    // Update the email of the current user
    fun updateEmail(newEmail: String) {
        _userState.value = _userState.value?.copy(email = newEmail)
    }

    // Update the password of the current user
    fun updatePassword(newPassword: String) {
        _userState.value = _userState.value?.copy(password = newPassword)
    }

    // Update the username of the current user
    fun updateUsername(newUsername: String) {
        _userState.value = _userState.value?.copy(username = newUsername)
    }

    // Update the lastSeen time of the current user
    fun updateLastSeen(newLastSeen: Long) {
        _userState.value = _userState.value?.copy(lastSeen = newLastSeen)
    }

    // Update the verification status of the current user
    fun updateIsVerified(isVerified: Boolean) {
        _userState.value = _userState.value?.copy(isVerified = isVerified)
    }


    // Clear the current user
    fun clearUser() {
        _userState.value = null
    }


}