package com.example.plogging.data.source.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.plogging.data.source.remote.PreferenceManager.UserPreferenceKeys.USER_EMAIL
import com.example.plogging.data.source.remote.PreferenceManager.UserPreferenceKeys.USER_IMAGE
import com.example.plogging.data.source.remote.PreferenceManager.UserPreferenceKeys.USER_LOGIN_STATE
import com.example.plogging.data.source.remote.PreferenceManager.UserPreferenceKeys.USER_NAME
import com.example.plogging.data.source.remote.PreferenceManager.UserPreferenceKeys.USER_TOKEN
import com.example.plogging.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.USER_PREFERENCES_NAME)

    private object UserPreferenceKeys {
        val USER_LOGIN_STATE = booleanPreferencesKey(Constants.USER_PREFERENCES_IS_LOGIN)
        val USER_NAME = stringPreferencesKey(Constants.USER_PREFERENCES_NAME)
        val USER_TOKEN = stringPreferencesKey(Constants.USER_PREFERENCES_TOKEN)
        val USER_IMAGE = stringPreferencesKey(Constants.USER_PREFERENCES_IMAGE)
        val USER_EMAIL = stringPreferencesKey(Constants.USER_PREFERENCES_EMAIL)
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map {
        it[USER_NAME] ?: ""
    }
    val userTokenFlow: Flow<String> = context.dataStore.data.map {
        it[USER_TOKEN] ?: ""
    }
    val userPhotoFlow: Flow<String> = context.dataStore.data.map {
        it[USER_IMAGE] ?: ""
    }
    val userEmailFlow: Flow<String> = context.dataStore.data.map {
        it[USER_EMAIL] ?: ""
    }

    val isLogin: Flow<Boolean> = context.dataStore.data.map {
        it[USER_LOGIN_STATE] ?: false
    }

    suspend fun setUserInfo(
        token: String,
        name: String,
        image: String,
        email: String,
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
            preferences[USER_NAME] = name
            preferences[USER_IMAGE] = image
            preferences[USER_EMAIL] = email
            preferences[USER_LOGIN_STATE] = true
        }
    }

    suspend fun editUserName(changedName: String) {
        context.dataStore.edit {
            it[USER_NAME] = changedName
        }
    }

    suspend fun editUserImage(image: String) {
        context.dataStore.edit {
            it[USER_IMAGE] = image
        }
    }

    suspend fun deleteUserInfo() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}