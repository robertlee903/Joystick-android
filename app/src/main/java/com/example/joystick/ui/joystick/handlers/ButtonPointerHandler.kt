package com.example.joystick.ui.joystick.handlers

import androidx.compose.ui.geometry.Rect
import com.example.joystick.ui.joystick.inputstate.InputState

data class ButtonPointerHandler(override val id: Int, override val rect: Rect) : PointerHandler {
    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
    ): Result {
        return Result(inputState.setDigitalKey(id, pointers.isNotEmpty()))
    }
}
