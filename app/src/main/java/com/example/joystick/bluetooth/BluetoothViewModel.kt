package com.example.joystick.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.util.UUID
import java.io.IOException

class BluetoothViewModel : ViewModel() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    var pairedDevices = mutableStateListOf<BluetoothDevice>()
    var devices = mutableStateOf(listOf<BluetoothDevice>())
    var isConnecting = mutableStateOf(false)
    var isFailed = mutableStateOf(false)
    var connectedDevice = mutableStateOf<BluetoothDevice?>(null)
    var mmSocket = mutableStateOf<BluetoothSocket?>(null)
    var mmOutStream = mutableStateOf<OutputStream?>(null)

    fun discoverDevices() {
        pairedDevices = mutableStateListOf()
        bluetoothAdapter?.bondedDevices?.let {
            pairedDevices.addAll(it)
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                isConnecting.value = true
                mmSocket.value = device?.createRfcommSocketToServiceRecord(MY_UUID)

                mmSocket.value?.let { socket ->
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    socket.connect()
                }

                mmOutStream.value = mmSocket.value?.outputStream

                connectedDevice.value = device
                isConnecting.value = false
            } catch (e: Exception) {
                isConnecting.value = false
                isFailed.value = true
                Log.e("BluetoothDebug", "Failed to connect your robot: "+device.address, e)
            }
        }
    }

    fun disconnectDevice() {
        try {
            mmSocket.value?.close()
            mmSocket.value = null
            connectedDevice.value = null
        } catch (e: IOException) {
            Log.e("BluetoothDebug", "Could not close the client socket", e)
        }
    }

    fun write(bytes: ByteArray) {
        try {
            mmOutStream.value?.write(bytes)
        } catch (e: IOException) {
            Log.e("BluetoothDebug", "Error occurred when sending data", e)
        }
    }

    companion object {
        val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    }
}