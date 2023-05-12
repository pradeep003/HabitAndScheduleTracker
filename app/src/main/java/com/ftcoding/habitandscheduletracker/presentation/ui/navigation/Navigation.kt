package com.ftcoding.habitandscheduletracker.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ftcoding.habitandscheduletracker.presentation.home.HomeScreen
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

