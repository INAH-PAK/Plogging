package com.example.plogging.data.source

import com.example.plogging.data.model.Documents
import com.example.plogging.data.source.remote.ApiClient
import com.example.plogging.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackingRepository(
    private val apiClient: ApiClient
) {
    suspend fun getKakaoLocalApi(
        latitude: String,
        longitude: String,
        radius: Int,
        keyword: String = Constants.KEYWORD_KAKAO_LOCAL_API
    ): Flow<Documents> = flow {
        emit(
            apiClient.getKakaoLocalApi(
                latitude,
                longitude,
                radius,
                keyword
            )
        )
    }
}