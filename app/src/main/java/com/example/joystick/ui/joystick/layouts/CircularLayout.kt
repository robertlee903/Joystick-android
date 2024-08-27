package com.example.joystick.ui.joystick.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import com.example.joystick.ui.joystick.utils.Constants
import com.example.joystick.ui.joystick.utils.GeometryUtils
import com.example.joystick.ui.joystick.utils.GeometryUtils.toRadians
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
internal fun CircularLayout(
    modifier: Modifier = Modifier,
    rotationInDegrees: Float = 0f,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val layoutSize = minOf(constraints.maxHeight, constraints.maxWidth)
        val relativePlaceableSize =
            GeometryUtils.computeSizeOfItemsOnCircumference(measurables.size)
        val placeableSize = (layoutSize * relativePlaceableSize).roundToInt()

        val childConstraints =
            constraints.copy(
                minWidth = placeableSize,
                maxWidth = placeableSize,
                minHeight = placeableSize,
                maxHeight = placeableSize,
            )

        val placeables = measurables.map { it.measure(childConstraints) }
        val radius = (minOf(constraints.maxWidth, constraints.maxHeight) - placeableSize) / 2f

        val baseRotation = rotationInDegrees.toRadians()

        layout(constraints.maxWidth, constraints.maxHeight) {
            val centerX = constraints.maxWidth / 2
            val centerY = constraints.maxHeight / 2

            placeables.forEachIndexed { index, placeable ->
                val angle = baseRotation + Constants.PI2 * index / placeables.size
                val childX = (centerX + radius * cos(angle)).toInt() - placeable.width / 2
                val childY = (centerY + radius * sin(angle)).toInt() - placeable.height / 2
                placeable.place(childX, childY)
            }
        }
    }
}
