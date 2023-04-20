package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.InvalidHabitException
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository

class InsertHabitUseCases (
    private val repository: HabitRepository
        ) {

    @Throws(InvalidHabitException::class)
    suspend operator fun invoke(habitModel: HabitModel) : Long {
        if (habitModel.habitTitle.isBlank()) {
            throw InvalidHabitException("Title can't be empty")
        } else if (habitModel.habitIcon == 0) {
            throw InvalidHabitException("Please select an icon")
        } else if (habitModel.habitDesc.isBlank()) {
            throw InvalidHabitException("Write something to encourage you")
        }

        return repository.insertNewHabit(habitModel)
    }
}