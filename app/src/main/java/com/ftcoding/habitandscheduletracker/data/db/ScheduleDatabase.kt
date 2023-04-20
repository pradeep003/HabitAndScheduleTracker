package com.ftcoding.habitandscheduletracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ftcoding.habitandscheduletracker.data.dao.ScheduleDao
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Notes
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.ScheduleTrackerTime
import com.ftcoding.habitandscheduletracker.data.domain.models.type_converter.ListTypeConverter
import com.ftcoding.habitandscheduletracker.data.domain.models.type_converter.LocalTimeConverter

@Database(
    entities = [Event::class ,Notes::class, ScheduleTrackerTime::class],
    version = 13,
    exportSchema = false
)
@TypeConverters(LocalTimeConverter::class, ListTypeConverter::class)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract val scheduleDao: ScheduleDao

    companion object {
        const val DATABASE_NAME = "schedule_db"
    }
}