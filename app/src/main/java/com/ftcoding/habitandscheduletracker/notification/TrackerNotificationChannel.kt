package com.ftcoding.habitandscheduletracker.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.core.app.NotificationCompat
import com.ftcoding.habitandscheduletracker.notification.receiver.ScheduleNotificationReceiver
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_DESC
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_TITLE
import java.time.LocalTime
import kotlin.jvm.optionals.getOrDefault


const val PRIMARY_CHANNEL_ID = "schedule_tracker_notification_channel"

private lateinit var notificationManager: NotificationManager

// display alarm notification
fun Context.showNotificationWithFullScreenIntent(
    isAlarm: Boolean = false,
    notificationId: Int = 0,
    channelId: String = PRIMARY_CHANNEL_ID,
    eventName: String,
    eventDesc: String,
    eventIcon: Int
) {

    val builder = if (isAlarm) {
        NotificationCompat.Builder(this, channelId)
            .setSmallIcon(eventIcon)
            .setContentTitle(eventName)
            .setContentText(eventDesc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVibrate(longArrayOf(0, 800, 200, 1200, 300, 2000, 400, 4000, 500, 5000))
            .setFullScreenIntent(getFullScreenIntent(eventName, eventDesc), true)
    } else {
        NotificationCompat.Builder(this, channelId)
            .setSmallIcon(eventIcon)
            .setContentTitle(eventName)
            .setContentText(eventDesc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)

    }

    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    with(notificationManager) {
        buildChannel()
        val notification = builder.build()
        notify(notificationId, notification)
    }
}

// build channel for notification
private fun NotificationManager.buildChannel() {
    val name = "Schedule tracker Notification"
    val descriptionText = "Notification relate to schedule tracker are displayed"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(PRIMARY_CHANNEL_ID, name, importance).apply {
        description = descriptionText
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    }
    createNotificationChannel(channel)
}

// get full screen intent to navigate to lock screen activity
// it will trigger when you want to use alarm
private fun Context.getFullScreenIntent(eventName: String, eventDesc: String): PendingIntent {

    val intent = Intent(this, AlarmLockScreenActivity::class.java)
    intent.putExtra(NOTIFICATION_TITLE, eventName)
    intent.putExtra(NOTIFICATION_DESC, eventDesc)

    return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
}


// schedule next notification
fun Context.scheduleNotification(
    isAlarm: Boolean = true,
    id: Int,
    eventName: String,
    eventDesc: String,
    eventStartHour: Int,
    eventStartMin: Int,
    eventIcon: Int,
    repeatDayList: IntArray
) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    val nextWeekDayTarget = repeatDayList.toList().stream().filter { it ->
        it > dayOfWeek
    }.findFirst().getOrDefault(1)

    val eventScheduleTime = LocalTime.of(eventStartHour, eventStartMin)

    // if the notification schedule for today and is current time is before schedule time
    if (repeatDayList.contains(dayOfWeek) && eventScheduleTime.isAfter(LocalTime.now())) {
//        if (eventStartHour >= LocalTime.now().hour)

        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)

    } else if (nextWeekDayTarget > dayOfWeek) {
        // if schedule week of day is greater than current day of week
        calendar.set(Calendar.DAY_OF_WEEK, nextWeekDayTarget)

    } else {
        // if schedule on next week of day
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        calendar.set(Calendar.DAY_OF_WEEK, repeatDayList[0])
    }
    calendar.set(Calendar.HOUR_OF_DAY, eventStartHour)
    calendar.set(Calendar.MINUTE, eventStartMin)
    calendar.set(Calendar.SECOND, 0)

    // schedule alarm at exact time
    with(alarmManager) {
        setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            getReceiver(
                id = id,
                isAlarm = isAlarm,
                eventName = eventName,
                eventDesc = eventDesc,
                eventStartHour = eventStartHour,
                eventStartMin = eventStartMin,
                eventIcon = eventIcon,
                repeatDayList = repeatDayList
            )
        )
    }
}

private fun Context.getReceiver(
    id: Int,
    isAlarm: Boolean,
    eventName: String,
    eventDesc: String,
    eventStartHour: Int,
    eventStartMin: Int,
    eventIcon: Int,
    repeatDayList: IntArray
): PendingIntent {
    return PendingIntent.getBroadcast(
        this,
        0,
        ScheduleNotificationReceiver.build(
            this,
            eventIsAlarm = isAlarm,
            eventId = id,
            eventName = eventName,
            eventDesc = eventDesc,
            eventStartHour = eventStartHour,
            eventStartMin = eventStartMin,
            eventIcon = eventIcon,
            repeatDayList = repeatDayList
        ),
        PendingIntent.FLAG_IMMUTABLE
    )
}

