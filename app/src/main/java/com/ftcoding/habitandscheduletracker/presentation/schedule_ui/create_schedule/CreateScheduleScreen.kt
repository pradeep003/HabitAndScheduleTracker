package com.ftcoding.habitandscheduletracker.presentation.schedule_ui.create_schedule

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.notification.scheduleNotification
import com.ftcoding.habitandscheduletracker.presentation.components.*
import com.ftcoding.habitandscheduletracker.presentation.util.Constants
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.localTimeToTimestamp
import com.ftcoding.habitandscheduletracker.presentation.util.state.*
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import com.ftcoding.habitandscheduletracker.util.HabitConstants.NOTIFY_TYPE
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScheduleScreen(
    navController: NavController,
    eventId: Int = -1,
    viewModel: CreateScheduleViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    // message info bar
    val messageBarState = remember {
        viewModel.messageBarState.value
    }

    // state for handling dialog
    val dialogState = remember {
        DialogsStateHandle()
    }

    // drop down menu state
    val stateHolder = rememberExposedMenuStateHolder()

    // notification permission state
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }

    // fetch event model if user pass id
    LaunchedEffect(key1 = true) {
        viewModel.getEventById(eventId)
    }


    Column(modifier = Modifier.fillMaxWidth()) {

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasNotificationPermission = isGranted
                // Permission is granted. Continue the action or workflow in your app.
                if (!isGranted) {
                    // if no permission then open a dialog box to explain user why we need permission
                    dialogState.setDialogState(DialogState.PermissionDialogState(true))
                }
            }
        )

        TopAppBar(

            modifier = Modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = "Create Schedule",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            navigationIcon = {
                // navigate to previous screen
                IconButton(onClick = {
                    navController.popBackStack()

                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "arrow_back_icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    // save the new schedule event
                    viewModel.saveScheduleEvent() { event ->
                        // if sdk version is over 33 then ask for permission
                        // only schedule notification if user give notification permission
                        if (hasNotificationPermission) {
                            context.scheduleNotification(
                                isAlarm = event.alarmType,
                                id = event.eventId,
                                eventName = event.name,
                                eventDesc = event.description,
                                eventStartHour = event.start.hour,
                                eventStartMin = event.start.minute,
                                eventIcon = event.icon,
                                repeatDayList = event.repeatDayList.toIntArray()
                            )
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    }

                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "done_icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        )

        // handle error message info
        MessageBar(messageBarState)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "Select Color",
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Right
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onBackground,
                                MaterialTheme.shapes.small
                            )
                            .background(
                                viewModel.eventState.value.color.hexColorToIntColor,
                                MaterialTheme.shapes.small
                            )
                            .clickable {
                                dialogState.setDialogState(
                                    DialogState.ColorSelectionDialogState(
                                        true
                                    )
                                )
                            }
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    // event title
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel.eventState.value.name,
                        onValueChange = {
                            viewModel.setScheduleTitle(StandardTextFieldState(text = it))
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = {
                            Text(
                                text = "Schedule Title",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "habit_title_icon",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(MaterialTheme.colorScheme.onBackground)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // event description
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.eventState.value.description,
                        onValueChange = {
                            viewModel.setDescTitle(StandardTextFieldState(text = it))
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = "habit_description_icon",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        singleLine = false,
                        minLines = 5,
                        colors = TextFieldDefaults.textFieldColors(MaterialTheme.colorScheme.onBackground)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // event start time
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                dialogState.setDialogState(DialogState.StartTimePickerDialog(true))
                            },
                        value = viewModel.eventState.value.start.localTimeToTimestamp,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = {
                            Text(
                                text = "Start Time",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = "habit_start_time_icon",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            MaterialTheme.colorScheme.onBackground,
                            disabledTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // event end time
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                dialogState.setDialogState(DialogState.EndTimePickerDialog(true))
                            },
                        value = viewModel.eventState.value.end.localTimeToTimestamp,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = {
                            Text(
                                text = "End Time",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.TimerOff,
                                contentDescription = "habit_end_time_icon",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            MaterialTheme.colorScheme.onBackground,
                            disabledTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    // repeat day list selection ui
                    Row(
                        modifier = Modifier
                            .width(intrinsicSize = IntrinsicSize.Max)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {

                        for ((index, value) in Constants.WEEK_OF_DAY_LIST.withIndex()) {
                            val selected =
                                viewModel.eventState.value.repeatDayList.contains(index + 1)

                            Card(
                                shape = MaterialTheme.shapes.medium,
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                ),
                                colors = CardDefaults.cardColors(containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background),
                                onClick = {
                                    viewModel.addOrRemoveWeekOfDay(index + 1)
                                }
                            ) {
                                Text(
                                    text = value.substring(0..2),
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .align(CenterHorizontally)
                                )
                            }
                        }
                    }

                    // event notify
                    Box {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    stateHolder.onNotifyTypeEnabled(!(stateHolder.notifyTypeEnabled))
                                }
                                .onGloballyPositioned {
                                    stateHolder.onSize(it.size.toSize())
                                },
                            value = if (viewModel.eventState.value.alarmType) NOTIFY_TYPE[1] else NOTIFY_TYPE[0],
                            onValueChange = { }, enabled = false,
                            readOnly = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            label = {
                                Text(
                                    text = "Notify Type",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "habit_notification_icon",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(stateHolder.notifyTypeIcon),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    contentDescription = "drop down menu icon"
                                )
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                MaterialTheme.colorScheme.onBackground,
                                disabledTextColor = MaterialTheme.colorScheme.onBackground
                            ),

                            )
                        DropdownMenu(expanded = stateHolder.notifyTypeEnabled,
                            onDismissRequest = { stateHolder.onNotifyTypeEnabled(false) },
                            modifier = Modifier
                                .width(with(LocalDensity.current)
                                { stateHolder.size.width.toDp() }
                                )
                        ) {
                            NOTIFY_TYPE.forEachIndexed { _: Int, s: String ->
                                DropdownMenuItem(text = { Text(text = s) }, onClick = {
                                    viewModel.setNotifyTypeState(s)
                                    stateHolder.onNotifyTypeEnabled(false)
                                })
                            }
                        }
                    }

                }

            }

            iconSelectUi(viewModel)
        }

    }
// pop up dialog to show permission status
    if (dialogState.permissionDialogState) {
        PermissionDialog(
            context = context,
            message = stringResource(R.string.notification_permission_denied_message),
            icon = Icons.Filled.Warning,
            closeDialogState = {
                dialogState.setDialogState(DialogState.PermissionDialogState(false))
            }
        )
    }

// open date picker and make showDialog to false
// save new selected date to viewModel
    if (dialogState.startTimePickerDialogState) {
        LocalTimePicker(
            context = context,
            preSelectedTimeCallback = {},
            selectedTime = {
                viewModel.setStartTime(it)
            },
            preSelectedTime = viewModel.eventState.value.start
        )
        dialogState.setDialogState(DialogState.StartTimePickerDialog(false))
    }
    if (dialogState.endTimePickerDialogState) {
        LocalTimePicker(
            context = context,
            preSelectedTimeCallback = {},
            selectedTime = {
                viewModel.setEndTime(it)
            },
            preSelectedTime = viewModel.eventState.value.end
        )
        dialogState.setDialogState(DialogState.EndTimePickerDialog(false))
    }

// custom color dialog box
// if state is true open dialog box
    if (dialogState.colorDialogState) {

        Dialog(onDismissRequest = {
            dialogState.setDialogState(DialogState.ColorSelectionDialogState(false))
        }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.large
                    )
            ) {

                ColorPicker(newSelectedColor = {
                    viewModel.setEventColor(it)
                })

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {

                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                dialogState.setDialogState(
                                    DialogState.ColorSelectionDialogState(
                                        false
                                    )
                                )
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

        }
    }


}


fun LazyListScope.iconSelectUi(
    viewModel: CreateScheduleViewModel
) {

    item {

        // select Icon ui
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = "Select Icon",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Right,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 45.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.height(500.dp)
            ) {

                items(HabitConstants.HABIT_ICON_LIST) { habitIcon ->
                    val backgroundColor =
                        if (habitIcon.icon == viewModel.eventState.value.icon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                    Card(
                        modifier = Modifier
                            .clickable {
                                // set new icon in viewModel
                                viewModel.setEventIcon(habitIcon.icon)
                            },
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {

                        Icon(
                            painter = painterResource(id = habitIcon.icon),
                            contentDescription = habitIcon.name,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                        )

                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateScheduleScreenPreview() {
    CreateScheduleScreen(navController = rememberNavController())
}