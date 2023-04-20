package com.ftcoding.habitandscheduletracker.di

import android.app.Application
import androidx.room.Room
import com.ftcoding.habitandscheduletracker.data.db.ScheduleDatabase
import com.ftcoding.habitandscheduletracker.data.repository.ScheduleRepository
import com.ftcoding.habitandscheduletracker.presentation.data_source.repository.ScheduleRepositoryImpl
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.schedule.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleModule {

    @Provides
    @Singleton
    fun provideScheduleDatabase(app: Application): ScheduleDatabase {
        return Room.databaseBuilder(
            app,
            ScheduleDatabase::class.java,
            ScheduleDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideScheduleRepository(db: ScheduleDatabase): ScheduleRepository {
        return ScheduleRepositoryImpl(db.scheduleDao)
    }

    @Provides
    @Singleton
    fun provideScheduleEventUseCases(repository: ScheduleRepository) : ScheduleUseCases {
        return ScheduleUseCases(
            getAllScheduleEvents = GetAllScheduleEvents(repository),
            getScheduleEventById = GetScheduleEventById(repository),
            insertScheduleEvent = InsertScheduleEvent(repository),
            deleteScheduleEvent = DeleteScheduleEvent(repository),
            getScheduleEventWithNotes = GetScheduleEventWithNotes(repository),
            getScheduleEventWithTrackerTimes = GetScheduleEventWithTrackerTimes(repository)
        )
    }
}