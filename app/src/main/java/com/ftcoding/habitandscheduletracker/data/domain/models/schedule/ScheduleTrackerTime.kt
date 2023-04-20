package com.ftcoding.habitandscheduletracker.data.domain.models.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScheduleTrackerTime(

    @PrimaryKey(autoGenerate = true)
    val trackerId: Int = 0,

    @ColumnInfo(name = "date")
    val trackerDate: Long,

    @ColumnInfo(name = "tracker_time")
    val trackerTime: Long,

    @ColumnInfo(name = "eventId")
    val eventId: Int
)
