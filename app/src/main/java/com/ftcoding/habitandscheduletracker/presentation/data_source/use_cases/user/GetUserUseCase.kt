package com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user

import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase (
    private val repository: UserRepository
        ) {

     operator fun invoke() : Flow<List<User>> {
        return repository.getUser()
    }
}