package com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ftcoding.habitandscheduletracker.R
import com.ftcoding.habitandscheduletracker.notification.scheduleHabitNotification
import com.ftcoding.habitandscheduletracker.notification.showHabitNotification
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen.components.HabitInfoCard
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    navController: NavController,
    viewModel: HabitViewModel = hiltViewModel()
) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tag"),
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
                    onClick = {
                        // navigate
                    navController.navigate(Screen.CreateHabitScreen.route)

                    },
                    modifier = Modifier.testTag("add")
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "add_icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            items(viewModel.list.value) { habit ->
                HabitInfoCard(
                    navController = navController,
                    habitModel = habit,
                    onDelete = { viewModel.deleteHabit(it) }) {
                    viewModel.insertResetDateAndTime(it)
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HabitScreenPreview() {

}