package com.example.joystick.ui.joystick

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import com.example.joystick.ui.joystick.config.HapticFeedbackType
import com.example.joystick.ui.joystick.handlers.Pointer
import com.example.joystick.ui.joystick.handlers.PointerHandler
import com.example.joystick.ui.joystick.haptics.InputHapticGenerator
import com.example.joystick.ui.joystick.haptics.rememberHapticGenerator
import com.example.joystick.ui.joystick.inputstate.InputState
import com.example.joystick.ui.joystick.utils.relativeTo

@Composable
fun JamPad(
    modifier: Modifier = Modifier,
    onInputStateUpdated: (InputState) -> Unit = { },
    hapticFeedbackType: HapticFeedbackType = HapticFeedbackType.PRESS,
    content: @Composable JamPadScope.() -> Unit,
) {
    val scope = remember { JamPadScope() }
    val rootPosition = remember { mutableStateOf(Offset.Zero) }

    val hapticGenerator = rememberHapticGenerator()
    val inputHapticGenerator = remember { InputHapticGenerator(hapticGenerator, hapticFeedbackType) }

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .onGloballyPositioned { rootPosition.value = it.positionInRoot() }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val trackedPointers = scope.getTrackedIds()

                        val handlersAssociations: Map<PointerHandler?, List<Pointer>> =
                            event.changes
                                .asSequence()
                                .filter { it.pressed }
                                .map { Pointer(it.id.value, it.position + rootPosition.value) }
                                .groupBy { pointer ->
                                    if (pointer.pointerId in trackedPointers) {
                                        scope.getHandlerTracking(pointer.pointerId)
                                    } else {
                                        scope.getHandlerAtPosition(pointer.position)
                                    }
                                }

                        scope.inputState.value =
                            scope.getAllHandlers()
                                .fold(scope.inputState.value) { state, handler ->
                                    val pointers =
                                        handlersAssociations.getOrElse(handler) { emptyList() }

                                    val relativePointers =
                                        pointers
                                            .map {
                                                Pointer(
                                                    it.pointerId,
                                                    it.position.relativeTo(handler.rect),
                                                )
                                            }

                                    val (updatedState, startDragGesture) =
                                        handler.handle(
                                            relativePointers,
                                            state,
                                            scope.getStartDragGestureForHandler(handler),
                                        )

                                    scope.setStartDragGestureForHandler(handler, startDragGesture)
                                    updatedState
                                }
                    }
                }
            },
    ) {
        scope.content()
    }

    DisposableEffect(key1 = scope.inputState.value) {
        onInputStateUpdated(scope.inputState.value)
        onDispose { }
    }

    DisposableEffect(key1 = scope.inputState.value) {
        inputHapticGenerator.onInputStateChanged(scope.inputState.value)
        onDispose { }
    }
}