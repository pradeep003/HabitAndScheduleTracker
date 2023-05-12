package com.ftcoding.habitandscheduletracker.widget.util

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.HabitModel
import com.ftcoding.habitandscheduletracker.data.domain.models.habit.TimerModel
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen.components.calculateTimeDifference
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.OBJ
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HabitTrackerWidgetInfo (
    habitModel: HabitModel,
) {
    // action button
    var isEditActionButtonVisible by remember {
        mutableStateOf(false)
    }
    // animation duration
    val duration = 800


    var trackerTimer by remember {
        mutableStateOf(TimerModel(0, 0, 0, 0))
    }
    val calendar = Calendar.getInstance()

    // calculate in float about how much time left to complete achievement
    LaunchedEffect(key1 = Unit) {
        while (true) {

            val currentTime = OBJ.parse("${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${calendar.get(Calendar.SECOND)}}")

            val lastResetTime = OBJ.parse(habitModel.habitLastResetTime)?.time

            trackerTimer = calculateTimeDifference(lastResetTime?.let { currentTime?.time?.minus(it) }
                ?: 0)

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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = habitModel.habitColor.hexColorToIntColor)
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

                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }
}