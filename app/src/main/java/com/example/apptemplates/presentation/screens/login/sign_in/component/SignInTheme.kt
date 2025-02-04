package com.example.apptemplates.presentation.login.sign_in.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import com.example.apptemplates.utils.theme.AppColors
import com.example.apptemplates.utils.theme.ThemeColors


@Composable
fun getThemeColors(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    gradientCenter: Offset = Offset(300f, 500f),
    gradientRadius: Float = 1000f
): ThemeColors {

    val gradientColors = if (isDarkTheme) AppColors.darkGradient else AppColors.lightGradient

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

@Composable
fun getThemeTopAppBarColors(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
): ThemeColors {

    val gradientColors = if (isDarkTheme) AppColors.darkGradient else AppColors.lightGradient



    return ThemeColors(
        textColor = if (isDarkTheme) AppColors.darkTextColor else AppColors.lightTextColor,
        buttonBackgroundColor = if (isDarkTheme) AppColors.darkButtonColor else AppColors.buttonLightColor,
        gradientBrush = Brush.linearGradient(
            colors = gradientColors,
            tileMode = TileMode.Mirror
        )

    )
}