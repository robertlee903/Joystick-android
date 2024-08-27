package com.example.joystick.ui.joystick.haptics

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.example.joystick.ui.joystick.config.HapticFeedbackType
import com.example.joystick.ui.joystick.inputstate.InputState
import java.io.IOException

class InputHapticGenerator(
    private val generator: HapticGenerator,
    private val hapticFeedbackType: HapticFeedbackType,
) {
    private var previousInputState: InputState? = null

    fun onInputStateChanged(current: InputState) {
        if (hapticFeedbackType == HapticFeedbackType.NONE) {
            return
        }

        val previous = previousInputState

        if (previous == null) {
            previousInputState = current
            return
        }

        val requestedEffect =
            keyEffect(previous, current)
                ?: discreteDirectionEffect(previous, current)
                ?: continuousDirectionEffect(previous, current)

        if (requestedEffect != null && shouldPlayEffect(requestedEffect)) {
            generator.generate(requestedEffect)
        }

        previousInputState = current
    }

    private fun shouldPlayEffect(effect: HapticEffect): Boolean {
        return when {
            effect == HapticEffect.PRESS -> true
            effect == HapticEffect.RELEASE && hapticFeedbackType == HapticFeedbackType.PRESS_RELEASE -> true
            else -> false
        }
    }

    private fun keyEffect(
        previous: InputState,
        current: InputState,
    ): HapticEffect? {
        if (previous.digitalKeys == current.digitalKeys) {
            return null
        }

        val previouslyPressedInputs = previous.digitalKeys.size
        val currentlyPressedInputs = current.digitalKeys.size

        return if (currentlyPressedInputs >= previouslyPressedInputs) {
            HapticEffect.PRESS
        } else {
            HapticEffect.RELEASE
        }
    }

    private fun continuousDirectionEffect(
        previous: InputState,
        current: InputState,
    ): HapticEffect? {
        val previouslyActiveAnalogs =
            previous.continuousDirections
                .values.count { it != Offset.Unspecified }

        val currentlyActiveAnalogs =
            current.continuousDirections
                .values.count { it != Offset.Unspecified }

        return when {
            currentlyActiveAnalogs > previouslyActiveAnalogs -> HapticEffect.PRESS
            currentlyActiveAnalogs == previouslyActiveAnalogs -> null
            else -> HapticEffect.RELEASE
        }
    }

    private fun discreteDirectionEffect(
        previous: InputState,
        current: InputState,
    ): HapticEffect? {
        if (previous.discreteDirections == current.discreteDirections) {
            return null
        }

        val previouslyPressed =
            previous.discreteDirections.values
                .asSequence()
                .flatMap { sequenceOf(it.x > 0.5, it.x < -0.5, it.y > 0.5, it.y < -0.5) }
                .count { it }

        val currentlyPressed =
            current.discreteDirections.values
                .asSequence()
                .flatMap { sequenceOf(it.x > 0.5, it.x < -0.5, it.y > 0.5, it.y < -0.5) }
                .count { it }

        return if (currentlyPressed >= previouslyPressed) {
            HapticEffect.PRESS
        } else {
            HapticEffect.RELEASE
        }
    }
}
