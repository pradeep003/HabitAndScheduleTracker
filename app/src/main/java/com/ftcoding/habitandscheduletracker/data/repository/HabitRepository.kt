package com.ftcoding.habitandscheduletracker.data.repository

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation.HabitWithResetList
import kotlinx.coroutines.flow.Flow


interface HabitRepository {

    fun getAllHabitData(): Flow<List<HabitModel>>

    suspend fun getHabitById(id: Int): HabitModel?

    suspend fun insertNewHabit(habitModel: HabitModel): Long

    suspend fun deleteHabit(habitModel: HabitModel)

    suspend fun insertResetTime (resetTime: ResetList)

    fun getHabitWithResetList(habitId: Int): Flow<List<HabitWithResetList>>
}