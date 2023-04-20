package com.ftcoding.habitandscheduletracker.data.domain.models.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter {

    @TypeConverter
    fun stringToList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun listToString(string: String): List<String> {
        return Gson().fromJson(string, object : TypeToken<List<String>>(){}.type)
    }
    @TypeConverter
    fun stringToIntList(list: List<Int>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun intListToString(string: String): List<Int> {
        return Gson().fromJson(string, object : TypeToken<List<Int>>(){}.type)
    }
}
