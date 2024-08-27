package com.example.joystick.pages

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.joystick.navigation.Screen
import com.example.joystick.ui.NavTopBar
import com.example.joystick.bluetooth.BluetoothViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Joystick(
    navController: NavHostController,
    viewModel: BluetoothViewModel,
) {
    val device = viewModel.connectedDevice.value

    // Closes the client socket and causes the thread to finish.
    fun disconnectDevice() {
        viewModel.disconnectDevice()
        navController.navigate(Screen.Home.route)
    }

    fun moveForward() {
        viewModel.write("f".toByteArray())
    }
    fun moveBackward() {
        viewModel.write("b".toByteArray())
    }
    fun turnLeft() {
        viewModel.write("l".toByteArray())
    }
    fun turnRight() {
        viewModel.write("r".toByteArray())
    }
    fun stop() {
        viewModel.write("s".toByteArray())
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
            Text("Connected to device: ${device?.address}")
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .pointerInteropFilter { motionEvent ->
                            when (motionEvent.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    moveForward()
                                    true // Return true to capture the event
                                }
                                MotionEvent.ACTION_UP -> {
                                    stop()
                                    true
                                }
                                else -> false
                            }
                        }
                ) {
                    Text("Forward")
                }
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .pointerInteropFilter { motionEvent ->
                            when (motionEvent.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    moveBackward()
                                    true // Return true to capture the event
                                }
                                MotionEvent.ACTION_UP -> {
                                    stop()
                                    true
                                }
                                else -> false
                            }
                        }
                ) {
                    Text("Backward")
                }
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .pointerInteropFilter { motionEvent ->
                            when (motionEvent.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    turnLeft()
                                    true // Return true to capture the event
                                }
                                MotionEvent.ACTION_UP -> {
                                    stop()
                                    true
                                }
                                else -> false
                            }
                        }
                ) {
                    Text("Left")
                }
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .pointerInteropFilter { motionEvent ->
                            when (motionEvent.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    turnRight()
                                    true // Return true to capture the event
                                }
                                MotionEvent.ACTION_UP -> {
                                    stop()
                                    true
                                }
                                else -> false
                            }
                        }
                ) {
                    Text("Right")
                }
            }
        }
    }
}
