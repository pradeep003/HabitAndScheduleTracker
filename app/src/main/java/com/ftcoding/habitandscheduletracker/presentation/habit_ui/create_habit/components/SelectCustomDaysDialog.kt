package com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit.components

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitCustomDaysModel
import com.ftcoding.habitandscheduletracker.presentation.components.MessageBar
import com.ftcoding.habitandscheduletracker.presentation.components.TimePicker
import com.ftcoding.habitandscheduletracker.presentation.util.Constants
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.longToTimestamp
import com.ftcoding.habitandscheduletracker.presentation.util.state.MessageBarState
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCustomDaysDialog(
    context: Context,
    habitCustomDaysModel: HabitCustomDaysModel,
    dialogState: () -> Unit,
    saveCustomDaysData: (HabitCustomDaysModel) -> Unit
) {

    val openDialog = remember {
        mutableStateOf(true)
    }
    val timePickerState = remember {
        mutableStateOf(false)
    }
    val habitCustomDaysModelState = remember {
        mutableStateOf(habitCustomDaysModel)
    }
    val checkDayOfWeekButtonEnabled = remember {
        mutableStateOf(true)
    }
    val dayOfWeekList = remember {
        mutableStateListOf<String>()
    }
    val messageBarState = remember {
        MessageBarState()
    }

    habitCustomDaysModelState.value.let { dayOfWeekList.addAll(it.customDaysOfWeekList) }

    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
                dialogState()
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            )
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // message bar
                MessageBar(messageBarState = messageBarState)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Select custom Days",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(20.dp)
                    )

                    // switch to enabled day to repeat Selection
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Select days to repeat",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = !checkDayOfWeekButtonEnabled.value,
                            onCheckedChange = {
                                checkDayOfWeekButtonEnabled.value =
                                    !checkDayOfWeekButtonEnabled.value
                            },
                            thumbContent = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "radio button icon"
                                )
                            }
                        )
                    }


                    AnimatedVisibility(
                        visible = !checkDayOfWeekButtonEnabled.value,
                        enter = expandVertically(
                            animationSpec = tween(durationMillis = 300),
                            expandFrom = Alignment.Top
                        ),
                        exit = shrinkVertically(
                            animationSpec = tween(durationMillis = 300),
                            shrinkTowards = Alignment.Top
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            TextField(
                                value = habitCustomDaysModelState.value.customNumOfDays.toString(),
                                onValueChange = {
                                    if (it.isBlank()) {
                                        habitCustomDaysModelState.value =
                                            habitCustomDaysModelState.value.copy(customNumOfDays = 0)
                                    } else if (it.length > 2) {
                                        messageBarState.addError("Weeks can't be longer than 2 digit")
                                    } else {
                                        habitCustomDaysModelState.value =
                                            habitCustomDaysModelState.value.copy(customNumOfDays = it.toInt())
                                    }
                                },
                                textStyle = MaterialTheme.typography.bodyMedium,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(0.3f)
                            )

                            Text(
                                text = "days",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // switch to enabled weekOfDay Selection
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Select day of week",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = checkDayOfWeekButtonEnabled.value,
                            onCheckedChange = {
                                checkDayOfWeekButtonEnabled.value =
                                    !checkDayOfWeekButtonEnabled.value
                            },
                            thumbContent = {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "radio button icon"
                                )
                            }
                        )
                    }

                    // custom weekOfDay selection grid view
                    // shrink or expand on observing state of check button

                    AnimatedVisibility(
                        visible = checkDayOfWeekButtonEnabled.value,
                        enter = expandVertically(
                            animationSpec = tween(durationMillis = 300),
                            expandFrom = Alignment.Top
                        ),
                        exit = shrinkVertically(
                            animationSpec = tween(durationMillis = 300),
                            shrinkTowards = Alignment.Top
                        )
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 50.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {

                            items(Constants.WEEK_OF_DAY_LIST) { weekOfDay ->

                                // checking whether current weekOfDay is in dayOfWeekRepeatState
                                // change color for selected weekOfDay
                                val selected =
                                    dayOfWeekList.contains(weekOfDay)

                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 4.dp,
                                        pressedElevation = 8.dp
                                    ),
                                    colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background),
                                    onClick = {
                                        if (selected) dayOfWeekList.remove(weekOfDay) else dayOfWeekList.add(weekOfDay)
                                        habitCustomDaysModelState.value =
                                            habitCustomDaysModelState.value.copy(
                                                customDaysOfWeekList = ArrayList(dayOfWeekList)
                                            )
                                        dayOfWeekList.clear()

                                    }
                                ) {
                                    Text(
                                        text = weekOfDay,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp, vertical = 8.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    ElevatedAssistChip(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(4.dp)
                            .align(Alignment.CenterHorizontally),
                        elevation = AssistChipDefaults.assistChipElevation(4.dp),
                        onClick = {
                            // open time picker when user click on chip
                            timePickerState.value = true
                        },
                        label = {
                                Text(
                                    text = habitCustomDaysModelState.value.customDayTime.longToTimestamp,
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .weight(6f)
                                )

                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "add custom hour icon",
                                modifier = Modifier
                                    .size(AssistChipDefaults.IconSize)
                                    .padding(horizontal = 4.dp)
                                    .weight(2f)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "remove icon",
                                modifier = Modifier
                                    .size(AssistChipDefaults.IconSize)
                                    .padding(horizontal = 4.dp)
                                    .weight(2f)
                                    .clickable {
                                        timePickerState.value = true
                                    }
                            )
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        dialogState()
                        openDialog.value = false
                    }) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    TextButton(onClick = {
                        // save the data in HabitCustomDaysModel and send a callback
                        // close dialog
                        saveCustomDaysData(habitCustomDaysModelState.value)
                        dialogState()
                        openDialog.value = false
                    }) {
                        Text(
                            text = "Save",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                }

            }

            if (timePickerState.value) {
                TimePicker(
                    context = context,
                    preSelectedTime = Constants.getCurrentTimeInLong(),
                    onCancelDialog = {
                        timePickerState.value = false
                        Log.e("time pciker", timePickerState.value.toString())
                    },
                    preSelectedTimeCallback = {},
                    selectedTime = {
                        habitCustomDaysModelState.value = habitCustomDaysModelState.value.copy(customDayTime = it)
                        timePickerState.value = false
                    }
                )
            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun SelectCustomDaysDialogPreview(

) {
    val context = LocalContext.current
    val cal = Calendar.getInstance()
    SelectCustomDaysDialog(
        context = context,
        habitCustomDaysModel = HabitCustomDaysModel(
            customDayTime = cal.timeInMillis
        ),
        dialogState = {},
        saveCustomDaysData = {
            Log.e("days", it.toString())
        }
    )
}