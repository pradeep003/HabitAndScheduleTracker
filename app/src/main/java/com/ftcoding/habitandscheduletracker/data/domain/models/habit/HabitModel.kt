package com.ftcoding.habitandscheduletracker.data.domain.models.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftcoding.habitandscheduletracker.util.HabitConstants

@kotlinx.serialization.Serializable
@Entity(tableName = HabitConstants.HABIT_TABLE_NAME)
data class HabitModel(

    @PrimaryKey(autoGenerate = true)
    val habitId: Int = 0,

    @ColumnInfo(name = HabitConstants.KEY_TITLE)
    val habitTitle: String,

    @ColumnInfo(name = HabitConstants.KEY_DESC)
    val habitDesc: String,

    @ColumnInfo(name = HabitConstants.KEY_START_TIME)
    val habitStartTime: String,

    @ColumnInfo(name = HabitConstants.KEY_LAST_RESET_TIME)
    val habitLastResetTime: String,

    @ColumnInfo(name = HabitConstants.KEY_NOTIFY)
    val notify : Boolean = false,

    @ColumnInfo(name = HabitConstants.KEY_ICON)
    val habitIcon : Int,

    @ColumnInfo(name = HabitConstants.KEY_COLOR)
    val habitColor: String
)

class InvalidHabitException(message: String) : Exception(message)