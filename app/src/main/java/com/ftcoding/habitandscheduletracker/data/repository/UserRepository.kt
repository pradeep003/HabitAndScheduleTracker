package com.ftcoding.habitandscheduletracker.data.repository

import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

     fun getUser() : Flow<List<User>>

    suspend fun insertUser(user: User)
}