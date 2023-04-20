package com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitCustomHoursModel
import com.ftcoding.habitandscheduletracker.presentation.components.TimePicker
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.longToTimestamp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCustomHoursDialog(
    context: Context,
    dialogState: (Boolean) -> Unit,
    saveCustomHoursDate: (HabitCustomHoursModel) -> Unit
) {

    val openDialog = remember {
        mutableStateOf(true)
    }
    val timePickerState = remember {
        mutableStateOf(false)
    }
    val selectedChipTime = remember {
        mutableStateOf<Long?>(null)
    }
    val customTimeList = remember {
        mutableStateListOf<Long>()
    }

    if (openDialog.value) {
        Dialog(
            onDismissRequest = {
                openDialog.value = false
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
                    .fillMaxHeight(0.8f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    ),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                    Text(
                        text = "Select custom hours",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(20.dp)
                    )

                    ExtendedFloatingActionButton(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        text = {
                            Text(text = "Select Time")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.AlarmAdd,
                                contentDescription = "add custom hour"
                            )
                        },
                        onClick = {
                            timePickerState.value = true
                        })

                    LazyColumn(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f)
                            .weight(0.7f),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        items(customTimeList) {

                            ElevatedAssistChip(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .padding(4.dp)
                                    .align(CenterHorizontally),
                                onClick = {
                                    selectedChipTime.value = it
                                    timePickerState.value = true
                                },
                                label = {
                                    Text(
                                        text = it.longToTimestamp,
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .weight(6f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.AlarmAdd,
                                        contentDescription = "add custom hour icon",
                                        modifier = Modifier
                                            .size(AssistChipDefaults.IconSize)
                                            .padding(horizontal = 4.dp)
                                            .weight(2f)
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "remove icon",
                                        modifier = Modifier
                                            .size(AssistChipDefaults.IconSize)
                                            .padding(horizontal = 4.dp)
                                            .weight(2f)
                                            .clickable {
                                                customTimeList.remove(it)
                                            }
                                    )
                                }
                            )

                        }
                    }

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        // throw a callback of false state when cancel button is pressed
                        dialogState(false)
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
                        // save customHoursData in database and close dialog
                        val data = HabitCustomHoursModel(
                            customHourListTime = ArrayList(customTimeList.toList())
                        )
                        saveCustomHoursDate(data)
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
                    preSelectedTime = selectedChipTime.value,
                    onCancelDialog = {
                        timePickerState.value = false
                    },
                    preSelectedTimeCallback = { oldValue ->
                        customTimeList.remove(oldValue)
                    }
                ) {
                    customTimeList.add(it)
                    timePickerState.value = false
                    selectedChipTime.value = null
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectCustomHourScreenPreview() {
    val context = LocalContext.current
    SelectCustomHoursDialog(context = context, dialogState = {}, saveCustomHoursDate = {})
}