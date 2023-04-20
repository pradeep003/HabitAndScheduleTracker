package com.ftcoding.habitandscheduletracker.notification.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.notification.PRIMARY_CHANNEL_ID
import com.ftcoding.habitandscheduletracker.notification.scheduleAppNotificationDaily
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule.ScheduleUseCases
import com.ftcoding.habitandscheduletracker.util.HabitConstants.FOREGROUND_NOTIFICATION_ID
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class AppWorkerClass @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val scheduleUseCases: ScheduleUseCases
) : CoroutineWorker(appContext, workerParams) {

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {

        notificationManager.createNotificationChannel(buildChannel())
//        setForeground(ForegroundInfo(FOREGROUND_NOTIFICATION_ID, startForegroundService().build()))
//        setForegroundAsync(ForegroundInfo(FOREGROUND_NOTIFICATION_ID, startForegroundService().build()))

        delay(500)

        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, startForegroundService().build())

        val calendar = Calendar.getInstance()

        scheduleUseCases.getAllScheduleEvents.invoke().collect { repeatList ->
            Log.e("do work", repeatList.toString())
            repeatList.forEach { event ->
                if (event.repeatDayList.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
//                    applicationContext.scheduleTodayNotification(
//                        isAlarm = event.alarmType,
//                        id = event.eventId,
//                        eventName = event.name,
//                        eventDesc = event.description,
//                        eventStartHour = event.start.hour,
//                        eventStartMin = event.start.minute,
//                        eventIcon = event.icon,
//                        repeatDayList = event.repeatDayList.toIntArray()
//                    )
                }
            }

        }

        return Result.success()
    }

    companion object {
        const val WORK_NAME =
            "com.ftcoding.habitandscheduletracker.notification.receiver.AppWorkerClass"
    }

    private fun startForegroundService(): NotificationCompat.Builder {

        return NotificationCompat
            .Builder(applicationContext, "channel_id")
            .setSmallIcon(R.drawable.trophy)
            .setContentText("Scheduling today alarm...")
            .setContentTitle("Work in progress")
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setOngoing(true)
    }

    // build channel for notification
    private fun buildChannel(): NotificationChannel {
        val name = "app Notification"
        val descriptionText = "This is used to demonstrate the notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(PRIMARY_CHANNEL_ID, name, importance).apply {
            description = descriptionText
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        return channel
    }
}

