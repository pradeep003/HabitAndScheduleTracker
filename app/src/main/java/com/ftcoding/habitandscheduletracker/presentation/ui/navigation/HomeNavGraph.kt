package com.ftcoding.habitandscheduletracker.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.create_habit.CreateHabitScreen
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_detail_screen.HabitDetailScreen
import com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen.HabitScreen
import com.ftcoding.habitandscheduletracker.presentation.schedule_ui.create_schedule.CreateScheduleScreen
import com.ftcoding.habitandscheduletracker.presentation.schedule_ui.schedule_screen.ScheduleScreen
import com.ftcoding.habitandscheduletracker.presentation.setting_ui.SettingScreen

@Composable
fun HomeNavGraph(navController: NavHostController, paddingValues: PaddingValues) {

    NavHost(
        navController = navController,
        route = Screen.HomeScreen.route,
        startDestination = Screen.SettingScreen.route,
        modifier = Modifier.padding(paddingValues)
    ) {

        habitNavGraph(navController)

        scheduleNavGraph(navController)

        composable(Screen.SettingScreen.route) {
            SettingScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.scheduleNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.SCHEDULE,
        startDestination = Screen.ScheduleScreen.route
    ) {

        composable(Screen.ScheduleScreen.route) {
            ScheduleScreen(navController = navController)
        }

        composable(
            "${Screen.CreateScheduleScreen.route}?eventId={eventId}",
            arguments = listOf(
                navArgument(name = "eventId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val scheduleId = backStackEntry.arguments?.getInt("eventId", -1) ?: -1
            CreateScheduleScreen(navController = navController, eventId = scheduleId)
        }
    }
}

fun NavGraphBuilder.habitNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.TRACKER,
        startDestination = Screen.HabitScreen.route
    ) {

        composable(Screen.HabitScreen.route) {
            HabitScreen(navController = navController)
        }

        composable(
            route = "${Screen.HabitDetailScreen.route}/{habitId}",
            arguments = listOf(
                navArgument("habitId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getInt("habitId") ?: 0
            HabitDetailScreen(
                navController = navController,
                habitId = habitId
            )
        }

        composable("${Screen.CreateHabitScreen.route}?habitId={habitId}",
            arguments = listOf(
                navArgument(name = "habitId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getInt("habitId", -1) ?: -1
            CreateHabitScreen(navController = navController, habitId)
        }
    }
}