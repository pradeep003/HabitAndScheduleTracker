package com.ftcoding.habitandscheduletracker.presentation.util

import android.annotation.SuppressLint
import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


object Constants {

    const val  POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATION"

    @SuppressLint("ConstantLocale")
    val OBJ = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

    val String.hexColorToIntColor
        get() = Color(parseColor(this))

    val LocalTime.localTimeToTimestamp: String
    get() {
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return this.format(formatter)
    }

    val Long.longToTimestamp: String
        get() {
            val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
            return formatter.format(this)
        }

    val Long.longToFullTimestamp: String
        get() {
            val formatter = SimpleDateFormat("d hh:mm aa", Locale.getDefault())
            return formatter.format(this)
        }

    val Long.longToHours: Int
        get() {
            val formatter = SimpleDateFormat("HH", Locale.getDefault())
            return formatter.format(this).toInt()
        }

    val Long.longToDateAndTime: String
    get() {
        val formatter = SimpleDateFormat("MMM d, yy hh:mm aa", Locale.getDefault())
        return formatter.format(this).toString()
    }

    val Long.longToMinutes: Int
        get() {
            val formatter = SimpleDateFormat("mm", Locale.getDefault())
            return formatter.format(this).toInt()
        }

    fun getCurrentTimeInLong() : Long {
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)
        return calendar.timeInMillis
    }

    val WEEK_OF_DAY_LIST = arrayListOf(
        "SUN", "MUN", "TUE", "WED", "THU", "FRI", "SAT"
    )
}
