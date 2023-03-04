package com.example.plogging

import com.example.plogging.data.source.remote.ApiClient

class AppContainer {

    private var apiClient: ApiClient? = null

    fun provideApiClient(): ApiClient {
        return apiClient ?: ApiClient.create(BuildConfig.KAKAO_REST_API_KEY).apply {
            apiClient = this
        }
    }
}