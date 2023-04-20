package com.ftcoding.habitandscheduletracker.presentation.data_source.repository

import com.ftcoding.habitandscheduletracker.data.dao.UserDao
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl (
    private val dao: UserDao
        ) : UserRepository {
    override fun getUser(): Flow<List<User>> {
        return dao.getUser()
    }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }
}