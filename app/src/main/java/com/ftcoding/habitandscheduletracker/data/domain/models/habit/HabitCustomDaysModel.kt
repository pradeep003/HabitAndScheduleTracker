package com.ftcoding.habitandscheduletracker.data.domain.models.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftcoding.habitandscheduletracker.util.HabitConstants

@Entity(tableName = HabitConstants.CUSTOM_DAYS_TABLE_NAME)
data class HabitCustomDaysModel(

    @PrimaryKey(autoGenerate = true)
    val customDaysId: Int = 0,

    @ColumnInfo(name = HabitConstants.KEY_CUSTOM_NUM_OF_DAYS)
    val customNumOfDays : Int? = 1,

    @ColumnInfo(name = HabitConstants.KEY_CUSTOM_DAYS_LIST)
    val customDaysOfWeekList: ArrayList<String> = ArrayList<String>() ,

    @ColumnInfo(name = HabitConstants.KEY_CUSTOM_DAYS_TIME)
    val  customDayTime: Long
)
