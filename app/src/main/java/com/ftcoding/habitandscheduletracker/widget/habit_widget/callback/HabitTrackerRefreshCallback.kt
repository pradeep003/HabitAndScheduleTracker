package com.ftcoding.habitandscheduletracker.widget.habit_widget.callback

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitTrackerWidgetReceiver

class HabitTrackerRefreshCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = Intent(context, HabitTrackerWidgetReceiver::class.java).apply {
            action = UPDATE_ACTION
        }
        context.sendBroadcast(intent)
    }

    companion object {
        const val UPDATE_ACTION = "update_action"
        const val HABIT_ID = "habit_model_id"
    }
}