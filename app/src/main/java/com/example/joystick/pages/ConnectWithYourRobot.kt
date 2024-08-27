package com.example.joystick.pages

import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.joystick.bluetooth.BluetoothViewModel
import com.example.joystick.ui.DeviceList
import com.example.joystick.navigation.Screen
import com.example.joystick.ui.NavTopBar

@Composable
fun ConnectWithYourRobot(
    navController: NavHostController,
    viewModel: BluetoothViewModel,
) {
    val context = LocalContext.current

    val pairedDevices = viewModel.pairedDevices
    val isConnecting = viewModel.isConnecting.value
    val isFailed = viewModel.isFailed.value

    fun connectToDevice(device: BluetoothDevice) {
        viewModel.connectToDevice(device)
    }

    if (isConnecting) {
        Dialog(onDismissRequest = {}) {
            Text("Connecting, please wait...")
        }
    }

    if (isFailed) {
        Toast.makeText(context, "Failed to connect your robot", Toast.LENGTH_SHORT).show()
        viewModel.isFailed.value = false;
    }

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
            DeviceList("Paired Devices", pairedDevices) {device ->
                connectToDevice(device)
            }
        }
    }

    LaunchedEffect(viewModel.connectedDevice.value) {
        if (viewModel.connectedDevice.value != null) {
            navController.navigate(Screen.Joystick.route)
        }
    }
}
