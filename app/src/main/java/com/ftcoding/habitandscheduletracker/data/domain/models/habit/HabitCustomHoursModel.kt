package com.ftcoding.habitandscheduletracker.data.domain.models.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftcoding.habitandscheduletracker.util.HabitConstants

@Entity(tableName = HabitConstants.CUSTOM_HOURS_TABLE_NAME)
data class HabitCustomHoursModel(

    @PrimaryKey(autoGenerate = true)
    val  customHoursId: Int = 0,

    @ColumnInfo(name = HabitConstants.KEY_CUSTOM_HOURS_LIST_TIME)
    val customHourListTime: ArrayList<Long>
)
