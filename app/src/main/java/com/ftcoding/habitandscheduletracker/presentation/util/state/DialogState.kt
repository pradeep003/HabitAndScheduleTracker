package com.ftcoding.habitandscheduletracker.presentation.util.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class DialogState {
    object SelectCustomHoursDialog : DialogState()
    object SelectCustomDaysDialog : DialogState()
    object SelectCustomWeeksDialog : DialogState()
    data class StartTimePickerDialog(val state: Boolean) : DialogState()
    data class EndTimePickerDialog(val state: Boolean) : DialogState()
    data class StartDatePickerDialog(val state: Boolean) : DialogState()
    data class PermissionDialogState(val state: Boolean) : DialogState()
    data class IconSelectionDialogState(val state: Boolean) : DialogState()
    data class ColorSelectionDialogState(val state: Boolean) : DialogState()
    data class SelectGoalDialog(val state: Boolean) : DialogState()
    data class SelectRingtoneDialog(val state: Boolean): DialogState()
}


class DialogsStateHandle {

    var iconDialogState by mutableStateOf(false)
        private set

    var colorDialogState by mutableStateOf(false)
        private set

    var endTimePickerDialogState by mutableStateOf(false)
    private set

    var startTimePickerDialogState by mutableStateOf(false)
        private set

    var startDatePickerDialogState by mutableStateOf(false)
        private set

    var selectCustomHoursDialogState by mutableStateOf(false)
        private set

    var selectCustomDaysDialogState by mutableStateOf(false)
    private set

    var selectCustomWeeksDialogState by mutableStateOf(false)
        private set

    var permissionDialogState by mutableStateOf(false)
    private set

    var selectGoalDialogState by mutableStateOf(false)
    private set

    var selectRingtoneDialog by mutableStateOf(false)
    private set


    fun setDialogState(dialogState: DialogState) {
        when(dialogState) {
            is DialogState.IconSelectionDialogState -> {
                iconDialogState = dialogState.state
            }
            is DialogState.ColorSelectionDialogState -> {
                colorDialogState = dialogState.state
            }
            is DialogState.EndTimePickerDialog -> {
                endTimePickerDialogState = dialogState.state
            }
            is DialogState.StartTimePickerDialog -> {
                startTimePickerDialogState = dialogState.state
            }
            is DialogState.SelectCustomHoursDialog -> {
                selectCustomHoursDialogState = !selectCustomHoursDialogState
            }
            is DialogState.SelectCustomDaysDialog -> {
                selectCustomDaysDialogState = !selectCustomDaysDialogState
            }
            is DialogState.SelectCustomWeeksDialog -> {
                selectCustomWeeksDialogState = !selectCustomWeeksDialogState
            }
            is DialogState.PermissionDialogState -> {
                permissionDialogState = dialogState.state
            }
            is DialogState.SelectGoalDialog -> {
                selectGoalDialogState = dialogState.state
            }
            is DialogState.StartDatePickerDialog -> {
                startDatePickerDialogState = dialogState.state
            }
            is DialogState.SelectRingtoneDialog -> {
                selectRingtoneDialog = dialogState.state
            }
        }
    }
}