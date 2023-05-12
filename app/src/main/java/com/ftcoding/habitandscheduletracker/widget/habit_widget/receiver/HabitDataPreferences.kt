package com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import kotlinx.serialization.Serializable

@Serializable
data class HabitDataPreferences(
    val prefData: List<HabitWithGlanceIdModel> = listOf()
)

@Serializable
data class HabitWithGlanceIdModel(
    val list: HabitModel? = null,
    val glanceId: Int = -1
)
