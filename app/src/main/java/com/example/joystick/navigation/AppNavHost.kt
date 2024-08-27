package com.example.joystick.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.joystick.bluetooth.BluetoothViewModel
import com.example.joystick.pages.Home
import com.example.joystick.pages.ConnectWithYourRobot
import com.example.joystick.pages.Joystick

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: BluetoothViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            Home(navController, viewModel)
        }
        composable(Screen.ConnectWithYourRobot.route) {
            ConnectWithYourRobot(navController, viewModel)
        }
        composable(Screen.Joystick.route) {
            Joystick(navController, viewModel)
        }
    }
}