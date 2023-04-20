package com.ftcoding.habitandscheduletracker.presentation.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.BottomBar
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.HomeNavGraph

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        HomeNavGraph(navController = navController, it)
    }
}