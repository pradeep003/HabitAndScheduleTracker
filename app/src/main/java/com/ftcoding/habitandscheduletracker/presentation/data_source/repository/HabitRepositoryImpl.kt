package com.ftcoding.habitandscheduletracker.presentation.data_source.repository

import com.ftcoding.habitandscheduletracker.data.dao.HabitDao
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation.HabitWithResetList
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow

class HabitRepositoryImpl (
    private val dao: HabitDao
        ) : HabitRepository {

    override  fun getAllHabitData(): Flow<List<HabitModel>> {
        return dao.getAllHabitList()
    }


    override suspend fun getHabitById(id: Int): HabitModel? {
        return dao.getHabitById(id)
    }

    override suspend fun insertNewHabit(habitModel: HabitModel): Long {
        return dao.insertHabit(habitModel)
    }

    override suspend fun deleteHabit(habitModel: HabitModel) {
        dao.deleteHabitModel(habitModel)
    }

    override suspend fun insertResetTime(resetTime: ResetList) {
        dao.insertResetList(resetTime)
    }

    override fun getHabitWithResetList(habitId: Int): Flow<List<HabitWithResetList>> {
        return dao.getHabitWithResetList(habitId)
    }
}