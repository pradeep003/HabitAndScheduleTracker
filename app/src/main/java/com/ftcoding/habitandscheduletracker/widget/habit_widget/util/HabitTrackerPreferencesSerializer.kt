package com.ftcoding.habitandscheduletracker.widget.habit_widget.util

import androidx.datastore.core.Serializer
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitDataPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object HabitTrackerPreferencesSerializer : Serializer<HabitDataPreferences> {

    override val defaultValue: HabitDataPreferences
        get() = HabitDataPreferences()

    override suspend fun readFrom(input: InputStream): HabitDataPreferences {
        return try {
            Json.decodeFromString(
                deserializer = HabitDataPreferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: HabitDataPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = HabitDataPreferences.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}