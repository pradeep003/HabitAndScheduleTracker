package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository

class GetHabitModelByIdUseCase (
    private val repository: HabitRepository
        ) {

    suspend operator fun invoke(id: Int) : HabitModel? {
        return repository.getHabitById(id)
    }
}