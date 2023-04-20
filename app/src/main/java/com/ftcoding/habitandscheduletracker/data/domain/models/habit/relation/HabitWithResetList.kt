package com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList

data class HabitWithResetList(
    @Embedded val habit: HabitModel,
    @Relation(
        parentColumn = "habitId",
        entityColumn = "habitId"
    )
    val resetList: List<ResetList>
)