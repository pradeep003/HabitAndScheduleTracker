package com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen.components

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.util.Log
import android.view.KeyEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.TimerModel
import com.ftcoding.habitandscheduletracker.presentation.components.MaterialTimePickers
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit.StartDateAndTimePicker
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Screen
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.OBJ
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.presentation.util.state.DialogState
import com.ftcoding.habitandscheduletracker.presentation.util.state.DialogsStateHandle
import com.ftcoding.habitandscheduletracker.presentation.util.state.StandardTextFieldState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HabitInfoCard(
    navController: NavController,
    habitModel: HabitModel,
    lastResetTime: Long,
    onDelete: (habitModel: HabitModel) -> Unit,
    resetTime: (reset: ResetList) -> Unit,
) {
    // action button
    var isEditActionButtonVisible by remember {
        mutableStateOf(false)
    }
    // animation duration
    val duration = 800

    // shows percentage according to achievement target
    var goalPercentage by remember {
        mutableStateOf(20f)
    }

    // size animation
    val size by animateFloatAsState(
        targetValue = goalPercentage,
        tween(durationMillis = 1000, delayMillis = 200, easing = LinearOutSlowInEasing)
    )

    var trackerTimer by remember {
        mutableStateOf(TimerModel(0, 0, 0, 0))
    }

    // dialog state
    val dialogState = remember {
        DialogsStateHandle()
    }

    // calculate in float about how much time left to complete achievement
    LaunchedEffect(key1 = Unit) {
        while (true) {
            val calendar = Calendar.getInstance()

            val currentTime = OBJ.parse("${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${calendar.get(Calendar.SECOND)}}")

            trackerTimer = calculateTimeDifference(currentTime?.time?.minus(lastResetTime) ?: 0)

            goalPercentage = trackerTimer.hours / 24f

            // refresh timer in 1 second
            delay(1000)

        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize(animationSpec = tween(500))
            .clickable {
                isEditActionButtonVisible = true
            },
        shape = MaterialTheme.shapes.extraLarge
    ) {


        Column(modifier = Modifier.fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {

                // will increase width as a progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth(size)
                        .fillMaxHeight()
                        .background(habitModel.habitColor.hexColorToIntColor)
                        .animateContentSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = habitModel.habitColor.hexColorToIntColor.copy(alpha = 0.5F))
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                        .clip(MaterialTheme.shapes.large)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = habitModel.habitIcon),
                            contentDescription = "habit icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(50.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = habitModel.habitTitle,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = habitModel.habitStartTime,
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        AnimatedContent(
                            targetState = trackerTimer.days,
                            transitionSpec = {
                                slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
                                    animationSpec = tween(durationMillis = duration)
                                ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
                                    animationSpec = tween(durationMillis = duration)
                                )
                            }
                        ) { days ->
                            Text(
                                text = days.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }

                        Text(
                            text = " day  ",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White

                        )

                        AnimatedContent(
                            targetState = trackerTimer.hours,
                            transitionSpec = {
                                slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
                                    animationSpec = tween(durationMillis = duration)
                                ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
                                    animationSpec = tween(durationMillis = duration)
                                )
                            }
                        ) { hours ->
                            Text(
                                text = hours.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.White

                            )
                        }

                        Text(
                            text = " hours  ",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White

                        )

                        AnimatedContent(
                            targetState = trackerTimer.minutes,
                            transitionSpec = {
                                slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
                                    animationSpec = tween(durationMillis = duration)
                                ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
                                    animationSpec = tween(durationMillis = duration)
                                )
                            }
                        ) { min ->
                            Text(
                                text = min.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.White

                            )
                        }

                        Text(
                            text = " minutes  ",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White

                        )

                        AnimatedContent(
                            targetState = trackerTimer.seconds,
                            transitionSpec = {
                                slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
                                    animationSpec = tween(durationMillis = duration)
                                ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
                                    animationSpec = tween(durationMillis = duration)
                                )
                            }
                        ) { sec ->
                            Text(
                                text = sec.toString(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                        Text(
                            text = " seconds  ",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.End,
                            color = Color.White

                        )

                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = if (trackerTimer.days <= 21) "${trackerTimer.days} days / 21 days" else "${
                                trackerTimer.days
                            } days",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Text(
                            text = "Progress",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .weight(1f)
                        )

                        AnimatedContent(
                            targetState = goalPercentage * 100,
                            transitionSpec = {
                                slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
                                    animationSpec = tween(durationMillis = duration)
                                ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> -height } + fadeOut(
                                    animationSpec = tween(durationMillis = duration)
                                )
                            }
                        ) { newTime ->
                            Text(
                                text = "${newTime.toBigDecimal()} %",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

            }

            // action button
            if (isEditActionButtonVisible) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            habitModel.habitColor.hexColorToIntColor,
                            shape = RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)
                        )
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(modifier = Modifier.width(8.dp))
                    // Calendar view
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "details icon",
                        tint = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .clickable {
                                // navigate to detail screen
                                navController.navigate("${Screen.HabitDetailScreen.route}/${habitModel.habitId}")
                            }
                            .testTag("detail_icon")
                    )
                    // edit
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "edit icon",
                        tint = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                // navigate to create habit screen with habit id
                                navController.navigate("${Screen.CreateHabitScreen.route}?habitId=${habitModel.habitId}")
                            }
                    )
                    // reset
                    Icon(
                        imageVector = Icons.Filled.LockReset,
                        contentDescription = "reset icon",
                        tint = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {

                                // open time picker dialog
                                dialogState.setDialogState(DialogState.StartTimePickerDialog(true))

                            }
                    )

                    // delete
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "delete icon",
                        tint = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onDelete(habitModel)
                            }
                    )
                    // close
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "close icon",
                        tint = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                isEditActionButtonVisible = false
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }

    if (dialogState.startTimePickerDialogState) {

        Dialog(onDismissRequest = { dialogState.setDialogState(DialogState.StartTimePickerDialog(false)) }, properties = DialogProperties(dismissOnClickOutside = false)) {

            val time = LocalTime.now()
            val state = rememberTimePickerState(initialHour = time.hour, initialMinute = time.minute)

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.large)) {
                Text(text = "Select Reset Time", modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp), style = MaterialTheme.typography.bodyMedium)

                TimePicker(state = state)

                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            dialogState.setDialogState(
                                DialogState.StartTimePickerDialog(
                                    false
                                )
                            )
                        }
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = {

                            val reset = ResetList(
                                habitResetDate = LocalDate.now(),
                                habitResetTime = LocalTime.of(state.hour, state.minute, 0),
                                habitId = habitModel.habitId
                            )
                            resetTime(reset)
                            dialogState.setDialogState(
                                DialogState.StartTimePickerDialog(
                                    false
                                )
                            )
                        }

                    ) { Text("OK") }
                }
            }

        }
    }
}

