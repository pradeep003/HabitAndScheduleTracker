package com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ftcoding.habitandscheduletracker.presentation.components.MessageBar
import com.ftcoding.habitandscheduletracker.presentation.util.state.MessageBarState
import java.sql.Time
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGoalDialogBox(
    targetTime: Long = 0,
    newTargetTime: (Long) -> (Unit)
) {

    val openDialog = remember {
        mutableStateOf(true)
    }

    val messageBarState = remember {
        MessageBarState()
    }

    val dayTarget = remember {
        mutableStateOf(TimeUnit.MILLISECONDS.toDays(targetTime))
    }
    val hourTarget = remember {
        mutableStateOf(TimeUnit.MILLISECONDS.toHours(targetTime) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(targetTime)))
    }
    val minTarget = remember {
        mutableStateOf(TimeUnit.MILLISECONDS.toMinutes(targetTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(targetTime)))
    }

    if (openDialog.value) {
        Dialog(
            onDismissRequest = { /*TODO*/ },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                // message bar
                MessageBar(messageBarState = messageBarState)


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {

                    TextField(
                        value = dayTarget.value.toString(),
                        onValueChange = {
                            if (it.isBlank()) {
                                dayTarget.value = 0
                            } else if (it.toLong() > 21) {
                                // show error in message bar
                                messageBarState.addError("Days can't be greater than 21")
                            } else {
                                dayTarget.value = it.toLong()
                            }
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.3f)
                    )

//                    val days = 23
//                    val hour = 23
//                    val min = 58
//
//                    val milli = TimeUnit.DAYS.toMillis(days.toLong()) + TimeUnit.HOURS.toMillis(hour.toLong()) + TimeUnit.MINUTES.toMillis(min.toLong())
//
//                    val newDay = TimeUnit.MILLISECONDS.toDays(milli)
//                    val newHour = TimeUnit.MILLISECONDS.toHours(milli) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milli))
//                    val newMin = TimeUnit.MILLISECONDS.toMinutes(milli) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milli))
//
//                    Log.e("new milli is : $milli", "$newDay day $newHour hour $newMin min")

                    Text(
                        text = "days",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {

                    TextField(
                        value = hourTarget.value.toString(),
                        onValueChange = {
                            if (it.isBlank()) {
                                hourTarget.value = 0
                            } else if (it.toLong() > 24) {
                                // show error in message bar
                                messageBarState.addError("Hour can't be greater than 24 ")
                            } else {
                                hourTarget.value = it.toLong()
                            }
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.3f)
                    )

                    Text(
                        text = "hours",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {

                    TextField(
                        value = minTarget.value.toString(),
                        onValueChange = {
                            if (it.isBlank()) {
                                minTarget.value = 0
                            } else if (it.toLong() > 60) {
                                // show error in message bar
                                messageBarState.addError("Minute can't be greater 60")
                            } else {
                                minTarget.value = it.toLong()
                            }
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.3f)
                    )

                    Text(
                        text = "Minutes",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
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
                        // callback new Target value
                        // close dialog
                        newTargetTime(dayTarget.value + hourTarget.value + minTarget.value )
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectGoalDialogBoxPreview() {
    SelectGoalDialogBox()  {

    }
}