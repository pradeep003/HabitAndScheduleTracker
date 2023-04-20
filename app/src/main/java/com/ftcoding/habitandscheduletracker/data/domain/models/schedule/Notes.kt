package com.ftcoding.habitandscheduletracker.data.domain.models.schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ftcoding.habitandscheduletracker.util.HabitConstants

@Entity
data class Notes (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: Long? = null,

    @ColumnInfo(name = "notes")
    val notes: List<String>? = null,

    @ColumnInfo(name = "eventId")
    val eventId: Int? = null
)