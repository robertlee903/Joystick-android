package com.example.joystick.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.joystick.pages.Home
import com.example.joystick.pages.ConnectWithYourRobot

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            Home(navController)
        }
        composable(NavigationItem.ConnectWithYourRobot.route) {
            ConnectWithYourRobot(navController)
        }
    }
}