package com.example.apptemplates.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.apptemplates.utils.colorShift

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


}