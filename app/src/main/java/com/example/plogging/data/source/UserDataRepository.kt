package com.example.plogging.data.source

import com.example.plogging.data.source.remote.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserDataRepository(
    private val preferenceManager: PreferenceManager
) {
    suspend fun setUserInfo(
        token: String,
        name: String,
        photo: String,
        email: String,
    ) {
        preferenceManager.setUserInfo(token, name, photo, email)
    }

    suspend fun getUserLoginState(): Boolean {
        return preferenceManager.isLogin.first()
    }

    fun getUserName(): Flow<String> = preferenceManager.userNameFlow
    fun getUserProfileUrl(): Flow<String> = preferenceManager.userPhotoFlow
    fun getUserEmail(): Flow<String> = preferenceManager.userEmailFlow
    suspend fun logout() = preferenceManager.deleteUserInfo()
    suspend fun changeUserName(changedName: String) = preferenceManager.editUserName(changedName)
    suspend fun changeUserProfileImage(changedImage: String) = preferenceManager.editUserImage(changedImage)
}