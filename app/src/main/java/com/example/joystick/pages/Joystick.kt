package com.example.joystick.pages

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.joystick.Global.Companion.bluetoothAdapter
import com.example.joystick.Global.Companion.MY_UUID
import com.example.joystick.bluetooth.DiscoverBluetoothDevices
import com.example.joystick.navigation.Screen
import com.example.joystick.ui.NavTopBar
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import com.example.joystick.Global.Companion.mmSocket

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Joystick(
    navController: NavHostController,
    bluetoothAddress: String?
) {
    val mmOutStream: OutputStream? = mmSocket?.outputStream

    // Call this from the main activity to send data to the remote device.
    fun write(bytes: ByteArray) {
        try {
            mmOutStream?.write(bytes)
        } catch (e: IOException) {
            Log.e("BluetoothDebug", "Error occurred when sending data", e)
        }
    }

    // Closes the client socket and causes the thread to finish.
    fun disconnectDevice() {
        try {
            mmSocket?.close()
            mmSocket = null
        } catch (e: IOException) {
            Log.e("BluetoothDebug", "Could not close the client socket", e)
        }
        navController.navigate(Screen.Home.route)
    }

    fun moveForward() {
        write("f".toByteArray())
    }
    fun moveBackward() {
        write("b".toByteArray())
    }
    fun turnLeft() {
        write("l".toByteArray())
    }
    fun turnRight() {
        write("r".toByteArray())
    }
    fun stop() {
        write("s".toByteArray())
    }

    LaunchedEffect(mmSocket) {
        if (mmSocket == null) {
            disconnectDevice()
        }
    }

    Scaffold(
        topBar = {
            NavTopBar(
                title = "Connected your robot",
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