fun calculateTimeDifference(startTime: Long): TimerModel {


    // Calculate time difference in days using TimeUnit class
    val daysDifference: Long = TimeUnit.MILLISECONDS.toDays(startTime) % 365
    // Calculate time difference in years using TimeUnit class
    TimeUnit.MILLISECONDS.toDays(startTime) / 365L
    // Calculate time difference in seconds using TimeUnit class
    val secondsDifference: Long = TimeUnit.MILLISECONDS.toSeconds(startTime) % 60
    // Calculate time difference in minutes using TimeUnit class
    val minutesDifference: Long = TimeUnit.MILLISECONDS.toMinutes(startTime) % 60
    // Calculate time difference in hours using TimeUnit class
    val hoursDifference: Long = TimeUnit.MILLISECONDS.toHours(startTime) % 24
    // Show difference in years, in days, hours, minutes, and s


    return TimerModel(daysDifference, hoursDifference, minutesDifference, secondsDifference)
}


@Preview(showBackground = true)
@Composable
fun HabitInfoCardPreview() {

    HabitModel(
        habitId = 1,
        habitTitle = "Smoking and drinking",
        habitDesc = "I want to quite smoking and drinking and start my new life",
        habitStartTime = "1673371638847",
        habitLastResetTime = "1673371638847",
        habitIcon = com.ftcoding.habitandscheduletracker.R.drawable.chocolate,
        habitColor = "#0099e1",
    )
//    HabitInfoCard(habitModel = habitModel)
}