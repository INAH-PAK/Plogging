package com.example.plogging

import android.app.Application
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk

class PloggingApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIENT_ID)
    }
}