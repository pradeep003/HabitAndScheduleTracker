package com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.InvalidHabitException
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.TimerModel
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit.HabitUseCases
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.presentation.util.state.MessageBarState
import com.ftcoding.habitandscheduletracker.presentation.util.state.StandardTextFieldState
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitTrackerGlanceStateDefinition
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitTrackerWidgetReceiver
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitWithGlanceIdModel
import com.ftcoding.habitandscheduletracker.widget.habit_widget.ui.HabitTrackerWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val habitUseCases: HabitUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    fun getHabitModelById(habitId: Int) {

        if (habitId != -1) {
            // if habitId != 0 than fetch the habitModel of given id
            // else user is going to create new habitModel
            getResetListById(habitId)
            viewModelScope.launch {
                try {
                    habitUseCases.getHabitModelByIdUseCase.invoke(habitId)?.also { habitModel ->
                        setHabitId(id = habitModel.habitId)
                        setHabitTitle(StandardTextFieldState(text = habitModel.habitTitle))
                        setDescTitle(StandardTextFieldState(text = habitModel.habitDesc))
                        setHabitIcon(habitModel.habitIcon)
                        setStartTime(StandardTextFieldState(text = habitModel.habitStartTime))
                        setHabitColor(habitModel.habitColor)
                        _lastTimeState.value = habitModel.habitLastResetTime
                        setNotifyTypeState(habitModel.notify)
                        getListSize(habitId)
                    }
                } catch (e: Exception) {
                    onError("Something went wrong")
                }

            }

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
                calendar.get(Calendar.MONTH) + 1
            }-${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${
                calendar.get(
                    Calendar.MINUTE
                )
            }:${calendar.get(Calendar.SECOND)}"
        )
    )
    val startTimeState: State<StandardTextFieldState> = _startTimeState

    private val _lastTimeState = mutableStateOf(
        "${calendar.get(Calendar.DAY_OF_MONTH)}-${
            calendar.get(Calendar.MONTH) + 1
        }-${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${
            calendar.get(
                Calendar.MINUTE
            )
        }:${calendar.get(Calendar.SECOND)}"
    )
    val lastTimeState: State<String> = _lastTimeState

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
                // if user edit habit event
                if (currentHabitId.value != -1) {
                    val habitEvent = HabitModel(
                        habitId = currentHabitId.value,
                        habitTitle = titleState.value.text,
                        habitDesc = descState.value.text,
                        habitStartTime = startTimeState.value.text,
                        habitLastResetTime = startTimeState.value.text,
                        notify = notifyTypeState.value,
                        habitIcon = habitIcon.value,
                        habitColor = habitColor.value
                    )
                    val newId = habitUseCases.insertHabitUseCases.invoke(habitEvent).toInt()
                    habitUseCases.getHabitModelByIdUseCase.invoke(newId)?.let {
                        // callback new habit model
                        newHabit(it)
                        // insert first reset time
                        habitUseCases.insertResetUseCase(
                            ResetList(
                                habitResetDate = LocalDate.now(),
                                habitResetTime = LocalTime.now(),
                                habitId = it.habitId
                            )
                        )
                    }
                } else {
                    // else make a new event
                    val habitEvent = HabitModel(
                        habitTitle = titleState.value.text,
                        habitDesc = descState.value.text,
                        habitStartTime = startTimeState.value.text,
                        habitLastResetTime = startTimeState.value.text,
                        notify = notifyTypeState.value,
                        habitIcon = habitIcon.value,
                        habitColor = habitColor.value
                    )
                    val newId = habitUseCases.insertHabitUseCases.invoke(habitEvent).toInt()
                    habitUseCases.getHabitModelByIdUseCase.invoke(newId)?.let {
                        newHabit(it)
                        // insert first reset time
                        habitUseCases.insertResetUseCase(
                            ResetList(
                                habitResetDate = LocalDate.now(),
                                habitResetTime = LocalTime.now(),
                                habitId = it.habitId
                            )
                        )
                    }
                }
            } catch (e: InvalidHabitException) {
                onError(e.message ?: "Couldn't save new Event")
            }
        }
    }

    // state for preference glance data
    private var prefDataState = mutableListOf<HabitWithGlanceIdModel>()

    // save new reset time in database and update new last reset time in app glance widget
    fun insertResetDateAndTime(context: Context, dateAndTime: ResetList) {
        val widgetManager = GlanceAppWidgetManager(context = context)
        viewModelScope.launch {
            val habitModel = habitUseCases.getHabitModelByIdUseCase.invoke(currentHabitId.value)
            habitUseCases.insertResetUseCase.invoke(dateAndTime)
            habitModel?.copy(habitLastResetTime = "${dateAndTime.habitResetDate.dayOfMonth}-${dateAndTime.habitResetDate.monthValue}-${dateAndTime.habitResetDate.year} ${dateAndTime.habitResetTime.hour}:${dateAndTime.habitResetTime.minute}:0")
                ?.let { habitUseCases.insertHabitUseCases.invoke(it) }
            getResetListById(currentHabitId.value)

            //  get all glance ids
            val glanceIds = widgetManager.getGlanceIds(HabitTrackerWidget::class.java)

            // get preference data stored
            glanceIds.forEach { glanceId ->
                prefDataState =
                    getAppWidgetState(context, HabitTrackerGlanceStateDefinition, glanceId).prefData.toMutableList()

                prefDataState.forEachIndexed { index, habitWithGlanceIdModel ->
                    // if glance id and habit id is current then update the new last reset time
                    if (habitWithGlanceIdModel.glanceId == glanceId.hashCode() && habitWithGlanceIdModel.list?.habitId == currentHabitId.value) {

                        updateAppWidgetState(
                            context,
                            HabitTrackerGlanceStateDefinition,
                            glanceId
                        ) { pref ->
                            val newHabitModel =
                                habitUseCases.getHabitModelByIdUseCase.invoke(currentHabitId.value)
                            val newModel = HabitWithGlanceIdModel(newHabitModel, glanceId.hashCode())
                            // remove current habitWithGlanceId model and add the new one with same glance id
                            prefDataState.remove(habitWithGlanceIdModel)
                            prefDataState.add(newModel)
                            //update
                            pref.copy(
                                prefData = prefDataState.toList()
                            )
                        }
                        HabitTrackerWidget().update(context, glanceId)
                    }
                }
            }
        }
    }

    private var _resetList = mutableStateListOf<LocalDate>()
    val resetList: List<LocalDate> = _resetList


    // reset date and time list in long
    private var _resetTimeAndDateList = mutableStateListOf<Long>()
    val resetTimeAndDateList: List<Long> = _resetTimeAndDateList


    // fetch the reset time and date list of that habit

    private fun getResetListById(habitId: Int) {
        val obj = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        // if list is not null or empty remove all items
        _resetList.removeAll(resetList)
        _resetTimeAndDateList.removeAll(resetTimeAndDateList)
        _resetTimeAndDateList.clear()

        viewModelScope.launch {
            habitUseCases.getHabitWithResetList.invoke(habitId).collect { lists ->
                if (lists.isNotEmpty()) {
                    lists[0].resetList.forEach { listItem ->

                        // storing reset date to mutableState
//                        _resetList.value = resetList.value.plus(listItem.habitResetDate)
                        _resetList.add(listItem.habitResetDate)

                        // converting time and date to long and storing it to mutableList
                        obj.parse("${listItem.habitResetDate} ${listItem.habitResetTime}")?.time?.let { it ->
                            _resetTimeAndDateList.add(
                                it
                            )
                        }
                    }
                }
            }
        }
    }

    val listSize = mutableStateOf(1)
    private fun getListSize(habitId: Int) {
        viewModelScope.launch {
            habitUseCases.getHabitWithResetList.invoke(habitId = habitId).collect {
                if (it.isNotEmpty()) {
                    if (it[0].resetList.isEmpty()) {
                        listSize.value = 1
                    }  else {
                        listSize.value = it[0].resetList.size
                    }
                }
            }
        }
    }

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

        var max = 0L
        // sort reset list ascending
        val sortedResetTime = resetTimeAndDateList.sorted()
        sortedResetTime.forEachIndexed { index, time ->
            if (index + 1 < sortedResetTime.size) {
                // time difference between two interval
                val temp = sortedResetTime[index + 1] - time
                // if time difference is greater than max
                if (temp > max) {
                    max = temp
                }
            } else {
                // if index is last than calculate time diff with current time
                val temp = Calendar.getInstance().timeInMillis - sortedResetTime.last()
                if (temp > max) {
                    max = temp
                }
            }
        }
        // Calculate time difference in days using TimeUnit class
        val daysDifference: Long = TimeUnit.MILLISECONDS.toDays(max) % 365
        val yearsDifference: Long = TimeUnit.MILLISECONDS.toDays(max) / 365L
        val minutesDifference: Long = TimeUnit.MILLISECONDS.toMinutes(max) % 60
        val hoursDifference: Long = TimeUnit.MILLISECONDS.toHours(max) % 24
        // Show difference in years, in days, hours, minutes

        // highest achievement days
        achievementDays.value = daysDifference.toInt()
        return "$yearsDifference year $daysDifference days $hoursDifference hours $minutesDifference minutes"
    }

    val achievementDays = mutableStateOf(0)

    // function to get average abstinence time
    fun findAvgAbstinenceTime(): String {

        val timeDifference =
            (Calendar.getInstance().timeInMillis - resetTimeAndDateList.min()) / listSize.value

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

        val timerModel = TimerModel(
            daysDifference,
            hoursDifference,
            minutesDifference,
            secondsDifference
        )

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

    // get user setting state
    private val _userState = mutableStateOf(User())
    val userState: State<User> = _userState
    fun getUserState() {
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
}