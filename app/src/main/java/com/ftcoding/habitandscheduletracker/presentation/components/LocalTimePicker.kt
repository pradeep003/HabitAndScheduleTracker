package com.ftcoding.habitandscheduletracker.presentation.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.LocalTime
import java.util.*


@Composable
fun LocalTimePicker (
    context: Context,
    preSelectedTime: LocalTime? = null,
    preSelectedTimeCallback: (LocalTime)-> Unit,
    selectedTime: (LocalTime) -> Unit
) {

    val calendar = Calendar.getInstance()
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    if (preSelectedTime != null) {
        // if time is already selected them show that pre selected time in timepicker
//        val zonedDateTime = ZonedDateTime.of(preSelectedTime)
//        calendar.time = Date(preSelectedTime.)
        hour = preSelectedTime.hour
        minute = preSelectedTime.minute

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
                val newTime = LocalTime.of(hour, minute)
                selectedTime(newTime)
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