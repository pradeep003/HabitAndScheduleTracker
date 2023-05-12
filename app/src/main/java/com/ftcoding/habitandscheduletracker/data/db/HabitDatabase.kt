package com.ftcoding.habitandscheduletracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ftcoding.habitandscheduletracker.data.dao.HabitDao
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.type_converter.BitmapTypeConverter
import com.ftcoding.habitandscheduletracker.data.domain.models.type_converter.ListTypeConverter
import com.ftcoding.habitandscheduletracker.data.domain.models.type_converter.LocalDateConverter
import com.ftcoding.habitandscheduletracker.data.domain.models.type_converter.LocalTimeConverter

@Database(
    entities = [HabitModel::class, ResetList::class],
    version = 17,
    exportSchema = false
)
@TypeConverters(ListTypeConverter::class, LocalDateConverter::class, LocalTimeConverter::class, BitmapTypeConverter::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao

    companion object {
        const val DATABASE_NAME = "habit_db"
    }
}