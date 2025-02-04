package com.example.apptemplates.domain.usecase

import com.example.apptemplates.data.model.model.user.User
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
    fun isUserVerified(): Boolean {
        return _userState.value?.verified ?: false
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
        _userState.value = _userState.value?.copy(verified = isVerified)
    }

    fun updateProfileImageUrl(newUrl: String) {
        _userState.value = _userState.value?.copy(profileImageUrl = newUrl)
    }


    // Clear the current user
    fun clearUser() {
        _userState.value = null
    }


}