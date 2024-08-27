package com.example.joystick.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.joystick.ui.joystick.JamPad
import com.example.joystick.ui.joystick.config.HapticFeedbackType
import com.example.joystick.ui.joystick.controls.ControlAnalog
import com.example.joystick.ui.joystick.controls.ControlCross
import com.example.joystick.ui.joystick.controls.ControlFaceButtons


@Composable
fun JoyStickPad() {
    JamPad(
        hapticFeedbackType = HapticFeedbackType.PRESS,
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            HalfPad(
                modifier = Modifier.weight(1f, fill = false),
                primary = {
                    ControlCross(
                        modifier = Modifier.fillMaxSize(),
                        id = 0,
                    )
                },
                secondary = {
                    ControlAnalog(
                        modifier = Modifier.fillMaxSize(),
                        id = 1,
                    )
                },
            )
            HalfPad(
                modifier = Modifier.weight(1f, fill = false),
                primary = {
                    ControlFaceButtons(
                        modifier = Modifier.fillMaxSize(),
                        ids = listOf(6, 7, 8),
                    )
                },
                secondary = {
                    ControlAnalog(
                        modifier = Modifier.fillMaxSize(),
                        id = 2,
                    )
                },
            )
        }
    }
}

@Composable
private fun HalfPad(
    modifier: Modifier = Modifier,
    primary: @Composable () -> Unit,
    secondary: @Composable () -> Unit,
) {
    Column(
        modifier =
        modifier
            .padding(16.dp)
            .aspectRatio(2f / 4f)
            .widthIn(0.dp, 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            primary()
        }
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            secondary()
        }
    }
}