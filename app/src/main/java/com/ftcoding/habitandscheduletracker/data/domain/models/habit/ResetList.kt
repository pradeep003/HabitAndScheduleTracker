package com.ftcoding.habitandscheduletracker.data.domain.models.habit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = HabitConstants.HABIT_RESET_TABLE_NAME)
data class ResetList(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val habitResetDate: LocalDate,

    @ColumnInfo
    val habitResetTime: LocalTime,

    @ColumnInfo
    val habitId: Int
)
