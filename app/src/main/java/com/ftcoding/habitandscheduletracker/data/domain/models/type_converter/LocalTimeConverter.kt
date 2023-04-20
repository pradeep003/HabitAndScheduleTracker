package com.ftcoding.habitandscheduletracker.data.domain.models.type_converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*

class LocalTimeConverter {

    @TypeConverter
    fun localTimeToString(time: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        return time.format(formatter)
    }

    @TypeConverter
    fun stringToLocalTime(str: String): LocalTime {
        return LocalTime.parse(str)
    }
}