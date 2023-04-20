package com.ftcoding.habitandscheduletracker.data.domain.models.type_converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class LocalDateConverter {

    @TypeConverter
    fun localDateToString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

        return date.format(formatter)
    }

    @TypeConverter
    fun stringToLocalDate(str: String): LocalDate {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault()))
    }
}