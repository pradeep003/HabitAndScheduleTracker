package com.ftcoding.habitandscheduletracker.presentation.util.state

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import com.ftcoding.habitandscheduletracker.R

class ExposedDropMenuStateHolder {

    var frequencyEnabled by mutableStateOf(false)
    var notifyTypeEnabled by mutableStateOf(false)
    var size by mutableStateOf(Size.Zero)
    val frequencyIcon: Int
    @Composable get() = if (frequencyEnabled) {
        R.drawable.ic_arrow_up
    } else {
        R.drawable.ic_arrow_down
    }

    val notifyTypeIcon: Int
        @Composable get() = if (notifyTypeEnabled) {
            R.drawable.ic_arrow_up
        } else {
            R.drawable.ic_arrow_down
        }

    fun onFrequencyEnabled(newValue: Boolean) {
        frequencyEnabled = newValue
    }

    fun onNotifyTypeEnabled(newValue: Boolean) {
        notifyTypeEnabled = newValue
    }

    fun onSize(newValue : Size) {
        size = newValue
    }
}

@Composable
fun rememberExposedMenuStateHolder() = remember {
    ExposedDropMenuStateHolder()
}