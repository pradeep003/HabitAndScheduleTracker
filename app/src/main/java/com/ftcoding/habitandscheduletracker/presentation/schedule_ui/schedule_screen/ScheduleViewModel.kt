package com.ftcoding.habitandscheduletracker.presentation.schedule_ui.schedule_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule.ScheduleUseCases
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.presentation.util.state.MessageBarState
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    init {
        getAllScheduleEvent()
        getUserState()
    }

    private val messageBarState = mutableStateOf(MessageBarState())

    private val _list = mutableStateOf<List<Event>>(emptyList())
    val list: State<List<Event>> = _list

    private val _selectedDayEventList = mutableStateOf<List<Event>>(emptyList())
    val selectedDayEventList: State<List<Event>> = _selectedDayEventList

    private val _selectedDate = mutableStateOf(LocalDate.now())
    val selectedDate: State<LocalDate> = _selectedDate

    fun changeSelectedDate(date: LocalDate) {

        _selectedDate.value = date
        val newEventList = ArrayList<Event>()

        list.value.forEach { event ->

            if (event.repeatDayList.contains(date.dayOfWeek.value)) {
                newEventList.add(event)
            }
        }
        _selectedDayEventList.value = newEventList
        Log.e("${date.dayOfWeek.value}", newEventList.toString())
    }

    // get user setting detail state
   private val _userState = mutableStateOf(User())
    val userState : State<User> = _userState

    private fun getUserState () {
        viewModelScope.launch {
            userUseCases.getUserUseCase.invoke().collect { list ->
                if (list.isNotEmpty()) {
                    list.first {
                        if (it.userId == HabitConstants.USER_ID) {
                            _userState.value = it
                            return@first true
                        } else {
                            _userState.value = User()
                            return@first false
                        }
                    }
                }
            }
        }
    }

    private fun getAllScheduleEvent() {
        viewModelScope.launch {
            scheduleUseCases.getAllScheduleEvents.invoke().collect { list ->
                if (list.isNotEmpty()) {
                    _list.value = list
                    
                }
            }
        }
    }

    fun deleteScheduleEvent(event: Event) {
        viewModelScope.launch {
            scheduleUseCases.deleteScheduleEvent(
                event
            )
        }
    }

    private fun onError(message: String) {
        messageBarState.value.addError(message)
    }

    fun onSuccess(message: String) {
        messageBarState.value.addSuccess(message)
    }
}