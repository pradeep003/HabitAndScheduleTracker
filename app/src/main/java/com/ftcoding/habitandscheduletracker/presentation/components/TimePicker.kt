package com.ftcoding.habitandscheduletracker.presentation.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.longToHours
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.longToMinutes
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.*

@Composable
fun TimePicker (
    context: Context,
    preSelectedTime: Long? = null,
    preSelectedTimeCallback: (Long)-> Unit,
    onCancelDialog: () -> Unit,
    selectedTime: (Long) -> Unit
    ) {

    val calendar = Calendar.getInstance()
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    if (preSelectedTime != null) {
        // if time is already selected them show that pre selected time in timepicker
        calendar.time = Date(preSelectedTime)
        hour = preSelectedTime.longToHours
        minute = preSelectedTime.longToMinutes

    } else {
        // else show the current time
        calendar.time = Date()
    }

    val timePickerDialog = remember {

        TimePickerDialog(
            context,
            {_, hour: Int, minute: Int ->

                // converting time to long
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val timeInLong = cal.timeInMillis
                selectedTime(timeInLong)
                if (preSelectedTime != null) {
                    preSelectedTimeCallback(preSelectedTime)
                }

            }, hour, minute,false
        )
    }

    timePickerDialog.updateTime(hour, minute)
    timePickerDialog.show()
    timePickerDialog.setCancelable(false)
    timePickerDialog.setCanceledOnTouchOutside(false)

    // Todo: implement on cancel dialog


}
