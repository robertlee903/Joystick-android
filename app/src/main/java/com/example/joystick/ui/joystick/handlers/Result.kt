package com.example.joystick.ui.joystick.handlers

import com.example.joystick.ui.joystick.inputstate.InputState

data class Result(val inputState: InputState, val startDragGesture: Pointer? = null)
