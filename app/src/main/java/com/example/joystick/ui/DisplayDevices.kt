package com.example.joystick.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeviceList(header: String, devices: List<BluetoothDevice>, onDeviceClick: (BluetoothDevice) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = header,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyColumn {
            items(devices) { device ->
                DeviceItem(device, onDeviceClick)
            }
        }
    }
}

@Composable
fun DeviceItem(device: BluetoothDevice, onDeviceClick: (BluetoothDevice) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDeviceClick(device) }
                .padding(16.dp)
        ) {
            Text(
                text = "Name: ${device.name ?: "Unknown"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Address: ${device.address}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}