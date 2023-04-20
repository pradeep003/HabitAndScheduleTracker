package com.ftcoding.habitandscheduletracker.presentation.habit_ui.habit_screen

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.ftcoding.habitandscheduletracker.presentation.MainActivity
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Navigation
import com.ftcoding.habitandscheduletracker.presentation.ui.navigation.Screen
import com.google.common.truth.Truth

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HabitScreenTest {


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        composeRule.activity.setContent {
            navHostController = TestNavHostController(LocalContext.current)
            navHostController.navigatorProvider.addNavigator(ComposeNavigator())

            Navigation(navController = navHostController, padding = PaddingValues(4.dp))
        }

    }

    @Test
    fun assert_IsTopAppBarDisplayed() {
        composeRule.onNodeWithTag("tag").assertIsDisplayed()
    }

    @Test
    fun assert_IsAddButtonClickedInAppBar() {
        composeRule.onNodeWithTag("add").performClick()
        val current = navHostController.currentBackStackEntry?.destination?.route
        Truth.assertThat(current).isEqualTo("${Screen.CreateHabitScreen.route}?habitId={habitId}")
    }
}