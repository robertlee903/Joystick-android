package com.example.joystick.ui.joystick.arrangements

object EmptyArrangement : GravityArrangement() {
    override fun computeGravityPoints(): List<GravityPoint> {
        return emptyList()
    }

    override fun computeSize(): Float {
        return 0f
    }
}
