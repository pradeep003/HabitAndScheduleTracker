package com.ftcoding.habitandscheduletracker.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class AppBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("receiver", "true")

        val repeatingRequest = PeriodicWorkRequestBuilder<AppWorkerClass>(1, TimeUnit.DAYS).build()
        if (context != null) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                AppWorkerClass.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
        }

    }
}