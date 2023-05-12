package com.ftcoding.habitandscheduletracker.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.TimerModel
import com.ftcoding.habitandscheduletracker.notification.showHabitNotification
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit.HabitUseCases
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HabitNotificationReceiver : BroadcastReceiver() {

    @Inject lateinit var habitUseCases: HabitUseCases
    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.getStringExtra("habit_title")?.let { habitName ->
            intent.getStringExtra("habit_desc")?.let { habitDesc ->
                intent.getIntExtra("habit_icon", HabitConstants.HABIT_ICON_LIST[0].icon)
                    .let { habitIcon ->
                        intent.getIntExtra("habit_id", 0).let { habitId ->
                            // show how much day completed
                            var resetDays = 0
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    habitUseCases.getHabitWithResetList.invoke(habitId).collect {lists ->
                                        // get the last reset time
                                        val lastElementDate = lists[0].resetList.last().habitResetDate


                                        val currentTime = Calendar.getInstance().timeInMillis
                                        val selectedTime = Calendar.getInstance()
                                        selectedTime.set(lastElementDate.year, lastElementDate.monthValue, lastElementDate.dayOfMonth)

                                        val timeDifference = selectedTime.let {
                                            currentTime.minus(it.timeInMillis)
                                        }

                                        // Calculate time difference in days using TimeUnit class
                                        val daysDifference: Long = TimeUnit.MILLISECONDS.toDays(timeDifference) % 365
                                       resetDays = daysDifference.toInt()


                                    }
                                } finally {
                                    cancel()
                                }
                            }
                            context?.showHabitNotification(
                                habitId, habitName, habitDesc, habitIcon
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
