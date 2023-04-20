package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation.HabitWithResetList
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow

class GetAllHabitWithResetList (
    private val repository: HabitRepository
        ) {

    operator fun invoke(habitId: Int): Flow<List<HabitWithResetList>> {
        return repository.getHabitWithResetList(habitId)
    }
}

