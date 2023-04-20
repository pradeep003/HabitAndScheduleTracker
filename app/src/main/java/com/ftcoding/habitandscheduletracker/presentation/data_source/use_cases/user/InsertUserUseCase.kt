package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user

import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.data.repository.UserRepository

class InsertUserUseCase (
    private val repository: UserRepository
        ) {

    suspend operator fun invoke(user: User) {
        repository.insertUser(user)
    }
}