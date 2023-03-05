package com.example.plogging.data.source

import com.example.plogging.data.source.remote.PreferenceManager
import kotlinx.coroutines.flow.first

class PreferenceRepository(
    private val preferenceManager: PreferenceManager
) {
    suspend fun getUserName(): String = preferenceManager.userNameFlow.first()
    suspend fun getUserProfileUrl(): String = preferenceManager.userPhotoFlow.first()
    suspend fun getUserEmail(): String = preferenceManager.userEmailFlow.first()
    suspend fun logout() = preferenceManager.deleteUserInfo()
}