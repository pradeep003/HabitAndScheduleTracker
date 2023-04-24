package com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit.HabitUseCases
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
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

    private val _list = mutableStateOf<List<HabitModel>>(emptyList())
    val list: State<List<HabitModel>> = _list


    private fun getAllHabitList() {
        viewModelScope.launch {
            habitUseCases.getAllHabitModelList.invoke().collect {list ->
                    _list.value = list

            }
        }
    }

    fun insertResetDateAndTime(dateAndTime: ResetList) {
        viewModelScope.launch {
            habitUseCases.insertResetUseCase.invoke(dateAndTime)
        }
    }

    fun deleteHabit(habitModel: HabitModel) {
        viewModelScope.launch {
            habitUseCases.deleteHabitModelUseCase.invoke(habitModel)
        }
    }

    // get user setting state
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
}