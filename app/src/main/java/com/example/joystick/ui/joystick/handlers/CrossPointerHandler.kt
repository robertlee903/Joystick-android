package com.example.joystick.ui.joystick.handlers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.joystick.ui.joystick.inputstate.InputState

data class CrossPointerHandler(override val id: Int, override val rect: Rect) : PointerHandler {
    enum class State(val position: Offset) {
        UP(Offset(0f, 1f)),
        DOWN(Offset(0f, -1f)),
        LEFT(Offset(-1f, 0f)),
        RIGHT(Offset(1f, 0f)),
        UP_LEFT(Offset(-1f, 1f)),
        UP_RIGHT(Offset(1f, 1f)),
        DOWN_LEFT(Offset(-1f, -1f)),
        DOWN_RIGHT(Offset(1f, -1f)),
    }

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
    ): Result {
        val currentDragGesture = pointers.firstOrNull { it.pointerId == startDragGesture?.pointerId }

        return when {
            pointers.isEmpty() -> {
                Result(
                    inputState.setDiscreteDirection(id, Offset.Unspecified),
                    null,
                )
            }

            currentDragGesture != null -> {
                Result(
                    inputState.setDiscreteDirection(id, findCloserState(currentDragGesture)),
                    startDragGesture,
                )
            }

            else -> {
                val firstPointer = pointers.first()
                Result(
                    inputState.setDiscreteDirection(id, findCloserState(firstPointer)),
                    firstPointer,
                )
            }
        }
    }

    private fun findCloserState(pointer: Pointer): Offset {
        return State.entries
            .minBy { (pointer.position - it.position).getDistanceSquared() }
            .position
            .let { it.copy(y = -it.y) }
    }
}
