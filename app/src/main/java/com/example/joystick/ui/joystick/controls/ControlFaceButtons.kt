package com.example.joystick.ui.joystick.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.joystick.ui.joystick.JamPadScope
import com.example.joystick.ui.joystick.arrangements.CircleArrangement
import com.example.joystick.ui.joystick.arrangements.CircumferenceGravityArrangement
import com.example.joystick.ui.joystick.arrangements.CompositeCircumferenceArrangement
import com.example.joystick.ui.joystick.arrangements.EmptyArrangement
import com.example.joystick.ui.joystick.arrangements.GravityArrangement
import com.example.joystick.ui.joystick.config.FaceButtonsLayout
import com.example.joystick.ui.joystick.handlers.GravityPointsPointerHandler
import com.example.joystick.ui.joystick.layouts.GravityArrangementLayout
import com.example.joystick.ui.joystick.ui.DefaultButtonForeground
import com.example.joystick.ui.joystick.ui.DefaultCompositeForeground
import com.example.joystick.ui.joystick.ui.DefaultControlBackground

@Composable
fun JamPadScope.ControlFaceButtons(
    modifier: Modifier = Modifier,
    rotationInDegrees: Float = 0f,
    ids: List<Int>,
    sockets: Int = ids.size,
    faceButtonsLayout: FaceButtonsLayout = FaceButtonsLayout.CIRCUMFERENCE,
    includeComposite: Boolean = true,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (Int, Boolean) -> Unit = { _, pressed ->
        DefaultButtonForeground(pressed = pressed)
    },
    foregroundComposite: @Composable (Boolean) -> Unit = { pressed ->
        DefaultCompositeForeground(pressed = pressed)
    },
) {
    val primaryArrangement = rememberPrimaryArrangement(ids, sockets, faceButtonsLayout, rotationInDegrees)
    val compositeArrangement =
        rememberCompositeArrangement(includeComposite, ids, sockets, rotationInDegrees)

    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned {
                    registerHandler(
                        GravityPointsPointerHandler(
                            listOf(ids, faceButtonsLayout, rotationInDegrees, sockets).hashCode(),
                            it.boundsInRoot(),
                            primaryArrangement,
                            compositeArrangement,
                        ),
                    )
                },
    ) {
        background()

        GravityArrangementLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = primaryArrangement,
        ) {
            ids.forEach { id ->
                foreground(id, inputState.value.getDigitalKey(id))
            }
        }

        GravityArrangementLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = compositeArrangement,
        ) {
            compositeArrangement.getGravityPoints().forEach { point ->
                foregroundComposite(point.keys.all { inputState.value.getDigitalKey(it) })
            }
        }
    }
}

@Composable
private fun rememberCompositeArrangement(
    includeCompositeButtons: Boolean,
    ids: List<Int>,
    sockets: Int,
    rotationInDegrees: Float,
): GravityArrangement {
    return remember(ids, includeCompositeButtons, rotationInDegrees) {
        if (includeCompositeButtons) {
            CompositeCircumferenceArrangement(ids, sockets, rotationInDegrees)
        } else {
            EmptyArrangement
        }
    }
}

@Composable
private fun rememberPrimaryArrangement(
    ids: List<Int>,
    sockets: Int,
    faceButtonsLayout: FaceButtonsLayout,
    rotationInDegrees: Float,
): GravityArrangement {
    return remember(ids, faceButtonsLayout, rotationInDegrees) {
        when (faceButtonsLayout) {
            FaceButtonsLayout.CIRCUMFERENCE ->
                CircumferenceGravityArrangement(
                    ids,
                    sockets,
                    rotationInDegrees,
                )

            FaceButtonsLayout.CIRCLE -> CircleArrangement(ids, sockets, rotationInDegrees)
        }
    }
}
