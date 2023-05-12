package com.ftcoding.habitandscheduletracker.presentation.schedule_ui.schedule_screen

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.data.domain.models.schedule.Event
import com.ftcoding.habitandscheduletracker.presentation.components.DayWiseCalendar
import com.ftcoding.habitandscheduletracker.presentation.components.MonthCalendar
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Screen
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

private fun Modifier.eventData(event: Event) = this.then(EventDataModifier(event))

private class EventDataModifier(
    val event: Event
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = event
}

@OptIn(ExperimentalAnimationApi::class)
fun downColorTransition() =
    slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth }
    ) + fadeIn(
    ) with slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight }
    ) + fadeOut()

@OptIn(ExperimentalAnimationApi::class)
fun upColorTransition() =
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth }
    ) + fadeIn(
    ) with slideOutVertically(
        targetOffsetY = { fullHeight -> -fullHeight }
    ) + fadeOut()


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    viewModel: ScheduleViewModel = hiltViewModel()
) {

    // hold state of current calendar
    // true = dayWiseCalendar and false = MonthlyCalendar
    var currentCalendar by remember {
        mutableStateOf(true)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
        ) {

            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("schedule_tag"),
                title = {
                    Text(
                        text = "Hi ${viewModel.userState.value.userName},",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {

                    if (viewModel.userState.value.image != null) {
                        val painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = Uri.parse(viewModel.userState.value.image))
                                .build()
                        )

                        Image(
                            painter = painter,
                            contentDescription = "user profile image",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.app_icon),
                            contentDescription = "user profile image",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                },
                actions = {
                    IconButton(
                        modifier = Modifier.testTag("add"),
                        onClick = {
                            // navigate to create new schedule event screen
                            navController.navigate(Screen.CreateScheduleScreen.route)
                        }) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "add_new_schedule_icon",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )

            // toggle between full month calendar and day wise calendar
            AnimatedContent(targetState = currentCalendar,
                modifier = Modifier.fillMaxWidth(),
                content = { isVisible ->
                    if (isVisible) {
                        DayWiseCalendar(viewModel.selectedDate.value) { newSelectedDate ->
                            viewModel.changeSelectedDate(newSelectedDate)
                        }
                    } else {
                        MonthCalendar(viewModel.selectedDate.value) { newSelectedDate ->
                            viewModel.changeSelectedDate(newSelectedDate)
                        }
                    }
                },
                transitionSpec = {
                    slideInVertically(initialOffsetY = { int ->
                        -int
                    }, animationSpec = tween(500, easing = LinearEasing)) with slideOutVertically(
                        animationSpec = tween(500, easing = LinearEasing), targetOffsetY = { int ->
                            int
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
//                Icon(
//                    imageVector = Icons.Filled.ToggleOn,
//                    contentDescription = "toggle between week calendar and month calendar",
//                    tint = MaterialTheme.colorScheme.onBackground,
//                    modifier = Modifier
//                        .padding(start = 8.dp, end = 8.dp)
//                        .fillMaxWidth()
//                        .align(CenterHorizontally)
//                        .clickable {
//                            currentCalendar = !currentCalendar
//                        }
//                )

                Switch(
                    checked = currentCalendar,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp).fillMaxWidth().align(
                        CenterHorizontally).size(24.dp),
                    onCheckedChange = {
                        currentCalendar = !currentCalendar
                })

                Spacer(modifier = Modifier.height(16.dp))

                ShowScheduleUi(events = viewModel.list.value, navController)
            }
        }
    }

}

@Composable
fun ShowScheduleUi(events: List<Event>, navController: NavController) {
    val sortedEventsList = events.sortedBy { it.start }
    Schedule(
        events = sortedEventsList,
        hoursHeader = {
            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(horizontal = 8.dp),
            ) {
                Text(text = if (it > 12) "${it - 12} PM" else "$it AM")
            }
        },
        navController = navController
    )
}


@Composable
fun BasicEvent(
    event: Event,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {

    val eventDialogState = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 2.dp, bottom = 2.dp)
            .background(color = event.color.hexColorToIntColor, shape = MaterialTheme.shapes.medium)
            .padding(vertical = 4.dp, horizontal = 6.dp)
            .clickable {
                eventDialogState.value = true
            }
    ) {

        Row( modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = CenterVertically) {

            Image(painter = painterResource(id = event.icon), contentDescription = "event_icon", modifier = Modifier
                .size(32.dp)
                .padding(horizontal = 2.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${event.start} - ${event.end}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )

                Text(
                    text = event.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Text(
            text = event.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }

    if (eventDialogState.value) {

        // open dialog for this event
        Dialog(onDismissRequest = {
            eventDialogState.value = false
        }) {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 2.dp, bottom = 2.dp)
                    .background(
                        color = event.color.hexColorToIntColor,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
                    .clickable {

                    }
            ) {

                Row( modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = CenterVertically) {

                    Image(painter = painterResource(id = event.icon), contentDescription = "event_icon", modifier = Modifier
                        .size(32.dp)
                        .padding(horizontal = 2.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${event.start} - ${event.end}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )

                        Text(
                            text = event.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = event.description,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            event.color.hexColorToIntColor,
                            shape = RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)
                        )
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(modifier = Modifier.width(8.dp))

                    // edit
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "edit icon",
                        tint = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                // navigate to create habit screen with habit id
                                navController.navigate("${Screen.CreateScheduleScreen.route}?eventId=${event.eventId}")
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
                                // delete this schedule event
                                viewModel.deleteScheduleEvent(event)
                                eventDialogState.value = false
                            }
                    )
                    // close
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close icon",
                        tint = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                eventDialogState.value = false
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

        }
    }
}


@Composable
fun Schedule(
    events: List<Event>,
    hoursHeader: @Composable (index: Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val hoursHeaderLabels = @Composable {
        repeat(24) { hoursHeader(it) }
    }
    val eventContentsLabels = @Composable {
        events.forEach { event ->
            Box(modifier = Modifier.eventData(event)) {
                BasicEvent(event = event, navController = navController)
            }
        }
    }

    val hourHeight = 64.dp
    val width = 50.dp

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 50.dp
    val eventWidth = screenWidth - 50.dp

    Layout(
        contents = listOf(hoursHeaderLabels, eventContentsLabels),
        modifier = modifier
            .horizontalScroll(state = rememberScrollState())
            .verticalScroll(state = rememberScrollState())
            .drawBehind {
                repeat(24) {
                    drawLine(
                        Color.Black,
                        start = Offset(width.toPx(), (it + 1) * hourHeight.toPx()),
                        end = Offset(size.width, (it + 1) * hourHeight.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    )
    { (hoursHeaderMeasurable, eventContentMeasurable), constraints ->


        val hourLabelPlaceable = hoursHeaderMeasurable.map {
            it.measure(
                constraints.copy(
                    minHeight = hourHeight.toPx().toInt(),
                    maxHeight = hourHeight.toPx().toInt(),
                    maxWidth = width.toPx().toInt(),
                    minWidth = width.toPx().toInt()
                )
            )
        }


        val eventLabelPlaceable = eventContentMeasurable.map { measurable ->
            val event = measurable.parentData as Event

            var diversion = 0

            events.forEachIndexed { index, element ->
                if (element.eventId == event.eventId) {
                    val eventsIterator = events.subList(0, index)
                    eventsIterator.forEach {
                        val a = (event.start.hour..event.end.hour).filter { hour ->
                            (it.start.hour..it.end.hour).contains(hour)
                        }
                        if (a.isNotEmpty()) {
                            diversion += 1
                        }
                    }
                }

            }


            val eventDurationMinutes = ChronoUnit.MINUTES.between(event.start, event.end)
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).toInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minHeight = eventHeight,
                    maxHeight = eventHeight,
                    minWidth = eventWidth.toPx().toInt(),
                    maxWidth = eventWidth.toPx().toInt()
                )
            )
            Triple(placeable, event, diversion)
        }

        // hourHeight.toPx().toInt() * 25 will be the total height of layout
        layout(screenWidth.toPx().toInt() * 5, hourHeight.toPx().toInt() * 25) {

            var hourLabelY = 0

            hourLabelPlaceable.forEach { placeable ->
                placeable.place(0, hourLabelY)
                hourLabelY += placeable.height
            }

            eventLabelPlaceable.forEach { (placeable, event, div) ->

                val eventOffsetMinutes =
                    ChronoUnit.MINUTES.between(LocalTime.MIN, event.start)
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                placeable.place(width.toPx().toInt() + (div * eventWidth.toPx().toInt()), eventY)

            }
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun ShowUiPreview() {
//    ScheduleScreen(navController = rememberNavController())
//}
