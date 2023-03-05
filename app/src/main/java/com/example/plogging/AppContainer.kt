package com.example.plogging

import android.app.Application
import com.example.plogging.data.source.remote.ApiClient
import com.example.plogging.data.source.remote.PreferenceManager

class AppContainer(private val application: Application) {

    private var apiClient: ApiClient? = null
    private var dataStore: PreferenceManager? = null

    fun provideApiClient(): ApiClient {
        return apiClient ?: ApiClient.create(BuildConfig.KAKAO_REST_API_KEY).apply {
            apiClient = this
        }
    }

    fun provideDataStorePreferences(): PreferenceManager {
        return dataStore ?: PreferenceManager(application.applicationContext).apply {
            dataStore = this
        }
    }
}