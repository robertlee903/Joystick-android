package com.example.joystick.ui.joystick.handlers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.joystick.ui.joystick.inputstate.InputState
import com.example.joystick.ui.joystick.utils.coerceIn

data class AnalogPointerHandler(override val id: Int, override val rect: Rect) : PointerHandler {
    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
    ): Result {
        val currentDragGesture = pointers.firstOrNull { it.pointerId == startDragGesture?.pointerId }

        return when {
            pointers.isEmpty() -> {
                Result(
                    inputState.setContinuousDirection(id, Offset.Unspecified),
                    null,
                )
            }
            startDragGesture != null && currentDragGesture != null -> {
                val deltaPosition = (currentDragGesture.position - startDragGesture.position)
                val offsetValue = deltaPosition.coerceIn(Offset(-1f, -1f), Offset(1f, 1f))
                Result(
                    inputState.setContinuousDirection(id, offsetValue),
                    startDragGesture,
                )
            }
            else -> {
                val firstPointer = pointers.first()
                Result(
                    inputState.setContinuousDirection(id, Offset.Zero),
                    firstPointer,
                )
            }
        }
    }
}
