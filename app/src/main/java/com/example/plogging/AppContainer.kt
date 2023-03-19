package com.example.plogging

import android.app.Application
import com.example.plogging.data.source.UserDataRepository
import com.example.plogging.data.source.local.AppDatabase
import com.example.plogging.data.source.remote.ApiClient
import com.example.plogging.data.source.remote.PreferenceManager

class AppContainer(private val application: Application) {

    private var kakaoApiClient: ApiClient? = null
    private var firebaseApiClient: ApiClient? = null
    private var dataStore: PreferenceManager? = null
    private var userDataRepository: UserDataRepository? = null
    private var database: AppDatabase? = null

    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getInstance(application)
    }

    fun provideFirebaseApiClient() : ApiClient {
        return firebaseApiClient ?: ApiClient.createFirebaseApiClient().apply {
            firebaseApiClient = this
        }
    }

    fun provideKakaoApiClient(): ApiClient {
        return kakaoApiClient ?: ApiClient.createKakaoApiClient(BuildConfig.KAKAO_REST_API_KEY).apply {
            kakaoApiClient = this
        }
    }

    fun provideUserDataRepository(): UserDataRepository {
        return userDataRepository ?: UserDataRepository(
            provideDataStorePreferences()
        )
    }

    private fun provideDataStorePreferences(): PreferenceManager {
        return dataStore ?: PreferenceManager(application.applicationContext).apply {
            dataStore = this
        }
    }
}