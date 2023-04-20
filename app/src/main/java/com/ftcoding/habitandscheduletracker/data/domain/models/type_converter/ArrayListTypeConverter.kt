package com.ftcoding.habitandscheduletracker.data.domain.models.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ArrayListTypeConverter {

    @TypeConverter
    fun fromStringToListLong(value: String?): ArrayList<Long?>? {
        val listType: Type = object : TypeToken<ArrayList<Long?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListLongToString(list: ArrayList<Long?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToListString(value: String?): ArrayList<String?>? {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListStringToString(list: ArrayList<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

}