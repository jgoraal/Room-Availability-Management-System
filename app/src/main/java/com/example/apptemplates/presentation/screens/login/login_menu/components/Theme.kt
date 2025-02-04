package com.example.apptemplates.presentation.login.login_menu.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import com.example.apptemplates.utils.theme.AppColors
import com.example.apptemplates.utils.theme.ThemeColors


// Theme Colors ===================================================================================

@Composable
fun getThemeColors(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    gradientCenter: Offset = Offset(300f, 500f),
    gradientRadius: Float = 1000f
): ThemeColors {

    val colorShiftProgress = gradientsEffect()

    val gradientColors = if (isDarkTheme) {
        AppColors.darkBackgroundGradient(colorShiftProgress)
    } else {
        AppColors.lightBackgroundGradient(colorShiftProgress)
    }

    return ThemeColors(
        textColor = if (isDarkTheme) AppColors.darkTextColor else AppColors.lightTextColor,
        buttonBackgroundColor = if (isDarkTheme) AppColors.darkButtonColor else AppColors.buttonLightColor,
        gradientBrush = Brush.radialGradient(
            colors = gradientColors,
            center = gradientCenter,
            radius = gradientRadius,
            tileMode = TileMode.Mirror
        )
    )
}

// =================================================================================================