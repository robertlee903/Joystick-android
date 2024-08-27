package com.example.joystick.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.joystick.Global.Companion.mmSocket
import com.example.joystick.Global.Companion.MY_UUID
import com.example.joystick.Global.Companion.bluetoothAdapter
import com.example.joystick.navigation.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import java.io.IOException

@Composable
fun DiscoverBluetoothDevices(
    navController: NavHostController,
) {
    var isConnecting by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val pairedDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val discoveredDevices = remember { mutableStateListOf<BluetoothDevice>() }

    DisposableEffect(Unit) {
        bluetoothAdapter?.bondedDevices?.let {
            pairedDevices.addAll(it)
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                        if (!pairedDevices.contains(device) && !discoveredDevices.contains(device)) {
                            discoveredDevices.add(device)
                        }
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        bluetoothAdapter?.startDiscovery() // Keep discovering
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        context.registerReceiver(receiver, filter)
        bluetoothAdapter?.startDiscovery()

        onDispose {
            context.unregisterReceiver(receiver)
            bluetoothAdapter?.cancelDiscovery()
        }
    }

    fun goToJoyStick(device: BluetoothDevice) {
        navController.navigate(Screen.Joystick.withArgs("bluetoothAddress" to device.address))
    }

    fun connectToDevice(device: BluetoothDevice) {
        isConnecting = true
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter?.cancelDiscovery()

        mmSocket = device?.createRfcommSocketToServiceRecord(MY_UUID)

        try {
            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()
            }
            isConnecting = false
            goToJoyStick(device)
        } catch (e: IOException) {
            Log.e("BluetoothDebug", "Failed to connect your robot: "+device.address, e)
            Toast.makeText(context, "Failed to connect your robot: "+device.address, Toast.LENGTH_SHORT).show()
            isConnecting = false
        }
    }

    DeviceList("Paired Devices", pairedDevices) {device ->
        connectToDevice(device)
    }
    DeviceList("Discovered Devices", discoveredDevices) {device ->
        connectToDevice(device)
    }

    if (isConnecting) {
        Log.d("BluetoothDebug", "Connecting")
        LoadingDialog()
    }
}

@Composable
fun LoadingDialog() {
    AlertDialog(
        onDismissRequest = { /* Dismiss action */ },
        title = { Text("Connecting") },
        text = { Text("Please wait while we connect to the device...") },
        confirmButton = { }
    )
}