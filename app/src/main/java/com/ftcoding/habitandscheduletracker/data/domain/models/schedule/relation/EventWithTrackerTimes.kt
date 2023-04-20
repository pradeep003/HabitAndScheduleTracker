package com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.ScheduleTrackerTime

data class EventWithTrackerTimes(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val trackerTimes: List<ScheduleTrackerTime>
)
