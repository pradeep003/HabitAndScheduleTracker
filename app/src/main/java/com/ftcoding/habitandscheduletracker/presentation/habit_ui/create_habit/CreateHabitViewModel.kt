package com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.InvalidHabitException
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.TimerModel
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit.HabitUseCases
import com.ftcoding.habitandscheduletracker.presentation.util.state.MessageBarState
import com.ftcoding.habitandscheduletracker.presentation.util.state.StandardTextFieldState
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val habitUseCases: HabitUseCases
) : ViewModel() {


    fun getHabitModelById (habitId: Int) {
        Log.e("id", habitId.toString())
            if (habitId != -1) {
                // if habitId != 0 than fetch the habitModel of given id
                // else user is going to create new habitModel
                viewModelScope.launch {
                    try {
                        habitUseCases.getHabitModelByIdUseCase.invoke(habitId)?.also { habitModel ->
                            setHabitId(id = habitId)
                            setHabitTitle(StandardTextFieldState(text = habitModel.habitTitle))
                            setDescTitle(StandardTextFieldState(text = habitModel.habitDesc))
                            setHabitIcon(habitModel.habitIcon)
                            setStartTime(StandardTextFieldState(text = habitModel.habitStartTime))
                            setHabitColor(habitModel.habitColor)
                            setNotifyTypeState(habitModel.notify)
                            Log.e("id", habitModel.toString())
                        }
                    } catch (e: Exception) {
                        onError("Something went wrong")
                    }

                }
                getResetListById(habitId)
            }
    }

    private val _currentHabitId = mutableStateOf(-1)
    val currentHabitId: State<Int> = _currentHabitId

    private fun setHabitId(id: Int) {
        _currentHabitId.value = id
    }


    val calendar: Calendar = Calendar.getInstance()

    val messageBarState = mutableStateOf(MessageBarState())

    private val _titleState = mutableStateOf(StandardTextFieldState())
    val titleState: State<StandardTextFieldState> = _titleState

    fun setHabitTitle(state: StandardTextFieldState) {
        _titleState.value = state
    }

    private val _descState = mutableStateOf(StandardTextFieldState())
    val descState: State<StandardTextFieldState> = _descState

    fun setDescTitle(state: StandardTextFieldState) {
        _descState.value = state
    }

    private val _startTimeState = mutableStateOf(
        StandardTextFieldState(
            text = "${calendar.get(Calendar.DAY_OF_MONTH)}-${
                calendar.get(Calendar.MONTH)
            }-${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${
                calendar.get(
                    Calendar.MINUTE
                )
            }:${calendar.get(Calendar.SECOND)}"
        )
    )
    val startTimeState: State<StandardTextFieldState> = _startTimeState

    fun setStartTime(state: StandardTextFieldState) {
        _startTimeState.value = state
    }

    private val _habitIcon = mutableStateOf(R.drawable.chocolate)
    val habitIcon: State<Int> = _habitIcon

    fun setHabitIcon(icon: Int) {
        _habitIcon.value = icon
    }

    private val _habitColor = mutableStateOf("#e10000")
    val habitColor: State<String> = _habitColor

    fun setHabitColor(color: String) {
        _habitColor.value = color
    }

    private val _notifyTypeState = mutableStateOf(false)
    val notifyTypeState: State<Boolean> = _notifyTypeState

    fun setNotifyTypeState(state: Boolean) {
        _notifyTypeState.value = state
    }

    fun saveScheduleEvent(newHabit: (HabitModel) -> Unit) {
        viewModelScope.launch {

            try {
                if (currentHabitId.value != -1) {
                    val habitEvent = HabitModel(
                        habitId = currentHabitId.value,
                        habitTitle = titleState.value.text,
                        habitDesc = descState.value.text,
                        habitStartTime = startTimeState.value.text,
                        notify = notifyTypeState.value,
                        habitIcon = habitIcon.value,
                        habitColor = habitColor.value
                    )
                    val newId = habitUseCases.insertHabitUseCases.invoke(habitEvent).toInt()
                    habitUseCases.getHabitModelByIdUseCase.invoke(newId)?.let {
                        newHabit(it)
                    }
                } else {
                    val habitEvent = HabitModel(
                        habitTitle = titleState.value.text,
                        habitDesc = descState.value.text,
                        habitStartTime = startTimeState.value.text,
                        notify = notifyTypeState.value,
                        habitIcon = habitIcon.value,
                        habitColor = habitColor.value
                    )
                    val newId = habitUseCases.insertHabitUseCases.invoke(habitEvent).toInt()
                    habitUseCases.getHabitModelByIdUseCase.invoke(newId)?.let {
                        newHabit(it)
                    }
                }

            } catch (e: InvalidHabitException) {
                onError(e.message ?: "Couldn't save new Event")
            }
        }
    }

    fun insertResetDateAndTime(dateAndTime: ResetList) {
        viewModelScope.launch {
            habitUseCases.insertResetUseCase.invoke(dateAndTime)
        }
    }

    private var _resetList = mutableStateListOf<LocalDate>()
    val resetList: List<LocalDate> = _resetList

    // fetch the reset time and date list of that habit
    private fun getResetListById(habitId: Int) {
        val obj = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        // if list is not null or empty remove all items
        if (!resetList.isNullOrEmpty()) {
            _resetList.removeAll(resetList)
        }
        if (!resetTimeAndDateList.isNullOrEmpty()) {
            _resetTimeAndDateList.removeAll(resetTimeAndDateList)
        }
        viewModelScope.launch {
            habitUseCases.getHabitWithResetList.invoke(habitId).collect { lists ->
                lists[0].resetList.forEach { list ->

                    // storing reset date to mutableState
                    _resetList.add(list.habitResetDate)

                    // converting time and date to long and storing it to mutableList
                    obj.parse("${list.habitResetDate} ${list.habitResetTime}")?.time?.let { it ->
                        Log.e("list", it.toString())
                        _resetTimeAndDateList.add(
                            it
                        )
                    }
                }
            }
        }
    }

    // reset date and time list in long
    private var _resetTimeAndDateList = mutableStateListOf<Long>()
    val resetTimeAndDateList: List<Long> = _resetTimeAndDateList

    // current date for the reference of month calendar
    private val _currentDate = mutableStateOf<LocalDate>(LocalDate.now())
    val currentDate: State<LocalDate> = _currentDate

    fun setCurrentDate(date: LocalDate) {
        _currentDate.value = date
    }

    // selected date by user on month calendar
    private val _selectedDate = mutableStateOf<LocalDate>(LocalDate.now())
    val selectedDate: State<LocalDate> = _selectedDate

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    // trigger message bar state
    fun onError(message: String) {
        messageBarState.value.addError(message)
    }

    // trigger message bar state
    fun onSuccess(message: String) {
        messageBarState.value.addSuccess(message)
    }

    // function to get maximum abstinence period time
    fun findMaximumAbstinencePeriod(): String {
        var max: Long = 0

        if (resetTimeAndDateList.isNotEmpty() && resetTimeAndDateList.size > 1) {
            var min = resetTimeAndDateList[0]
            resetTimeAndDateList.sortedDescending().forEachIndexed { index, timeAndDate ->
                Log.e("desc", timeAndDate.toString())
                if (timeAndDate <= min) {
                    min = timeAndDate
                } else {
                    if (index >= 1) {
                        max =
                            Math.max(
                                max,
                                timeAndDate - resetTimeAndDateList[index - 1]
                            )
                    }

                }
            }
        }
        // Calculate time difference in days using TimeUnit class
        val daysDifference: Long = TimeUnit.MILLISECONDS.toDays(max) % 365
        val yearsDifference: Long = TimeUnit.MILLISECONDS.toDays(max) / 365L
        val minutesDifference: Long = TimeUnit.MILLISECONDS.toMinutes(max) % 60
        val hoursDifference: Long = TimeUnit.MILLISECONDS.toHours(max) % 24
        // Show difference in years, in days, hours, minutes

        achievementDays.value = daysDifference.toInt()
        return "$yearsDifference year $daysDifference days $hoursDifference hours $minutesDifference minutes"
    }

    val achievementDays = mutableStateOf<Int>(0)

    // function to get average abstinence time
    fun findAvgAbstinenceTime(): String {

        val obj = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        var timerModel = TimerModel(0, 0, 0, 0)


        val calendar = Calendar.getInstance()
        val selectedTime = obj.parse(startTimeState.value.text)
        val currentTime = obj.parse(
            "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH)}-${
                calendar.get(Calendar.YEAR)
            } ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${
                calendar.get(
                    Calendar.SECOND
                )
            }"
        )
        Log.e("dib", selectedTime?.time?.let {
            currentTime?.time?.minus(it)
        }.toString())
        val timeDifference = selectedTime?.time?.let {
            currentTime?.time?.minus(it)
        }?.div(resetTimeAndDateList.size)

        if (timeDifference != null) {
            // Calculate time difference in days using TimeUnit class
            val daysDifference: Long = TimeUnit.MILLISECONDS.toDays(timeDifference) % 365
            // Calculate time difference in years using TimeUnit class
            val yearsDifference: Long = TimeUnit.MILLISECONDS.toDays(timeDifference) / 365L
            // Calculate time difference in seconds using TimeUnit class
            val secondsDifference: Long = TimeUnit.MILLISECONDS.toSeconds(timeDifference) % 60
            // Calculate time difference in minutes using TimeUnit class
            val minutesDifference: Long = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60
            // Calculate time difference in hours using TimeUnit class
            val hoursDifference: Long = TimeUnit.MILLISECONDS.toHours(timeDifference) % 24
            // Show difference in years, in days, hours, minutes, and s

            timerModel = TimerModel(
                daysDifference,
                hoursDifference,
                minutesDifference,
                secondsDifference
            )

        }
        return "${timerModel.days} days ${timerModel.hours} hours ${timerModel.minutes} minutes"

    }

    // delete selected item
    fun deleteItem() {
        viewModelScope.launch {
            currentHabitId.let { habitId ->
                habitUseCases.getHabitModelByIdUseCase.invoke(habitId.value)?.let {
                    habitUseCases.deleteHabitModelUseCase.invoke(
                        it
                    )
                }
            }
        }
    }

}