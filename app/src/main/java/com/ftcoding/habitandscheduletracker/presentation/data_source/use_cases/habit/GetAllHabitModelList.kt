package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository
import kotlinx.coroutines.flow.Flow


class GetAllHabitModelList (
    private val repository: HabitRepository
        ) {

     operator fun invoke() : Flow<List<HabitModel>> {
        return repository.getAllHabitData()
    }
}