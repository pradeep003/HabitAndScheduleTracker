package com.ftcoding.habitandscheduletracker.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.notification.receiver.HabitNotificationReceiver
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import java.text.SimpleDateFormat
import java.util.*

const val PRIMARY_HABIT_CHANNEL_ID = "habit_notification_channel"

private lateinit var notificationManager: NotificationManager

fun Context.showHabitNotification(
    notificationId: Int,
    title: String,
    desc: String = "We become what we repeatedly do",
    icon: Int
) {
    val builder = NotificationCompat.Builder(this, PRIMARY_HABIT_CHANNEL_ID)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(desc)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)

    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    with(notificationManager) {
        Log.e("tag", "notify")
        buildHabitChannel()
        val notification = builder.build()
        notify(notificationId, notification)
    }
}

fun Context.scheduleHabitNotification(
    habitModel: HabitModel
) {

    val calendar = Calendar.getInstance()
    val obj = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    obj.parse(habitModel.habitStartTime)?.time

    calendar.set(Calendar.DAY_OF_MONTH, obj.calendar.get(Calendar.DAY_OF_MONTH))
    calendar.set(Calendar.MONTH, obj.calendar.get(Calendar.MONTH))
    calendar.set(Calendar.YEAR, obj.calendar.get(Calendar.YEAR))
    calendar.set(Calendar.HOUR_OF_DAY, obj.calendar.get(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.MINUTE, obj.calendar.get(Calendar.MINUTE))
    calendar.set(Calendar.SECOND, obj.calendar.get(Calendar.SECOND))

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Wake up the device to fire the alarm in 30 minutes, and every 1 day after that
    with(alarmManager) {
        setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
            AlarmManager.INTERVAL_DAY,
            getReceiver(
                habitId = habitModel.habitId,
                habitName = habitModel.habitTitle,
                habitDesc = habitModel.habitDesc,
                habitIcon = habitModel.habitIcon
            )
        )
    }
}


private fun Context.getReceiver(
    habitId: Int,
    habitName: String,
    habitDesc: String,
    habitIcon: Int
): PendingIntent {
    return PendingIntent.getBroadcast(
        this,
        0,
        HabitNotificationReceiver.habitBuild(this, habitId, habitName, habitDesc, habitIcon),
        PendingIntent.FLAG_IMMUTABLE
    )
}



private fun NotificationManager.buildHabitChannel() {
    val name = "Habit Notification Channel"
    val descriptionText = "Notification relate to habit tracker are displayed"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(PRIMARY_HABIT_CHANNEL_ID, name, importance).apply {
        description = descriptionText
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    }
    createNotificationChannel(channel)
}