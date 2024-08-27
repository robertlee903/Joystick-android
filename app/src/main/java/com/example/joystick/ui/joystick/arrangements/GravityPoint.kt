package com.example.joystick.ui.joystick.arrangements

import androidx.compose.ui.geometry.Offset
import kotlinx.collections.immutable.ImmutableSet

data class GravityPoint(
    val position: Offset,
    val strength: Float,
    val keys: ImmutableSet<Int>,
) {
    fun distance(point: Offset): Float {
        return (point - position).getDistanceSquared() / strength
    }
}
