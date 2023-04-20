package com.ftcoding.habitandscheduletracker.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit.CreateHabitScreen
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_detail_screen.HabitDetailScreen
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen.HabitScreen
import com.ftcoding.habitandscheduletracker.presentation.home.HomeScreen
import com.ftcoding.habitandscheduletracker.presentation.schedule_ui.create_schedule.CreateScheduleScreen
import com.ftcoding.habitandscheduletracker.presentation.schedule_ui.schedule_screen.ScheduleScreen
import com.ftcoding.habitandscheduletracker.presentation.setting_ui.SettingScreen
import com.ftcoding.habitandscheduletracker.presentation.splash_ui.AnimatedSplashScreen

@Composable
fun Navigation(navController: NavHostController, padding: PaddingValues) {

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {

        composable(route = Screen.SplashScreen.route) {
            AnimatedSplashScreen(navController = navController)
        }

        composable(route = Screen.HomeScreen.route) {
            HomeScreen()
        }

    }
}

