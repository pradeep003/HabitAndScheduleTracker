package com.ftcoding.habitandscheduletracker.di

import android.app.Application
import androidx.room.Room
import com.ftcoding.habitandscheduletracker.data.db.UserDatabase
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.data.repository.UserRepository
import com.ftcoding.habitandscheduletracker.presentation.data_source.repository.UserRepositoryImpl
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.GetUserUseCase
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.InsertUserUseCase
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModel {

    @Provides
    @Singleton
    fun provideUserDatabase(app: Application) : UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserRepository (database: UserDatabase) : UserRepository {
        return UserRepositoryImpl(database.userDao)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(userRepository: UserRepository) : UserUseCases {
        return UserUseCases(
            getUserUseCase = GetUserUseCase(userRepository),
            insertUserUseCase = InsertUserUseCase(userRepository)
        )
    }
}