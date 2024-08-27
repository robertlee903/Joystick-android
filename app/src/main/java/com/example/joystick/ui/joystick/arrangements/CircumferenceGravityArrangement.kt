package com.example.joystick.ui.joystick.arrangements

import androidx.compose.ui.geometry.Offset
import com.example.joystick.ui.joystick.utils.Constants
import com.example.joystick.ui.joystick.utils.GeometryUtils
import com.example.joystick.ui.joystick.utils.GeometryUtils.toRadians
import kotlinx.collections.immutable.persistentSetOf
import kotlin.math.cos
import kotlin.math.sin

class CircumferenceGravityArrangement(
    private val ids: List<Int>,
    private val sockets: Int,
    private val rotationInDegrees: Float,
) : GravityArrangement() {
    override fun computeGravityPoints(): List<GravityPoint> {
        if (sockets <= 1) {
            return emptyList()
        }

        val baseRotation = rotationInDegrees.toRadians()

        val primaryGravityPoints =
            ids.mapIndexed { index, id ->
                val angle = (baseRotation + Constants.PI2 * index / sockets)
                GravityPoint(
                    Offset(cos(angle), sin(angle)),
                    1f,
                    persistentSetOf(id),
                )
            }

        return primaryGravityPoints
    }

    override fun computeSize(): Float {
        if (sockets == 1) {
            return 0.5f
        }
        return GeometryUtils.computeSizeOfItemsOnCircumference(sockets)
    }
}
