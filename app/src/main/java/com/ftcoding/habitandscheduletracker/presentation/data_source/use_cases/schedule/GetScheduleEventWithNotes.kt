package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule

import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithNotes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.relation.EventWithTrackerTimes
import com.ftcoding.habitandscheduletracker.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetScheduleEventWithNotes(
    private val repository: ScheduleRepository
) {

    operator fun invoke (eventId: Int) : List<EventWithNotes> {
        return repository.getScheduleWithNotes(eventId)
    }
}