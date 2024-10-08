package com.example.joystick

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHostController

import androidx.navigation.compose.rememberNavController
import com.example.joystick.bluetooth.BluetoothViewModel
import com.example.joystick.navigation.AppNavHost

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: BluetoothViewModel by viewModels()

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "This device doesn't support bluetooth", Toast.LENGTH_SHORT).show()
        }

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val REQUEST_ENABLE_BT = 1
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        setContent {
            var navController: NavHostController = rememberNavController()
            AppNavHost(navController, viewModel)
        }
    }
}