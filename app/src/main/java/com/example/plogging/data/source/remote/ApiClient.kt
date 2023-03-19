package com.example.plogging.data.source.remote

import com.example.plogging.data.model.Documents
import com.example.plogging.data.model.TogetherRecode
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface ApiClient {

    @GET("v2/local/search/keyword.json")
    suspend fun getKakaoLocalApi(
        @Query("y") latitude: String,
        @Query("x") longitude: String,
        @Query("radius") radius: Int,
        @Query("query") query: String,
    ): Documents

    @POST("plogging/recodes/{create_date}.json")
    suspend fun postRecode(
        @Path("create_date") createDate: String,
        @Body togetherRecode: TogetherRecode
    )

    companion object {

        private const val BASE_URL_KAKAO = "https://dapi.kakao.com/"
        private const val BASE_URL_FIREBASE =
            "https://plogging-1efa6-default-rtdb.asia-southeast1.firebasedatabase.app/"

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        fun createFirebaseApiClient(): ApiClient {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL_FIREBASE)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiClient::class.java)
        }

        fun createKakaoApiClient(apiKey: String): ApiClient {
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
                .baseUrl(BASE_URL_KAKAO)
                .client(client)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiClient::class.java)
        }
    }
}