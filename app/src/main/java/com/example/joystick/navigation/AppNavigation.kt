package com.example.joystick.navigation

enum class Screen(val route: String) {
    Home("home"),
    ConnectWithYourRobot("connect_with_your_robot"),
    Joystick("joystick");

    // Function to create a route with parameters
    fun withArgs(vararg args: Pair<String, String>): String {
        var finalRoute = route
        args.forEach { (key, value) ->
            finalRoute = finalRoute.replace("{$key}", value)
        }
        return finalRoute
    }
}