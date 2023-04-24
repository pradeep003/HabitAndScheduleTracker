package com.ftcoding.habitandscheduletracker.presentation.setting_ui.components

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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

    // fetch audio file from device
    LaunchedEffect(true) {
        viewModel.initializeListIfNeeded(context)
    }

    BackHandler  {
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

                            Log.e("media player", mediaPlayer.isPlaying.toString())
                            if (mediaPlayer.isPlaying) {

                                if (musicModel.contentUri == viewModel.ringtone.value.contentUri) {
                                    mediaPlayer.stop()
                                    viewModel.setNewRingtone(MusicModel(Uri.EMPTY, -1, ""))
                                } else {
                                    mediaPlayer.release()
                                    mediaPlayer = MediaPlayer.create(context, musicModel.contentUri)
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
    }
}