package com.example.joystick.navigation

enum class Screen {
    Home,
    ConnectWithYourRobot,
}
sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.Home.name)
    object ConnectWithYourRobot : NavigationItem(Screen.ConnectWithYourRobot.name)
}