package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule

import android.util.Log
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.InvalidEventException
import com.ftcoding.habitandscheduletracker.data.repository.ScheduleRepository


class InsertScheduleEvent (
    private val repository: ScheduleRepository
        ) {

    @Throws(InvalidEventException::class)
    suspend operator fun invoke(event: Event) : Long {
        if (event.name.isBlank()) {
            throw InvalidEventException("Title can't be empty.")
        }
        if (event.end.isBefore(event.start)) {
            throw InvalidEventException("End time can't be before than start time")
        }

        return repository.insertScheduleEvent(event)
    }
}