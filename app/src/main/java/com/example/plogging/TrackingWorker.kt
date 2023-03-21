package com.example.plogging

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import com.example.plogging.util.Constants
import com.google.android.gms.location.*
import kotlinx.coroutines.delay

// https://medium.com/androiddevelopers/use-workmanager-for-immediate-background-execution-a57db502603d

class TrackingWorker(
    context: Context, parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        setForeground(ForegroundInfo(Constants.PLOGGING_NOTIFICATION_ID, createNotification()))
        delay(5000) // TODO 백그라운드에서 사용자 위치 추적
        return Result.success()
    }

    private val pendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.trackingFragment)
        .createPendingIntent()

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(applicationContext, Constants.PLOGGING_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_directions_run_24)
            .setContentTitle("Plogging")
            .setContentText("Plogging 중이시군요 !")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
}