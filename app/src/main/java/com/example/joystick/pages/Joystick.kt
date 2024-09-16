package com.example.joystick.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.joystick.navigation.Screen
import com.example.joystick.ui.NavTopBar
import com.example.joystick.bluetooth.BluetoothViewModel
import com.example.joystick.ui.JoyStickPad
import com.example.joystick.ui.NavKey

@Composable
fun Joystick(
    navController: NavHostController,
    viewModel: BluetoothViewModel,
) {
    fun disconnectDevice() {
        viewModel.disconnectDevice()
        navController.navigate(Screen.Home.route)
    }

    fun moveUp() {
        viewModel.write("f".toByteArray())
    }
    fun moveDown() {
        viewModel.write("b".toByteArray())
    }
    fun moveLeft() {
        viewModel.write("l".toByteArray())
    }
    fun moveRight() {
        viewModel.write("r".toByteArray())
    }
    fun moveUpRight() {

    }
    fun stop() {
        viewModel.write("s".toByteArray())
    }

    fun moveRobot(direction: NavKey) {
        when {
            direction == NavKey.ARROW_UP -> moveUp()
            direction == NavKey.ARROW_DOWN -> moveDown()
            direction == NavKey.ARROW_LEFT -> moveLeft()
            direction == NavKey.ARROW_RIGHT -> moveRight()
            direction == NavKey.ARROW_UPPER_RIGHT -> moveUpRight()
            direction == NavKey.ARROW_UPPER_LEFT -> moveUpRight()
            direction == NavKey.ARROW_LOWER_RIGHT -> moveUpRight()
            direction == NavKey.ARROW_LOWER_LEFT -> moveUpRight()
            else -> stop()
        }
    }

    Scaffold(
        topBar = {
            NavTopBar(
                title = "Joystick",
                canNavigateBack = true,
                navigateUp = {disconnectDevice()}
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            JoyStickPad(moveRobot = {direction -> moveRobot(direction)})
        }
    }
}
