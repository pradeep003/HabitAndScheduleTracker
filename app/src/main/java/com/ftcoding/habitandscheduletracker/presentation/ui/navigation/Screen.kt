package com.ftcoding.habitandscheduletracker.presentation.ui.navigation

sealed class Screen (val route: String) {
    object CreateHabitScreen: Screen("create_habit_screen")
    object HabitScreen: Screen("habit_screen")
    object ScheduleScreen: Screen("schedule_screen")
    object CreateScheduleScreen: Screen("create_schedule_screen")
    object HabitDetailScreen: Screen("habit_detail_screen")
    object SettingScreen: Screen("setting_screen")
    object SplashScreen: Screen("splash_screen")
    object HomeScreen: Screen("home_screen")
}

object Graph {
    const val SCHEDULE = "schedule_graph"
    const val TRACKER = "tracker_graph"
}