package com.example.joystick.ui.joystick.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.joystick.ui.joystick.JamPadScope
import com.example.joystick.ui.joystick.handlers.ButtonPointerHandler
import com.example.joystick.ui.joystick.ui.DefaultButtonForeground
import com.example.joystick.ui.joystick.ui.DefaultControlBackground

@Composable
fun JamPadScope.ControlButton(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable (Boolean) -> Unit = { DefaultControlBackground() },
    foreground: @Composable (Boolean) -> Unit = { DefaultButtonForeground(pressed = it) },
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    registerHandler(ButtonPointerHandler(id, it.boundsInRoot()))
                },
    ) {
        val pressed = inputState.value.getDigitalKey(id)
        background(pressed)
        foreground(pressed)
    }
}
