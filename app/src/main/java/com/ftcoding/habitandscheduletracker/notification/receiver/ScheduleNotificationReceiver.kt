package com.ftcoding.habitandscheduletracker.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ftcoding.habitandscheduletracker.notification.scheduleNotification
import com.ftcoding.habitandscheduletracker.notification.showNotificationWithFullScreenIntent
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_DESC
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_ICON
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_ID
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_IS_ALARM
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_REPEAT_LIST
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_START_HOUR
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_START_MINUTE
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFICATION_TITLE

class ScheduleNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.getBooleanExtra(NOTIFICATION_IS_ALARM, true).let { isAlarm ->
            intent?.getIntExtra(NOTIFICATION_ID, 0).let { id ->
                intent?.getStringExtra(NOTIFICATION_TITLE)?.let { eventName ->
                    intent.getStringExtra(NOTIFICATION_DESC)?.let { eventDesc ->
                        intent.getIntExtra(NOTIFICATION_START_HOUR, 0).let { eventStartHour ->
                            intent.getIntExtra(NOTIFICATION_START_MINUTE, 0).let { eventStartMin ->
                                intent.getIntExtra(NOTIFICATION_ICON, 0).let { eventIcon ->
                                    intent.getIntArrayExtra(NOTIFICATION_REPEAT_LIST)
                                        .let { repeatDayList ->
                                            if (repeatDayList != null) {

                                                context?.showNotificationWithFullScreenIntent(
                                                    eventName = eventName,
                                                    eventIcon = eventIcon,
                                                    eventDesc = eventDesc
                                                )
                                                // start scheduling for next notification
                                                if (id != null) {
                                                    context?.scheduleNotification(
                                                        isAlarm = isAlarm ?: true,
                                                        id = id,
                                                        eventName = eventName,
                                                        eventDesc = eventDesc,
                                                        eventStartHour = eventStartHour,
                                                        eventStartMin = eventStartMin,
                                                        eventIcon = eventIcon,
                                                        repeatDayList = repeatDayList
                                                    )
                                                }

                                                if ((Intent.ACTION_BOOT_COMPLETED) == intent.action) {
                                                    // if device reboot reschedule notification
                                                    if (id != null) {
                                                        context?.scheduleNotification(
                                                            isAlarm = isAlarm ?: true,
                                                            id = id,
                                                            eventName = eventName,
                                                            eventDesc = eventDesc,
                                                            eventStartHour = eventStartHour,
                                                            eventStartMin = eventStartMin,
                                                            eventIcon = eventIcon,
                                                            repeatDayList = repeatDayList
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun build(
            context: Context,
            eventIsAlarm: Boolean,
            eventId: Int,
            eventName: String,
            eventDesc: String,
            eventStartHour: Int,
            eventStartMin: Int,
            eventIcon: Int,
            repeatDayList: IntArray
        ): Intent {

            return Intent(context, ScheduleNotificationReceiver::class.java).also {
                it.putExtra(NOTIFICATION_IS_ALARM, eventIsAlarm)
                it.putExtra(NOTIFICATION_ID, eventId)
                it.putExtra(NOTIFICATION_TITLE, eventName)
                it.putExtra(NOTIFICATION_DESC, eventDesc)
                it.putExtra(NOTIFICATION_START_HOUR, eventStartHour)
                it.putExtra(NOTIFICATION_START_MINUTE, eventStartMin)
                it.putExtra(NOTIFICATION_ICON, eventIcon)
                it.putExtra(NOTIFICATION_REPEAT_LIST, repeatDayList)
            }


        }
    }
}

