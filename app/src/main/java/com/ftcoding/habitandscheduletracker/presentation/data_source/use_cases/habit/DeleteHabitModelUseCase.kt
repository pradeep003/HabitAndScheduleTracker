package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository

class DeleteHabitModelUseCase (
    private val repository: HabitRepository
        ) {

    suspend operator fun invoke(habitModel: HabitModel) {
        repository.deleteHabit(habitModel)
    }
}