package com.example.apptemplates.utils.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {

    //==============================================================================================
    //  Text Colors
    val lightTextColor = Color.Black
    val darkTextColor = Color.White
    //==============================================================================================


    //==============================================================================================
    //  Buttons Colors
    val buttonLightColor = Color(0xFFf7e7ce)
    val darkButtonColor = Color(0xFF2E2E2E)
    //==============================================================================================


    //==============================================================================================
    //  Login Screen

    fun lightBackgroundGradient(colorShiftProgress: Float) = listOf(
        colorShift(Color(0xFFFFD194), Color(0xFFFF9A9E), colorShiftProgress),
        colorShift(Color(0xFFFF9A9E), Color(0xFF56CCF2), colorShiftProgress),
        colorShift(Color(0xFF56CCF2), Color(0xFF84BFFF), colorShiftProgress)
    )

    fun darkBackgroundGradient(colorShiftProgress: Float) = listOf(
        colorShift(Color(0xFF232526), Color(0xFF5D26C1), colorShiftProgress),
        colorShift(Color(0xFF5D26C1), Color(0xFF16A085), colorShiftProgress),
        colorShift(Color(0xFF16A085), Color(0xFF8E44AD), colorShiftProgress)
    )
    //==============================================================================================


    //==============================================================================================
    // Sign In & Sign Up Screen
    val lightGradient = listOf(Color(0xFFFFD194), Color(0xFFFF9A9E))
    val darkGradient = listOf(Color(0xFF5D26C1), Color(0xFF16A085))
    //==============================================================================================


    //==============================================================================================
    val contentBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFDEDD4),
            Color(0xFFFCE5D1),
            Color(0xFFD8CAB8),
            Color(0xFFA8B9A4),
            Color(0xFFFCE5D1),
            Color(0xFFFDEDD4),
        )
    )

    val lightContentBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFDEDD4),
            Color(0xFFFCE5D1),
            Color(0xFFE0C9A6),
            Color(0xFFD2B48C),
            Color(0xFFFCE5D1),
            Color(0xFFFDEDD4),
        )
    )


    val darkContentBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF22313F),
            Color(0xFF34495E),
            Color(0xFF1ABC9C),
            Color(0xFF16A085),
            Color(0xFF34495E),
            Color(0xFF22313F),
        )
    )
    //==============================================================================================


}


@Composable
fun getContentBackGround(): Brush {
    return if (isSystemInDarkTheme()) AppColors.darkContentBackgroundGradient else AppColors.lightContentBackgroundGradient
}