package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule

import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class GetAllScheduleEvents(
    private val repository: ScheduleRepository
) {

    operator fun invoke() : Flow<List<Event>> {
        return repository.getAllSchedules()
    }
}