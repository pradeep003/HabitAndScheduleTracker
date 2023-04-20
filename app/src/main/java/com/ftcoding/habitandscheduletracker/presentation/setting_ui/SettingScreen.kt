package com.ftcoding.habitandscheduletracker.presentation.setting_ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.presentation.components.ColorPicker
import com.ftcoding.habitandscheduletracker.presentation.setting_ui.components.SelectRingtoneDialog
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.presentation.util.state.DialogState
import com.ftcoding.habitandscheduletracker.presentation.util.state.DialogsStateHandle
import java.net.URI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {

    // state for handling dialog
    val dialogState = remember {
        DialogsStateHandle()
    }

    // holding user image state
    val imageUri = remember {
        mutableStateOf<String?>(viewModel.user.value.image)
    }

    val context = LocalContext.current

    // launcher for selecting new image from device

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)
            viewModel.saveNewUserProfileImage(uri)
            imageUri.value = uri.toString()

        }
    }

    viewModel.getUserProfileImage()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        TopAppBar(

            modifier = Modifier
                .fillMaxWidth()
                .testTag("tag"),
            title = {
                Text(
                    text = "Hi ,",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {



//                 if image uri is null show app icon else show saved image
                if (viewModel.user.value.image != null) {
                    Log.e("not null", URI.create(viewModel.getUserProfileImage()).toString())
                    val painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = Uri.parse(viewModel.user.value.image))
                            .placeholder(R.drawable.app_icon)
                            .error(R.drawable.app_icon)
                            .setParameter("timestamp", imageUri.value, null)
                            .build()
                    )
                    Image(
                        painter = painter,
                        contentDescription = "user profile image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = "user profile image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Hi",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        modifier = Modifier,
                        value = viewModel.user.value.userName,
                        onValueChange = {
                            viewModel.saveNewUserName(it)
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = {
                            Text(
                                text = "Username",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            MaterialTheme.colorScheme.onBackground,
                            disabledTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                // on pressing open images from device
                Button(
                    elevation = ButtonDefaults.buttonElevation(8.dp),
//                    onClick = { launcher.launch("image/*") }
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                ) {
                    Text(
                        text = "Pick image",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }


        // refresh notification after reboot

        Button(
            elevation = ButtonDefaults.buttonElevation(8.dp),
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(54.dp)
                .padding(horizontal = 8.dp)
        ) {
            Text(text = "Refresh Notification", style = MaterialTheme.typography.bodyMedium)
        }

        Text(
            text = "Note - Some device kills notification after reboot. You can reschedule them again by pressing the reset notification button",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(54.dp)
                .padding(horizontal = 8.dp)
        )

        // selected ringtone for alarm
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(54.dp)
                .clickable {
                    dialogState.setDialogState(DialogState.SelectRingtoneDialog(true))
                    Log.e("click", dialogState.selectRingtoneDialog.toString())

                }
                .padding(horizontal = 8.dp),
            value = if (viewModel.ringtone.value.songId < 0) "App default ringtone" else viewModel.ringtone.value.songTitle,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            textStyle = MaterialTheme.typography.bodyMedium,
            label = {
                Text(
                    text = "Ringtone for alarm",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = "ringtone",
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

        // check user want vibration on alarm
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//                .height(54.dp)
//                .border(
//                    1.dp,
//                    MaterialTheme.colorScheme.onBackground,
//                    MaterialTheme.shapes.small
//                )
//                .padding(vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Icon(
//                imageVector = Icons.Filled.Vibration,
//                contentDescription = "vibration icon",
//                modifier = Modifier.padding(start = 8.dp)
//            )
//
//            Text(
//                text = "Alarm to vibrate",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier
//                    .padding(start = 16.dp)
//                    .weight(1f)
//            )
//
//            // get the alarmVibration state from viewModel
//            // if user change value save it to viewModel state
//            RadioButton(selected = viewModel.alarmVibration.value, onClick = {
//                viewModel.setAlarmVibration(!viewModel.alarmVibration.value)
//            })
//        }
//
//        // check user want vibration on notification
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//                .height(56.dp)
//                .background(MaterialTheme.colorScheme.surface)
//                .border(
//                    1.dp,
//                    MaterialTheme.colorScheme.onBackground,
//                    MaterialTheme.shapes.small
//                )
//                .padding(vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Icon(
//                imageVector = Icons.Filled.Vibration,
//                contentDescription = "vibration icon",
//                modifier = Modifier.padding(start = 8.dp)
//            )
//
//            Text(
//                text = "Notification to vibrate",
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier
//                    .padding(start = 16.dp)
//                    .weight(1f)
//            )
//
//            // get the alarmVibration state from viewModel
//            // if user change value save it to viewModel state
//            RadioButton(selected = viewModel.alarmVibration.value, onClick = {
//                viewModel.setAlarmVibration(!viewModel.alarmVibration.value)
//            })
//        }

        // set theme primary color for app
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Text(
                text = "Select primary color",
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
                        viewModel.user.value.themeColor.hexColorToIntColor,
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

        // select dark mode or light mode
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp)
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground,
                    MaterialTheme.shapes.small
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Filled.DarkMode,
                contentDescription = "Dark mode icon",
                modifier = Modifier.padding(start = 8.dp)
            )

            Text(
                text = "Dark Mode",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )

            // get the alarmVibration state from viewModel
            // if user change value save it to viewModel state
            RadioButton(selected = viewModel.user.value.darkMode, onClick = {
                viewModel.setDarkMode(!viewModel.user.value.darkMode)
            })
        }

        // should notification display (notification permission)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {

                Icon(
                    imageVector = Icons.Filled.NotificationsActive,
                    contentDescription = "notification active icon",
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = "Display Notification",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                )
            }

            // navigate to  system app  settings

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }) {
                Text(
                    text = "Go to setting",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onError
                )
            }

        }

    }


// if ringtone picker dialog state is true open selectRingtoneDialog
    if (dialogState.selectRingtoneDialog) {

        Dialog(onDismissRequest = {
            dialogState.setDialogState(
                DialogState.SelectRingtoneDialog(
                    false
                )
            )
        }) {
            SelectRingtoneDialog {
                dialogState.setDialogState(DialogState.SelectRingtoneDialog(false))
            }
        }

    }

// if color picker dialog state is true open colorPickerDialog
    if (dialogState.colorDialogState) {

        Dialog(onDismissRequest = {
            dialogState.setDialogState(
                DialogState.ColorSelectionDialogState(
                    false
                )
            )
        }) {

            // get the callback for new selected color and save it to viewModel
            ColorPicker(newSelectedColor = {
                viewModel.setPrimaryColor(it)
            })

        }
    }


}

fun getBitmapFromVectorDrawable(context: Context): Bitmap {
    val drawable = ContextCompat.getDrawable(context, R.drawable.trophy)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}