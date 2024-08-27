package com.example.joystick

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import java.util.UUID

class Global : Application() {
    companion object {
        @JvmField
        var bluetoothAdapter: BluetoothAdapter? = null
        val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        var mmSocket: BluetoothSocket? = null
    }
}