package com.ftcoding.habitandscheduletracker.presentation.components

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.longToHours
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.longToMinutes
import java.text.SimpleDateFormat
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialTimePickers (
    state: TimePickerState,
    title: String = "Select Time",
    onCancel: ()-> Unit,
    onConfirm: (ResetList)-> Unit
) : Unit {

    val formatter = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
    }



}
