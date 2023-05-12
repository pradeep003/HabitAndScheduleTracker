package com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import com.ftcoding.habitandscheduletracker.widget.habit_widget.util.HabitTrackerPreferencesSerializer
import java.io.File

const val habitFileName = "app-setting.json"
val Context.habitDataStore by dataStore(habitFileName, HabitTrackerPreferencesSerializer)


object HabitTrackerGlanceStateDefinition  : GlanceStateDefinition<HabitDataPreferences> {


    override suspend fun getDataStore(
        context: Context,
        fileKey: String
    ): DataStore<HabitDataPreferences> = context.habitDataStore

    override fun getLocation(context: Context, fileKey: String): File = context.dataStoreFile(
        habitFileName
    )

}