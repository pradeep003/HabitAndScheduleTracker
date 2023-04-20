package com.ftcoding.habitandscheduletracker.data.domain.models.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val eventId: Int = 0,

    @ColumnInfo(name = "schedule_title")
    val name: String,

    @ColumnInfo(name = "schedule_color")
    val color: String,

    @ColumnInfo(name = "schedule_icon")
    val icon: Int,

    @ColumnInfo(name = "schedule_start_time")
    val start: LocalTime,

    @ColumnInfo(name = "schedule_end_time")
    val end: LocalTime,

    @ColumnInfo(name = "schedule_description")
    val description: String ,

    @ColumnInfo(name = "schedule_repeat_day")
    val repeatDayList: List<Int>,

    @ColumnInfo(name = "alarm_type")
    val alarmType: Boolean
)

class InvalidEventException(message: String) : Exception(message)
