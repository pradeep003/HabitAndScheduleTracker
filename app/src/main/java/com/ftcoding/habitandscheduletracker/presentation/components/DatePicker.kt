package com.ftcoding.habitandscheduletracker.presentation.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun DatePicker(
    context: Context,
    selectedDate: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()


    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                selectedDate("$dayOfMonth-${month + 1}-$year")
            }, year, month, day


        )
    }
    datePickerDialog.show()

}
