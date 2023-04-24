package com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_detail_screen

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.ResetList
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit.CreateHabitViewModel
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Screen
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Composable
fun HabitDetailScreen(
    navController: NavController,
    habitId: Int = -1,
    viewModel: CreateHabitViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getHabitModelById(habitId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(Screen.CreateHabitScreen.route)
    ) {

        HabitDetailMonthCalendar(
            habitIcon = viewModel.habitIcon.value,
            newSelectedDate = {}
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                // navigate to create screen with id
                                navController.navigate("${Screen.CreateHabitScreen.route}?habitId=${viewModel.currentHabitId.value}")
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "edit icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = "EDIT",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }

                    // Delete the habit Model and navigate back
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                //  delete this item and navigate to habit screen
                                viewModel.deleteItem()
                                navController.popBackStack()
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "delete icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = "DELETE",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)

                        )
                    }
                }

            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.trophy),
                        contentDescription = "achievement",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(64.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Text(
                            text = "Achievement ( 21 day challenge )",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (viewModel.achievementDays.value < 21) "${viewModel.achievementDays.value} day / 21 days" else "21 days challenge completed",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Row(
                    modifier = Modifier.padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Event,
                        contentDescription = "start date icon",
                        tint = viewModel.habitColor.value.hexColorToIntColor,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                    )

                    Column(Modifier.fillMaxWidth()) {

                        Text(
                            text = "The day you quit",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = viewModel.startTimeState.value.text,
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Row(
                    modifier = Modifier.padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.HourglassBottom,
                        contentDescription = "max abstinence icon",
                        tint = viewModel.habitColor.value.hexColorToIntColor,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                    )

                    Column(Modifier.fillMaxWidth()) {

                        Text(
                            text = "Maximum abstinence period",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (viewModel.resetTimeAndDateList.size > 1) viewModel.findMaximumAbstinencePeriod() else "0year 0 days 0 hours 0 minutes",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Row(
                    modifier = Modifier.padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.HourglassFull,
                        contentDescription = "average icon",
                        tint = viewModel.habitColor.value.hexColorToIntColor,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                    )

                    Column(Modifier.fillMaxWidth()) {

                        Text(
                            text = "Average abstinence period",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (viewModel.resetList.size > 1) viewModel.findAvgAbstinenceTime() else "0 days 0 hours 0 minutes",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Row(
                    modifier = Modifier.padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Analytics,
                        contentDescription = "reset info icon",
                        tint = viewModel.habitColor.value.hexColorToIntColor,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                    )

                    Column(Modifier.fillMaxWidth()) {

                        Text(
                            text = "Number of timer resets",
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = viewModel.resetList.size.toString(),
                            textAlign = TextAlign.Right,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

    }

}

@Composable
fun HabitDetailMonthCalendar(
    context: Context = LocalContext.current,
    viewModel: CreateHabitViewModel = hiltViewModel(),
    date: LocalDate = LocalDate.now(),
    habitIcon: Int,
    newSelectedDate: (LocalDate) -> Unit
) {

    val dayInCurrentMonth = remember {
        mutableStateOf(0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // shows previous month when user clicks and change selectedValue
                IconButton(onClick = {
//                    currentDate.value = currentDate.value.minusMonths(1)
                    viewModel.setCurrentDate(viewModel.currentDate.value.minusMonths(1))
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowLeft,
                        contentDescription = "previous icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "${viewModel.currentDate.value.month.name} ${viewModel.currentDate.value.year}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(weight = 1f),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                // shows next month when user clicks and change selectedValue
                IconButton(onClick = {
//                    currentDate.value = currentDate.value.plusMonths(1)
                    viewModel.setCurrentDate(viewModel.currentDate.value.plusMonths(1))
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        contentDescription = "next icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .animateContentSize()
                    .padding(4.dp)
            ) {

                items(7) {
                    Text(
                        text = DayOfWeek.of(it + 1).toString().substring(0..2),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(2.dp)
                    )
                }

                // adding dayOfWeek to current month length
                val firstDayOfCurrentMonth =
                    LocalDate.of(
                        viewModel.currentDate.value.year,
                        viewModel.currentDate.value.month,
                        1
                    ).dayOfWeek.value - 1
                dayInCurrentMonth.value =
                    viewModel.currentDate.value.lengthOfMonth() + firstDayOfCurrentMonth

                items(dayInCurrentMonth.value) { day ->

                    if (day >= (firstDayOfCurrentMonth)) {

                        // subtracting firstDayOfCurrentMonth (which  i added early on) to get current dayOfMonth
                        val currentLocalDate =
                            LocalDate.of(
                                viewModel.currentDate.value.year,
                                viewModel.currentDate.value.month,
                                (day - firstDayOfCurrentMonth) + 1
                            )
                        // gets localDate for current lazy column item
                        Card(
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .padding(2.dp)
                                .clickable {
                                    viewModel.setSelectedDate(currentLocalDate)
                                    newSelectedDate(currentLocalDate)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (currentLocalDate.equals(
                                        viewModel.selectedDate.value
                                    )
                                ) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                            )
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(40.dp)
                            ) {


                                // if this day is user reset day than show icon else show day text
                                if (viewModel.resetList.contains(
                                        LocalDate.of(
                                            viewModel.currentDate.value.year,
                                            viewModel.currentDate.value.monthValue,
                                            (day - firstDayOfCurrentMonth) + 1
                                        )
                                    )
                                ) {
                                    Icon(
                                        painter = painterResource(id = habitIcon),
                                        contentDescription = "habit icon",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                } else {
                                    Text(
                                        text = currentLocalDate.dayOfMonth.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        color = if (currentLocalDate.equals(viewModel.selectedDate.value)) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // reset button
            Button(
                onClick = {

                    // open time picker dialog to select a time
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)
                    calendar.time = Date()
                    val timePickerDialog = TimePickerDialog(
                        context,
                        { _, hour: Int, minute: Int ->
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(Calendar.SECOND, 0)
                        }, hour, minute, false
                    )
                    timePickerDialog.show()
                    timePickerDialog.setCancelable(false)
                    timePickerDialog.setCanceledOnTouchOutside(false)

                    val resetItem =
                        ResetList(
                            habitResetDate = LocalDate.of(
                                viewModel.selectedDate.value.year,
                                viewModel.selectedDate.value.monthValue,
                                viewModel.selectedDate.value.dayOfMonth
                            ),
                            habitResetTime = LocalTime.of(
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                calendar.get(Calendar.SECOND)
                            ),
                            habitId = viewModel.currentHabitId.value
                        )

                    // save new reset time and date to database
                    viewModel.insertResetDateAndTime(resetItem)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Filled.RestartAlt,
                        contentDescription = "reset icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "RESET TIMER",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }


}

