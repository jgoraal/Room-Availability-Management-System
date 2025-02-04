package com.example.apptemplates.utils.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun colorShift(startColor: Color, endColor: Color, progress: Float): Color {
    return lerp(startColor, endColor, progress)
}