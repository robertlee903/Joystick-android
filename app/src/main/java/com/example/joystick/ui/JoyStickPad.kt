package com.example.joystick.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.joystick.ui.joystick.JamPad
import com.example.joystick.ui.joystick.config.HapticFeedbackType
import com.example.joystick.ui.joystick.controls.ControlAnalog
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

enum class NavKey {
    UNKNOWN,
    ARROW_RIGHT,
    ARROW_LOWER_RIGHT,
    ARROW_DOWN,
    ARROW_LOWER_LEFT,
    ARROW_LEFT,
    ARROW_UPPER_LEFT,
    ARROW_UP,
    ARROW_UPPER_RIGHT
}

@Composable
fun JoyStickPad(moveRobot: (direction: NavKey) -> Unit = {}) {
    fun getTheta(x: Float, y: Float): Float = atan2(y, x)

    fun normalizeAngle(angle: Float): Float = if (angle < 0) angle + 2 * PI.toFloat() else angle

    fun getDirectionFromAngle(theta: Float): NavKey = when {
        theta >= 0 && theta < PI / 8 -> NavKey.ARROW_RIGHT
        theta >= PI / 8 && theta < 3 * PI / 8 -> NavKey.ARROW_LOWER_RIGHT
        theta >= 3 * PI / 8 && theta < 5 * PI / 8 -> NavKey.ARROW_DOWN
        theta >= 5 * PI / 8 && theta < 7 * PI / 8 -> NavKey.ARROW_LOWER_LEFT
        theta >= 7 * PI / 8 && theta < 9 * PI / 8 -> NavKey.ARROW_LEFT
        theta >= 9 * PI / 8 && theta < 11 * PI / 8 -> NavKey.ARROW_UPPER_LEFT
        theta >= 11 * PI / 8 && theta < 13 * PI / 8 -> NavKey.ARROW_UP
        theta >= 13 * PI / 8 && theta < 15 * PI / 8 -> NavKey.ARROW_UPPER_RIGHT
        else -> NavKey.ARROW_RIGHT
    }

    JamPad(
        hapticFeedbackType = HapticFeedbackType.PRESS,
        modifier = Modifier.fillMaxSize(),
        onInputStateUpdated = { inputState ->
            val specificOffset = inputState.continuousDirections[0]
            if (specificOffset != null) {
                specificOffset.let {
                    val threshold = 0.0001f  // Threshold close to zero
                    if (abs(it.x) > threshold && abs(it.y) > threshold) {
                        Log.d("BluetoothDebug", ""+it.x+", "+it.y)
                        val theta = getTheta(it.x, it.y)
                        val normalizedTheta = normalizeAngle(theta)
                        val direction = getDirectionFromAngle(normalizedTheta)
                        Log.d("BluetoothDebug", ""+direction)
                        moveRobot(direction)
                    }
                }
            } else {
                moveRobot(NavKey.UNKNOWN)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(48.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(width = 180.dp, height = 180.dp)
            ) {
                ControlAnalog(
                    modifier = Modifier.fillMaxSize(),
                    id = 0,
                )
            }
        }
    }
}