package com.example.joystick.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.joystick.bluetooth.BluetoothViewModel
import com.example.joystick.navigation.Screen

@Composable
fun Home(
    navController: NavHostController,
    viewModel: BluetoothViewModel,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            viewModel.discoverDevices()
            navController.navigate(Screen.ConnectWithYourRobot.route)
        }) {
            Text(text = "Connect with your robot")
        }
    }
}
