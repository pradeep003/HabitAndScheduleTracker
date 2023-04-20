package com.ftcoding.habitandscheduletracker.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    // tracker
    object Tracker: BottomBarScreen(
        route = Screen.HabitScreen.route,
        title = "Tracker",
        icon = Icons.Filled.Analytics
    )
    // schedule
    object Schedule: BottomBarScreen(
        route = Screen.ScheduleScreen.route,
        title = "Schedule",
        icon = Icons.Filled.Schedule,

        )
    // setting
    object Setting: BottomBarScreen(
        route = Screen.SettingScreen.route,
        title = "Settings",
        icon = Icons.Filled.Settings,

        )
}

