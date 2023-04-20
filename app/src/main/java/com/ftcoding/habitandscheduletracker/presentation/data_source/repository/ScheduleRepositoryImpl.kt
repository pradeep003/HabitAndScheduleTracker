package com.ftcoding.habitandscheduletracker.presentation.data_source.repository


import com.ftcoding.habitandscheduletracker.data.dao.ScheduleDao
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Notes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.ScheduleTrackerTime
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithNotes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithTrackerTimes
import com.ftcoding.habitandscheduletracker.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class ScheduleRepositoryImpl (
    private val dao: ScheduleDao
        ) : ScheduleRepository {
    override fun getAllSchedules(): Flow<List<Event>> {
        return dao.getAllSchedules()
    }

    override suspend fun getScheduleEventById(eventId: Int): Event? {
        return dao.getScheduleEventById(eventId)
    }

    override suspend fun insertScheduleEvent(event: Event) : Long{
        return dao.insetScheduleEvent(event)
    }

    override suspend fun insertTrackerTime(data: ScheduleTrackerTime) {
        dao.insertTrackerTime(data)
    }

    override suspend fun insertNotes(data: Notes) {
        dao.insertNotes(data)
    }

    override suspend fun deleteScheduleEvent(event: Event) {
        dao.deleteScheduleEvent(event)
    }

    override fun getScheduleWithTrackerTime(eventId: Int): List<EventWithTrackerTimes> {
        return dao.getScheduleWithTrackerTime(eventId)
    }

    override fun getScheduleWithNotes(eventId: Int): List<EventWithNotes> {
        return dao.getScheduleWithNotes(eventId)
    }
}