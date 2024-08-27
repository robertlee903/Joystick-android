package com.example.joystick.ui.joystick.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.joystick.ui.joystick.JamPadScope
import com.example.joystick.ui.joystick.handlers.CrossPointerHandler
import com.example.joystick.ui.joystick.ui.DefaultControlBackground
import com.example.joystick.ui.joystick.ui.DefaultCrossForeground

@Composable
fun JamPadScope.ControlCross(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (Offset) -> Unit = { DefaultCrossForeground(direction = it) },
) {
    Box(
        modifier =
        modifier
            .aspectRatio(1f)
            .onGloballyPositioned { registerHandler(CrossPointerHandler(id, it.boundsInRoot())) },
    ) {
        background()
        foreground(inputState.value.getDiscreteDirection(id))
    }
}
