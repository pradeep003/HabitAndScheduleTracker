package com.ftcoding.habitandscheduletracker.presentation.setting_ui.components

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ftcoding.habitandscheduletracker.data.domain.models.data.MusicModel
import com.ftcoding.habitandscheduletracker.presentation.setting_ui.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRingtoneScreen(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var mediaPlayer =
        MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

    val isPermissionGranted = remember {
        mutableStateOf(false)
    }

    // request permission launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // if permission granted than get all audio files
        if (isGranted) {
            isPermissionGranted.value = true
            viewModel.initializeListIfNeeded(context)
        }
    }


    // fetch audio file from device
    LaunchedEffect(true) {

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                isPermissionGranted.value = true
                viewModel.initializeListIfNeeded(context)
            }
            else -> {
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    BackHandler {
        mediaPlayer.stop()
        mediaPlayer.release()
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        TopAppBar(

            modifier = Modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = "Select Ringtone",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            navigationIcon = {
                // navigate to previous screen
                IconButton(onClick = {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    navController.popBackStack()

                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "arrow_back_icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    // save the new ringtone uri
                    viewModel.saveNewRingtone(viewModel.ringtone.value.contentUri.toString())
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    navController.popBackStack()
                }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "done_icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )

        if (isPermissionGranted.value) {


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {

                //  get all audio file from device
                items(viewModel.musicModelList.toList()) { musicModel ->


                    Row(modifier = Modifier.fillMaxWidth()) {

                        Icon(
                            imageVector = if (viewModel.ringtone.value.songId == musicModel.songId && mediaPlayer.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayCircle,
                            contentDescription = "play icon",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.clickable {

                                if (mediaPlayer.isPlaying) {

                                    if (musicModel.contentUri == viewModel.ringtone.value.contentUri) {
                                        mediaPlayer.stop()
                                        viewModel.setNewRingtone(MusicModel(Uri.EMPTY, -1, ""))
                                    } else {
                                        mediaPlayer.release()
                                        mediaPlayer =
                                            MediaPlayer.create(context, musicModel.contentUri)
                                        mediaPlayer.start()
                                        viewModel.setNewRingtone(musicModel)
                                    }

                                } else {
                                    mediaPlayer.release()
                                    mediaPlayer = MediaPlayer.create(context, musicModel.contentUri)
                                    mediaPlayer.start()
                                    viewModel.setNewRingtone(musicModel)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = musicModel.songTitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (viewModel.ringtone.value.songId == musicModel.songId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .clickable {
                                    viewModel.setNewRingtone(musicModel)
                                }
                        )
                    }
                }
            }
        } else {
            PermissionDeniedUi()
        }
    }
}

@Composable
private fun PermissionDeniedUi() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            )
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "display icon",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(20.dp)
                .size(50.dp)
        )

        Text(
            text = "Read local device files permission denied. Can't access to audio files. Please give permission.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "To give permission press go to setting button",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // navigate to setting screen
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

        Spacer(modifier = Modifier.height(20.dp))
    }
}