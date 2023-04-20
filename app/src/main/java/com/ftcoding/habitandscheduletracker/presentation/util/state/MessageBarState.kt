package com.ftcoding.habitandscheduletracker.presentation.util.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MessageBarState {

    var success by mutableStateOf<String?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    internal var updated by mutableStateOf(false)
        private set

    fun addSuccess(message: String) {
        error = null
        success = message
        updated = !updated
    }

    fun addError(message: String) {
        error = message
        success = null
        updated = !updated
    }
}