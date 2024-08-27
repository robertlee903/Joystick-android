package com.example.joystick.pages

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.joystick.bluetooth.DiscoverBluetoothDevices
import com.example.joystick.ui.NavTopBar

@Composable
fun ConnectWithYourRobot(
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            NavTopBar(
                title = "Connect with your robot",
                canNavigateBack = true,
                navigateUp = {navController.popBackStack()}
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DiscoverBluetoothDevices(navController)
        }
    }
}
