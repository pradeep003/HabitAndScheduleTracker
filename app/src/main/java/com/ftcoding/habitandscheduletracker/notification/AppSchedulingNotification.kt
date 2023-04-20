package com.ftcoding.habitandscheduletracker.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import com.ftcoding.habitandscheduletracker.notification.receiver.AppBroadcastReceiver

fun Context.scheduleAppNotificationDaily(
) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)

    with(alarmManager) {
        Log.e("alarm ${calendar.get(Calendar.DAY_OF_MONTH)}", calendar.get(Calendar.DAY_OF_MONTH).toString())
        setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            getAppSchedulingReceiver()
        )
    }
}

private fun Context.getAppSchedulingReceiver(

) : PendingIntent {

    return PendingIntent.getBroadcast(
        this,
        0,
        Intent(this, AppBroadcastReceiver::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )
}