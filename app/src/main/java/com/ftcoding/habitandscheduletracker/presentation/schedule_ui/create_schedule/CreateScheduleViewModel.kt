package com.ftcoding.habitandscheduletracker.presentation.schedule_ui.create_schedule

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule.ScheduleUseCases
import com.ftcoding.habitandscheduletracker.presentation.util.state.MessageBarState
import com.ftcoding.habitandscheduletracker.presentation.util.state.StandardTextFieldState
import com.ftcoding.habitandscheduletracker.util.HabitConstants.HABIT_ICON_LIST
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateScheduleViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
) : ViewModel() {

    fun getEventById(eventId: Int) {
        if (eventId != -1) {
            viewModelScope.launch {
                scheduleUseCases.getScheduleEventById.invoke(eventId)?.also { event ->

                    Log.e("get event", eventId.toString())

                    setEventId(eventId)
                    setEventIcon(event.icon)
                    setEventColor(event.color)
                    setScheduleTitle(StandardTextFieldState(text = event.name))
                    setDescTitle(StandardTextFieldState(text = event.description))
                    setStartTime(event.start)
                    setEndTime(event.end)
                    // if true its alarm else its notification
                    setNotifyTypeState(
                        if (event.alarmType) NOTIFY_TYPE[1] else NOTIFY_TYPE[0]
                    )
                    event.repeatDayList.forEach {
                        addOrRemoveWeekOfDay(it)
                    }

                    Log.e(event.toString(), eventState.value.toString())

                }
            }
        }
    }
    private fun setEventId(id: Int) {
        _eventState.value = eventState.value.copy(eventId = id)
    }


    val calendar: Calendar = Calendar.getInstance()

    val currentTimeDate = LocalDateTime.now()

    // display error message
    val messageBarState = mutableStateOf(MessageBarState())

    private val _eventState = mutableStateOf(
        Event(
            eventId = -1,
            name = "",
            description = "",
            color = "#e10000",
            icon = HABIT_ICON_LIST[0].icon,
            start = currentTimeDate.toLocalTime(),
            end = currentTimeDate.toLocalTime().plusHours(1),
            repeatDayList = listOf(Calendar.DAY_OF_WEEK),
            alarmType = true
        )
    )
    val eventState: State<Event> = _eventState

    fun setScheduleTitle(state: StandardTextFieldState) {
        _eventState.value = eventState.value.copy(name = state.text)
    }

    // description
    fun setDescTitle(state: StandardTextFieldState) {
        _eventState.value = eventState.value.copy(description = state.text)
    }

    // start time
    fun setStartTime(state: LocalTime) {
        _eventState.value = eventState.value.copy(start = state)
    }

    // end time
    fun setEndTime(state: LocalTime) {
        _eventState.value = eventState.value.copy(end = state)
    }


    fun addOrRemoveWeekOfDay(weekOfDay: Int) {
        if (eventState.value.repeatDayList.contains(weekOfDay)) {
            _eventState.value =
                eventState.value.copy(repeatDayList = eventState.value.repeatDayList.minus(weekOfDay))
        } else {
            _eventState.value =
                eventState.value.copy(repeatDayList = eventState.value.repeatDayList.plus(weekOfDay))
        }
    }

    // notification type
    fun setNotifyTypeState(state: String) {
        _eventState.value = eventState.value.copy(alarmType = state != NOTIFY_TYPE[0])
    }

    // icon
    fun setEventIcon(icon: Int) {
        _eventState.value = eventState.value.copy(icon = icon)
    }

    // color
    fun setEventColor(color: String) {
        _eventState.value = eventState.value.copy(color = color)
    }


    fun saveScheduleEvent(newSavedEvent: (Event) -> Unit) {
        var eventId: Int
        viewModelScope.launch {
            try {
                val event = Event(
                    name = eventState.value.name,
                    color = eventState.value.color,
                    icon = eventState.value.icon,
                    start = eventState.value.start,
                    end = eventState.value.end,
                    repeatDayList = eventState.value.repeatDayList.sorted(),
                    description = eventState.value.description,
                    alarmType = eventState.value.alarmType
                )
                // save new event
                eventId = scheduleUseCases.insertScheduleEvent(
                    event
                ).toInt()
//                getEventById(eventId = eventId)
                scheduleUseCases.getScheduleEventById.invoke(eventId)?.let { newSavedEvent(it) }
                // send event to callback
            } catch (e: Exception) {
                messageBarState.value.addError(e.message ?: "Couldn't save schedule")
            }


        }
    }

}