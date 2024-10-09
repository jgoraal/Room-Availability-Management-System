package com.example.apptemplates.presentation.login.temp


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode


fun GradientBrush(
    colors: List<Color>,
    center: Offset = Offset(300f, 500f),
    radius: Float = 1000f,
    tileMode: TileMode = TileMode.Mirror
): Brush {

    return Brush.radialGradient(
        colors = colors!!,
        center = center,
        radius = radius,
        tileMode = tileMode
    )
}