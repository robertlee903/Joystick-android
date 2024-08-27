package com.example.joystick.ui.joystick.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.example.joystick.ui.joystick.layouts.CircularLayout
import com.example.joystick.ui.joystick.utils.ifUnspecified

@Composable
fun DefaultCrossForeground(
    modifier: Modifier = Modifier,
    direction: Offset,
    rightDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowRight),
        )
    },
    bottomDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowDown),
        )
    },
    leftDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowLeft),
        )
    },
    topDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowUp),
        )
    },
) {
    val adjustedDirection = direction.ifUnspecified { Offset.Zero }

    CircularLayout(modifier = modifier.fillMaxSize()) {
        rightDial(adjustedDirection.x > 0.5f)
        bottomDial(adjustedDirection.y < -0.5f)
        leftDial(adjustedDirection.x < -0.5f)
        topDial(adjustedDirection.y > 0.5f)
    }
}
