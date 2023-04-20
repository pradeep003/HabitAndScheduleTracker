package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule

import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.repository.ScheduleRepository

class GetScheduleEventById(
    private val repository: ScheduleRepository
) {

    suspend operator fun invoke(eventId: Int): Event? {
        return repository.getScheduleEventById(eventId)
    }
}