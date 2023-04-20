package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule

data class ScheduleUseCases(
    val getAllScheduleEvents: GetAllScheduleEvents,
    val getScheduleEventById: GetScheduleEventById,
    val insertScheduleEvent: InsertScheduleEvent,
    val deleteScheduleEvent: DeleteScheduleEvent,
    val getScheduleEventWithTrackerTimes: GetScheduleEventWithTrackerTimes,
    val getScheduleEventWithNotes: GetScheduleEventWithNotes
)
