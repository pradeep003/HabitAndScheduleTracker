package com.ftcoding.habitandscheduletracker.widget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.presentation.MainActivity
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen.HabitViewModel
import com.ftcoding.habitandscheduletracker.presentation.ui.theme.HabitAndScheduleTrackerTheme
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.util.HabitConstants
import com.ftcoding.habitandscheduletracker.widget.habit_widget.callback.HabitTrackerRefreshCallback.Companion.HABIT_ID
import com.ftcoding.habitandscheduletracker.widget.habit_widget.callback.HabitTrackerRefreshCallback.Companion.UPDATE_ACTION
import com.ftcoding.habitandscheduletracker.widget.habit_widget.receiver.HabitTrackerWidgetReceiver
import com.ftcoding.habitandscheduletracker.widget.util.HabitTrackerWidgetInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// this activity will show all habit tracker model list to select
@AndroidEntryPoint
class SelectWidgetActivity : ComponentActivity() {

    @Inject
    lateinit var userUseCases: UserUseCases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            // state to observe setting changes
            val settingDetails = remember {
                mutableStateOf(User())
            }

            // observe the changes made in setting
            if (userUseCases.getUserUseCase.invoke()
                    .collectAsState(initial = listOf(User())).value.isNotEmpty()
            ) {
                userUseCases.getUserUseCase.invoke()
                    .collectAsState(initial = listOf(User())).value.first {
                        if (it.userId == HabitConstants.USER_ID) {
                            settingDetails.value = it
                            return@first true
                        } else {
                            settingDetails.value = User()
                            return@first false
                        }
                    }
            }

            HabitAndScheduleTrackerTheme(
                primaryColor = settingDetails.value.themeColor.hexColorToIntColor,
                darkTheme = if (isSystemInDarkTheme()) true else settingDetails.value.darkMode
            ) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    SelectHabitTrackerWidgetScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectHabitTrackerWidgetScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val activity = context as Activity
    val isSelected = remember {
        mutableStateOf(-1)
    }

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

                IconButton(
                    onClick = {

                        // if item selected then send broadcast with habit id
                        val intent = Intent(context, HabitTrackerWidgetReceiver::class.java).apply {
                            action = UPDATE_ACTION
                        }
                        intent.putExtra(HABIT_ID, isSelected.value)
                        context.sendBroadcast(intent)

                        // finish activity
                        activity.finishAffinity()
                    },
                    modifier = Modifier.testTag("close")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close_icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

            },
            actions = {
                IconButton(
                    onClick = {
                        // navigate to main activity
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
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

        if (viewModel.mList.isEmpty()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = "Please add a habit to track", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    // navigate to main activity
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }) {

                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "add_icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(text = "Add tracker", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                items(viewModel.mList) { habit ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // if get selected display this habit tracker in widget
                        Checkbox(
                            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary),
                            checked = habit.habitId == isSelected.value,
                            onCheckedChange = {
                                if (it) isSelected.value = habit.habitId
                            })

                        HabitTrackerWidgetInfo(habitModel = habit)
                    }



                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}