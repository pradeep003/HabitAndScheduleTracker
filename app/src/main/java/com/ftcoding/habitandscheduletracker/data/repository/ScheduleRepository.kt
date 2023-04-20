package com.ftcoding.habitandscheduletracker.data.repository


import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Notes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.ScheduleTrackerTime
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithNotes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithTrackerTimes
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {


    fun getAllSchedules() : Flow<List<Event>>

    suspend fun getScheduleEventById(eventId: Int) : Event?

    suspend fun insertScheduleEvent(event: Event) : Long

    suspend fun insertTrackerTime(data: ScheduleTrackerTime)

    suspend fun insertNotes(data: Notes)

    suspend fun deleteScheduleEvent(event: Event)

     fun getScheduleWithTrackerTime(eventId: Int): List<EventWithTrackerTimes>

    fun getScheduleWithNotes(eventId: Int): List<EventWithNotes>
}