package com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen

import android.content.Context
import android.util.Log
import kotlinx.coroutines.cancel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit.HabitUseCases
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.OBJ
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitTrackerGlanceStateDefinition
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitWithGlanceIdModel
import com.ftcoding.habitandscheduletracker.widget.habit_widget.ui.HabitTrackerWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitUseCases: HabitUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    init {
        // get All the event list
        getAllHabitList()
        getUserState()
    }

//    private val _list = mutableStateOf<List<HabitModel>>(emptyList())
//    val list: State<List<HabitModel>> = _list

    private val _mList = mutableStateListOf<HabitModel>()
    val mList : List<HabitModel> = _mList

    private fun getAllHabitList() {
        viewModelScope.launch {
            habitUseCases.getAllHabitModelList.invoke().collect { list ->
                _mList.removeAll(mList)
                _mList.addAll(list)
            }
        }
    }

    // state for preference glance data
    private var prefDataState = mutableListOf<HabitWithGlanceIdModel>()

    // save new reset time in database and update new last reset time in app glance widget
    fun insertResetDateAndTime(context: Context, habitModel: HabitModel, dateAndTime: ResetList) {
        val widgetManager = GlanceAppWidgetManager(context = context)

        viewModelScope.launch {
            habitUseCases.insertResetUseCase.invoke(dateAndTime)
            // pattern to save "dd-MM-yyyy HH:mm"
            habitUseCases.insertHabitUseCases.invoke(habitModel.copy(habitLastResetTime = "${dateAndTime.habitResetDate.dayOfMonth}-${dateAndTime.habitResetDate.monthValue}-${dateAndTime.habitResetDate.year} ${dateAndTime.habitResetTime.hour}:${dateAndTime.habitResetTime.minute}:0"))
            // refresh the list to see the new changes
            getAllHabitList()

            //  get all glance ids
            val glanceIds = widgetManager.getGlanceIds(HabitTrackerWidget::class.java)

            // get preference data stored
            glanceIds.forEach { glanceId ->
                prefDataState =
                    getAppWidgetState(context, HabitTrackerGlanceStateDefinition, glanceId).prefData.toMutableList()

                prefDataState.forEachIndexed { index, habitWithGlanceIdModel ->
                    // if glance id and habit id is current then update the new last reset time
                    if (habitWithGlanceIdModel.glanceId == glanceId.hashCode() && habitWithGlanceIdModel.list?.habitId == habitModel.habitId) {

                        updateAppWidgetState(
                            context,
                            HabitTrackerGlanceStateDefinition,
                            glanceId
                        ) { pref ->
                            val newHabitModel =
                                habitUseCases.getHabitModelByIdUseCase.invoke(habitModel.habitId)
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

    fun deleteHabit(habitModel: HabitModel) {
        viewModelScope.launch {
            habitUseCases.deleteHabitModelUseCase.invoke(habitModel)
        }
    }

    // get user setting state
    private val _userState = mutableStateOf(User())
    val userState: State<User> = _userState
    private fun getUserState() {
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

    // reset date and time list in long
    private var _resetTimeAndDateList = mutableStateListOf<Long>()
    val resetTimeAndDateList: List<Long> = _resetTimeAndDateList

    // fetch the reset time and date list of that habit
    fun getResetListById(habitId: Int) {
        val obj = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        viewModelScope.launch {
            habitUseCases.getHabitWithResetList.invoke(habitId).collect { lists ->

                if (lists.isNotEmpty()) {

                    lists[0].resetList.forEach { list ->
                        Log.e("item", list.toString())
                        // storing reset date to mutableState

                        // converting time and date to long and storing it to mutableList

                        obj.parse("${list.habitResetDate} ${list.habitResetTime}")?.time?.let { it ->
                            if (!resetTimeAndDateList.contains(it)) {
                                _resetTimeAndDateList.add(
                                    it
                                )
                            }
                        }

                    }
                }
            }
        }
    }


}