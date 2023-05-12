package com.ftcoding.habitandscheduletracker.presentation

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ftcoding.habitandscheduletracker.data.domain.models.user.User
import com.ftcoding.habitandscheduletracker.notification.receiver.ScheduleNotificationReceiver
import com.ftcoding.habitandscheduletracker.presentation.data_source.use_cases.user.UserUseCases
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Navigation
import com.ftcoding.habitandscheduletracker.presentation.ui.theme.HabitAndScheduleTrackerTheme
import com.ftcoding.habitandscheduletracker.presentation.util.Constants.hexColorToIntColor
import com.ftcoding.habitandscheduletracker.util.HabitConstants.USER_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userUseCases: UserUseCases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receiver = ComponentName(applicationContext, ScheduleNotificationReceiver::class.java)
        applicationContext.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        setContent {

            // state to observe setting changes
            val settingDetails = remember {
                mutableStateOf(User())
            }

            // observe the changes made in setting
            if (userUseCases.getUserUseCase.invoke().collectAsState(initial = emptyList()).value.isNotEmpty()) {
                userUseCases.getUserUseCase.invoke()
                    .collectAsState(initial = listOf(User())).value.first {
                        if (it.userId == USER_ID) {
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            Navigation(navController = navController, padding = it)
                        }
                    )
                }
            }
        }
    }

}
