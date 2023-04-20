package com.ftcoding.habitandscheduletracker.presentation.ui.navigation

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
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController


    @Before
    fun setUp() {
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            Navigation(navController = navController, padding = PaddingValues(4.dp))
        }

    }


    @Test
    fun verify_StartDestinationIsHomeScreen() {
        composeTestRule.onNodeWithTag("tag").assertIsDisplayed()

    }

    @Test
    fun performClick_OnAddButton_navigateToCreateHabitScreen() {
        composeTestRule.onNodeWithTag("add").performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        Truth.assertThat(route).isEqualTo("${Screen.CreateHabitScreen.route}?habitId={habitId}")
    }


}