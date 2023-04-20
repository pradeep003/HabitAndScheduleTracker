package com.ftcoding.habitandscheduletracker.di

import android.app.Application
import androidx.room.Room
import com.ftcoding.habitandscheduletracker.data.db.HabitDatabase
import com.ftcoding.habitandscheduletracker.data.db.ScheduleDatabase
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.relation.HabitWithResetList
import com.ftcoding.habitandscheduletracker.data.repository.HabitRepository
import com.ftcoding.habitandscheduletracker.data.repository.ScheduleRepository
import com.ftcoding.habitandscheduletracker.presentation.data_source.repository.HabitRepositoryImpl
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.habit.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitModel {

    @Provides
    @Singleton
    fun provideHabitDatabase(app: Application): HabitDatabase {
        return Room.databaseBuilder(
            app,
            HabitDatabase::class.java,
            HabitDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideHabitRepository(db: HabitDatabase): HabitRepository {
        return HabitRepositoryImpl(db.habitDao)
    }

    @Provides
    @Singleton
    fun provideHabitUseCases(habitRepository: HabitRepository): HabitUseCases {
        return HabitUseCases(
            insertHabitUseCases = InsertHabitUseCases(habitRepository),
            deleteHabitModelUseCase = DeleteHabitModelUseCase(habitRepository),
            getAllHabitModelList = GetAllHabitModelList(habitRepository),
            getHabitModelByIdUseCase = GetHabitModelByIdUseCase(habitRepository),
            insertResetUseCase = InsertResetTimeUseCase(habitRepository),
            getHabitWithResetList = GetAllHabitWithResetList(habitRepository)
        )
    }
}