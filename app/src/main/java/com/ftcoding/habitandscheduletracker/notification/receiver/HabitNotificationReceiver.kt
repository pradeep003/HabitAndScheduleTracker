package com.ftcoding.habitandscheduletracker.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ftcoding.habitandscheduletracker.notification.showHabitNotification
import com.ftcoding.habitandscheduletracker.util.HabitConstants

class HabitNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.getStringExtra("habit_title")?.let { habitName ->
            intent.getStringExtra("habit_desc")?.let { habitDesc ->
                intent.getIntExtra("habit_icon", HabitConstants.HABIT_ICON_LIST[0].icon)
                    .let { habitIcon ->
                        intent.getIntExtra("habit_id", 0).let { habitId ->
                            // show how much day completed
                            context?.showHabitNotification(
                                habitId,habitName, habitDesc, habitIcon
                            )
                        }

                    }
            }
        }

    }

    companion object {
        fun habitBuild(
            context: Context,
            habitId: Int,
            habitName: String,
            habitDesc: String,
            habitIcon: Int
        ): Intent {
            return Intent(context, HabitNotificationReceiver::class.java).also {
                it.putExtra("habit_id", habitId)
                it.putExtra("habit_title", habitName)
                it.putExtra("habit_desc", habitDesc)
                it.putExtra("habit_icon", habitIcon)
            }
        }
    }
}
