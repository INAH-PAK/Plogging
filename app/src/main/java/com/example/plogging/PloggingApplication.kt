package com.example.plogging

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.naver.maps.map.NaverMapSdk

class PloggingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_CLIENT_ID)
        appContainer = AppContainer(this)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
            Log.i(" 채널이 만들어 졌는가? ", notificationManager.areNotificationsEnabled().toString())
        }
    }

    companion object {
        lateinit var appContainer: AppContainer
        const val CHANNEL_ID = "CHANNEL_GPS"
    }
}