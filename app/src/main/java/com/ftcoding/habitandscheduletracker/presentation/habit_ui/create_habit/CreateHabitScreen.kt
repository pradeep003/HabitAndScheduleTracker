package com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit


import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.notification.scheduleHabitNotification
import com.ftcoding.habitandscheduletracker.presentation.components.*
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.presentation.util.state.*
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    navController: NavController,
    habitId: Int = -1,
    viewModel: CreateHabitViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    // dialog state
    val dialogState = remember {
        DialogsStateHandle()
    }

    // show error message state
    val messageBarState = remember {
        viewModel.messageBarState.value
    }

    // check whether notification permission is given or not
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

    // start Launcher for notification permission
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

    // fetch habit model if user pass id
    LaunchedEffect(key1 = true) {
        viewModel.getHabitModelById(habitId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(

            modifier = Modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = "Create Habit",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            navigationIcon = {
                // navigate to habit screen
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
                // saved habit into database
                IconButton(onClick = {
                        viewModel.saveScheduleEvent() {
                            if (it.notify) {
                                // if sdk version is over 33 then ask for permission
                                // only schedule notification if user give notification permission
                                if (hasNotificationPermission) {
                                    context.scheduleHabitNotification(it)
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
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

        // display message bar state ui when new message
        MessageBar(messageBarState)

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {

            item {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.titleState.value.text,
                        onValueChange = {
                            viewModel.setHabitTitle(StandardTextFieldState(text = it))
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = {
                            Text(
                                text = "Habit Title",
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

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.descState.value.text,
                        onValueChange = {
                            viewModel.setDescTitle(StandardTextFieldState(text = it))
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = {
                            Text(
                                text = "Encouragement",
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


                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (viewModel.currentHabitId.value != 0) {
                                    viewModel.onError("Can't change start date and time")
                                } else {
                                    dialogState.setDialogState(DialogState.StartTimePickerDialog(true))

                                }
                            },
                        value = viewModel.startTimeState.value.text,
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

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onBackground,
                                MaterialTheme.shapes.small
                            )
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "notification icon",
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Text(
                            text = "Notification",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        )

                        RadioButton(selected = viewModel.notifyTypeState.value, onClick = {
                            if (!viewModel.notifyTypeState.value && !hasNotificationPermission) {
                                dialogState.setDialogState(DialogState.PermissionDialogState(true))
                            }

                            viewModel.setNotifyTypeState(!viewModel.notifyTypeState.value)
                        })
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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
                                    viewModel.habitColor.value.hexColorToIntColor,
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
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                }
            }

            iconSelectUi(viewModel)
        }




    }

    // custom icon and color dialog box
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
                    viewModel.setHabitColor(it)
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

    // open date picker and make showDialog to false
    // save new selected date to viewModel
    if (dialogState.startTimePickerDialogState) {

        StartDateAndTimePicker(context = context, onCancelDialog = {
            // make timeAndDatePicker dialog state false
            dialogState.setDialogState(DialogState.StartTimePickerDialog(false))
        }, selectedDate = {
            viewModel.setStartTime(StandardTextFieldState(text = it))
        })
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

}

private fun LazyListScope.iconSelectUi(
    viewModel: CreateHabitViewModel
) {
    item {

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
                        if (habitIcon.icon == viewModel.habitIcon.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                    Card(
                        modifier = Modifier
                            .clickable {
                                // set new icon in viewmodel
                                viewModel.setHabitIcon(habitIcon.icon)
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

@Composable
fun StartDateAndTimePicker(
    context: Context,
    onCancelDialog: () -> Unit,
    selectedDate: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    calendar.time = Date()


    val datePickerDialog = DatePickerDialog(context)
    datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
        calendar.set(year, month, dayOfMonth)
        onCancelDialog()
    }

    datePickerDialog.show()
    datePickerDialog.setCancelable(false)
    datePickerDialog.setCanceledOnTouchOutside(false)
    datePickerDialog.setOnDismissListener {

        val timePickerDialog = TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)

                selectedDate(
                    "${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH)+1}-${
                        calendar.get(
                            Calendar.YEAR
                        )
                    } ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}:${
                        calendar.get(
                            Calendar.SECOND
                        )
                    }"
                )
            }, hour, minute, false
        )
        timePickerDialog.show()
        timePickerDialog.setCancelable(false)
        timePickerDialog.setCanceledOnTouchOutside(false)
    }
    datePickerDialog.setOnCancelListener {
        onCancelDialog()
    }

}

//@Preview(showBackground = true)
//@Composable
//fun CreateHabitScreenPreview(
//) {
//
//    CreateHabitScreen(navController = rememberNavController())
//}
