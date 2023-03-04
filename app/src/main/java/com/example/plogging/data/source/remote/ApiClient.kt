package com.example.plogging.data.source.remote

import com.example.plogging.data.model.Documents
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("v2/local/search/keyword.json")
    suspend fun getKakaoLocalApi(
        @Query("y") latitude: String,
        @Query("x") longitude: String,
        @Query("radius") radius: Int,
        @Query("query") query: String,
    ): Documents

    companion object {

        private const val BASE_URL = "https://dapi.kakao.com/"
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        fun create(apiKey: String): ApiClient {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val header = Interceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", apiKey)
                    .build()
                chain.proceed(newRequest)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(header)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiClient::class.java)
        }
    }
}