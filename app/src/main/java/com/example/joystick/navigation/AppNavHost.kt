package com.example.joystick.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.joystick.pages.Home
import com.example.joystick.pages.ConnectWithYourRobot
import com.example.joystick.pages.Joystick

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            Home(navController)
        }
        composable(Screen.ConnectWithYourRobot.route) {
            ConnectWithYourRobot(navController)
        }
        composable(Screen.Joystick.route) { backStackEntry ->
            Joystick(navController, backStackEntry.arguments?.getString("bluetoothAddress"))
        }
    }
}