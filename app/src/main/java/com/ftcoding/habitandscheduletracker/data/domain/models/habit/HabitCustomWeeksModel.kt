package com.ftcoding.habitandscheduletracker.data.domain.models.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftcoding.habitandscheduletracker.util.HabitConstants

@Entity(tableName = HabitConstants.CUSTOM_WEEKS_TABLE_NAME)
data class HabitCustomWeeksModel(

    @PrimaryKey(autoGenerate = true)
    val customWeekId: Int = 0,

    @ColumnInfo(name = HabitConstants.KEY_CUSTOM_WEEK_REPEAT)
    var customWeek: Int = 1,

    @ColumnInfo(name = HabitConstants.KEY_CUSTOM_WEEK_REPEAT_DAY)
    var customWeekDay: String,

    @ColumnInfo(name = HabitConstants.KEY_CUSTOM_WEEK_REPEAT_TIME)
    var customWeekTime: Long
)
