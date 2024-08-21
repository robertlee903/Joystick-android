package com.example.joystick.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.joystick.Global.Companion.MY_UUID
import com.example.joystick.Global.Companion.bluetoothAdapter
import com.example.joystick.Global.Companion.mmSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@Composable
fun DiscoverBluetoothDevices() {
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

    DeviceList("Paired Devices", pairedDevices) {device ->
        connectDevice(device)
    }
    DeviceList("Discovered Devices", discoveredDevices) {device ->
        connectDevice(device)
    }
}

fun connectDevice(device: BluetoothDevice) {
    mmSocket = BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
        device.createRfcommSocketToServiceRecord(MY_UUID)
    }

    // Cancel discovery because it otherwise slows down the connection.
    bluetoothAdapter?.cancelDiscovery()

    mmSocket?.let { socket ->
        Log.d("BluetoothDebug", "Socket!!!!")
        // Connect to the remote device through the socket. This call blocks
        // until it succeeds or throws an exception.
        socket.connect()

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        var myBluetoothService = MyBluetoothService.
    }
}

private const val TAG = "BluetoothDebug"

// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2
// ... (Add other message types here as needed.)

class MyBluetoothService(
    // handler that gets info from Bluetooth service
    private val handler: Handler
) {

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    mmBuffer)
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, mmBuffer)
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }
}