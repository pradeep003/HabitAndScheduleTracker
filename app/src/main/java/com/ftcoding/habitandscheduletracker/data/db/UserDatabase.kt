package com.ftcoding.habitandscheduletracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ftcoding.habitandscheduletracker.data.dao.UserDao
import com.ftcoding.habitandscheduletracker.data.domain.models.type_converter.BitmapTypeConverter
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User

@Database(
    entities = [User::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(BitmapTypeConverter::class)
abstract class UserDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "user_db"
    }
}