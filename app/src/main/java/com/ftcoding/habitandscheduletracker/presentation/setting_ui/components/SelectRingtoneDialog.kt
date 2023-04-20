package com.ftcoding.habitandscheduletracker.presentation.setting_ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ftcoding.habitandscheduletracker.data.domain.models.data.MusicModel
import com.ftcoding.habitandscheduletracker.presentation.setting_ui.SettingViewModel

@Composable
fun SelectRingtoneDialog(
    viewModel: SettingViewModel = hiltViewModel(),
    onCancel: ()-> Unit
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initializeListIfNeeded(context)
    }

    Column(modifier = Modifier
        .fillMaxHeight(0.8f)
        .fillMaxWidth()
        .padding(12.dp)
        .background(MaterialTheme.colorScheme.background)
    ) {


        Text(text = "Select an ringtone", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            Log.e("list", viewModel.musicModelList.toList().toString())
            items(viewModel.musicModelList.toList()) { musicModel ->

                // if song_id is equal to current song
                if (viewModel.ringtone.value.songId == musicModel.songId) {

                    Text(
                        text = musicModel.songTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        )
                    )
                } else {

                    Text(
                        text = musicModel.songTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.shapes.medium
                            )
                            .clickable {
                                viewModel.setNewRingtone(musicModel)
                            }
                    )

                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp)
        ) {
            Text(
                text = "CANCEL",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onCancel()
                    }
            )
            Text(
                text = "SAVE",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        // save new ringtone
//                        viewModel.saveNewRingtone(viewModel.ringtone.value)
                        // close the dialog
                        onCancel()
                    }
            )
        }
    }
}