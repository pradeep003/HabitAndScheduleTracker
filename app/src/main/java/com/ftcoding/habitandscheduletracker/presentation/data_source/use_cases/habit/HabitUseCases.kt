package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit

import com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation.HabitWithResetList

data class HabitUseCases(
    val insertHabitUseCases: InsertHabitUseCases,
    val deleteHabitModelUseCase: DeleteHabitModelUseCase,
    val getAllHabitModelList: GetAllHabitModelList,
    val getHabitModelByIdUseCase: GetHabitModelByIdUseCase,
    val insertResetUseCase: InsertResetTimeUseCase,
    val getHabitWithResetList: GetAllHabitWithResetList
)
