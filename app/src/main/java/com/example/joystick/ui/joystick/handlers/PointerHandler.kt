package com.example.joystick.ui.joystick.handlers

import androidx.compose.ui.geometry.Rect
import com.example.joystick.ui.joystick.inputstate.InputState

interface PointerHandler {
    val id: Int
    val rect: Rect

    fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
    ): Result

    fun handlerId(): String {
        return "${this::class.simpleName}:$id"
    }
}
