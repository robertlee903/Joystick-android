package com.example.joystick.ui.joystick

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import com.example.joystick.ui.joystick.handlers.Pointer
import com.example.joystick.ui.joystick.handlers.PointerHandler
import com.example.joystick.ui.joystick.inputstate.InputState

@Stable
class JamPadScope {
    private data class HandlerState(
        val pointerHandler: PointerHandler,
        var startDragGesture: Pointer? = null,
    )

    internal val inputState = mutableStateOf(InputState())

    private val handlers = mutableMapOf<String, HandlerState>()

    internal fun registerHandler(pointerHandler: PointerHandler) {
        handlers[pointerHandler.handlerId()] = HandlerState(pointerHandler, null)
    }

    internal fun getAllHandlers(): Collection<PointerHandler> {
        return handlers.values
            .map { it.pointerHandler }
    }

    internal fun getTrackedIds(): Set<Long> {
        return handlers.values
            .mapNotNull { it.startDragGesture?.pointerId }
            .toSet()
    }

    internal fun getHandlerAtPosition(position: Offset): PointerHandler? {
        return handlers.values
            .firstOrNull { (handler, _) -> handler.rect.contains(position) }
            ?.pointerHandler
    }

    internal fun getHandlerTracking(pointerId: Long): PointerHandler? {
        return handlers.values
            .firstOrNull { (_, dragGesture) -> dragGesture?.pointerId == pointerId }
            ?.pointerHandler
    }

    internal fun getStartDragGestureForHandler(pointerHandler: PointerHandler): Pointer? {
        return handlers[pointerHandler.handlerId()]?.startDragGesture
    }

    internal fun setStartDragGestureForHandler(
        pointerHandler: PointerHandler,
        newGestureStart: Pointer?,
    ) {
        handlers[pointerHandler.handlerId()]?.startDragGesture = newGestureStart
    }
}