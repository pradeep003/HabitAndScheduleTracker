package com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit.HabitUseCases
import com.ftcoding.habitandscheduletracker.widget.habit_widget.callback.HabitTrackerRefreshCallback
import com.ftcoding.habitandscheduletracker.widget.habit_widget.callback.HabitTrackerRefreshCallback.Companion.HABIT_ID
import com.ftcoding.habitandscheduletracker.widget.habit_widget.ui.HabitTrackerWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HabitTrackerWidgetReceiver() : GlanceAppWidgetReceiver() {


    override val glanceAppWidget: GlanceAppWidget
        get() = HabitTrackerWidget()

    private val coroutineScope = MainScope()

    @Inject
    lateinit var habitUseCases: HabitUseCases

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeData(context, -1)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // receiver custom broadcast calls
        if (intent.action == HabitTrackerRefreshCallback.UPDATE_ACTION) {
            val habitId = intent.getIntExtra(HABIT_ID, -1)
            observeData(context, habitId)

        }
    }

     private fun observeData(context: Context, habitId: Int) {

        coroutineScope.launch {

            if (habitId != -1) {
                val habitModel = habitUseCases.getHabitModelByIdUseCase.invoke(habitId)

                // get list of glance id and select the last id (which will be the newest one)
                val glanceId =
                    GlanceAppWidgetManager(context = context).getGlanceIds(HabitTrackerWidget::class.java)
                        .lastOrNull()

                // fetch data to data class
                val habitModelWithGlance = HabitWithGlanceIdModel(habitModel, glanceId.hashCode())

                if (glanceId != null) {
                    updateAppWidgetState(context, HabitTrackerGlanceStateDefinition, glanceId) { pref ->
                        // add this data to datastore list

                        pref.copy(
                            prefData = pref.prefData.plus(habitModelWithGlance)
                        )

                    }
                    HabitTrackerWidget().update(context, glanceId)
                }

            }
        }
    }
}