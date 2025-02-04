package com.example.apptemplates.utils

import androidx.compose.ui.graphics.Color


interface ThemeColors {
    val headerBackgroundGradient: List<Color>
    val borderColor: Color
    val profileBackground: Color
    val textColorPrimary: Color
    val textColorSecondary: Color
    val progressColor: Color
    val progressTrackColor: Color
    val iconColor: Color
}

object LightThemeColors : ThemeColors {
    override val headerBackgroundGradient = listOf(Color(0xFFF3E5AB), Color(0xFFD7CCC8))
    override val borderColor = Color(0xFFBCAAA4)
    override val profileBackground = Color(0xFFD7CCC8)
    override val textColorPrimary = Color(0xFF5C4033)
    override val textColorSecondary = Color(0xFF4E342E)
    override val progressColor = Color(0xFF6D4C41)
    override val progressTrackColor = Color(0xFFD7CCC8)
    override val iconColor = Color(0xFF6D4C41)
}

object DarkThemeColors : ThemeColors {
    override val headerBackgroundGradient = listOf(Color(0xFF2C3E50), Color(0xFF34495E))
    override val borderColor = Color(0xFF4B79A1)
    override val profileBackground = Color(0xFF34495E)
    override val textColorPrimary = Color(0xFFECF0F1)
    override val textColorSecondary = Color(0xFFBDC3C7)
    override val progressColor = Color(0xFF16A085)
    override val progressTrackColor = Color(0xFF2C3E50)
    override val iconColor = Color(0xFF16A085)
}


interface ThemeComponentColors {
    val cardBackground: Color
    val cardGradient: List<Color>
    val primaryText: Color
    val secondaryText: Color
    val accentColor: Color
    val dividerColor: Color
    val iconColor: Color
    val selectedBackground: List<Color>
}


object LightThemeComponentsColors : ThemeComponentColors {
    override val cardBackground = Color(0xFFFDF5E6)
    override val cardGradient = listOf(Color(0xFFFDEDD4), Color(0xFFFCE5D1))
    override val primaryText = Color(0xFF4E342E)
    override val secondaryText = Color(0xFF5D4037)
    override val accentColor = Color(0xFFA8B9A4)
    override val dividerColor = Color(0xFFBCAAA4)
    override val iconColor = Color(0xFF6D4C41)
    override val selectedBackground =
        listOf(Color(0xFFA8B9A4), Color(0xFFD8CAB8))
}


object DarkThemeComponentsColors : ThemeComponentColors {
    override val cardBackground = Color(0xFF2E3B4E)
    override val cardGradient = listOf(Color(0xFF2C3E50), Color(0xFF34495E))
    override val primaryText = Color(0xFFECF0F1)
    override val secondaryText = Color(0xFFBDC3C7)
    override val accentColor = Color(0xFF16A085)
    override val dividerColor = Color(0xFF4B79A1)
    override val iconColor = Color(0xFF16A085)
    override val selectedBackground =
        listOf(Color(0xFF16A085), Color(0xFF4B79A1))
}

interface ThemeTimeLineColors {
    val cardBackground: Color
    val cardGradient: List<Color>
    val primaryText: Color
    val secondaryText: Color
    val accentColor: Color
    val dividerColor: Color
    val iconColor: Color
    val selectedBackground: List<Color>
    val timelineBackgroundGradient: List<Color>
    val timelineTitleColor: Color
    val noAvailableTextColor: Color

}


object LightThemeTimeLineColors : ThemeTimeLineColors {
    override val cardBackground = Color(0xFFFDF5E6)
    override val cardGradient = listOf(Color(0xFFFDEDD4), Color(0xFFFCE5D1))
    override val primaryText = Color(0xFF4E342E)
    override val secondaryText = Color(0xFF5D4037)
    override val accentColor = Color(0xFFA8B9A4)
    override val iconColor = Color(0xFF6D4C41)
    override val selectedBackground =
        listOf(Color(0xFFA8B9A4), Color(0xFFD8CAB8))

    override val timelineBackgroundGradient = listOf(
        Color(0xFFFDEDD4),
        Color(0xFFFCE5D1)
    )
    override val timelineTitleColor = Color(0xFF5D4037)
    override val dividerColor = Color(0xFF8D6E63)
    override val noAvailableTextColor = Color(0xFF5A6D5A)
}

object DarkThemeTimeLineColors : ThemeTimeLineColors {
    override val cardBackground = Color(0xFF2E3B4E)
    override val cardGradient = listOf(Color(0xFF2C3E50), Color(0xFF34495E))
    override val primaryText = Color(0xFFECF0F1)
    override val secondaryText = Color(0xFFBDC3C7)
    override val accentColor = Color(0xFF16A085)
    override val iconColor = Color(0xFF16A085)
    override val selectedBackground =
        listOf(Color(0xFF16A085), Color(0xFF4B79A1))

    override val timelineBackgroundGradient = listOf(
        Color(0xFF2C3E50),
        Color(0xFF34495E)
    )
    override val timelineTitleColor = Color(0xFFECF0F1)
    override val dividerColor = Color(0xFF4B79A1)
    override val noAvailableTextColor = Color(0xFFBDC3C7)
}


interface ThemeReservationColors {
    val backgroundGradient: List<Color>
    val cardBackground: Color
    val primaryText: Color
    val secondaryText: Color
    val accentColor: Color
    val borderColor: Color
    val iconColor: Color
    val buttonColor: Color
    val errorColor: Color
}


object LightThemeReservationColors : ThemeReservationColors {
    override val backgroundGradient = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))

    override val cardBackground = Color(0xFFFFF8E1)
    override val primaryText = Color(0xFF5D4037)
    override val secondaryText = Color(0xFF8D6E63)
    override val accentColor = Color(0xFFA1887F)
    override val borderColor = Color(0xFFD7CCC8)
    override val iconColor = Color(0xFF6D4C41)
    override val buttonColor = Color(0xFF8D6E63)
    override val errorColor = Color(0xFFD32F2F)
}


object DarkThemeReservationColors : ThemeReservationColors {
    override val backgroundGradient = listOf(Color(0xFF2C3E50), Color(0xFF34495E))


    override val cardBackground = Color(0xFF37474F)
    override val primaryText = Color(0xFFCFD8DC)
    override val secondaryText = Color(0xFFB0BEC5)
    override val accentColor = Color(0xFF78909C)
    override val borderColor = Color(0xFF546E7A)
    override val iconColor = Color(0xFF90A4AE)
    override val buttonColor = Color(0xFF607D8B)
    override val errorColor = Color(0xFFE57373)
}


interface ThemeHeaderColors {
    val backgroundGradient: List<Color>
    val primaryTextColor: Color
    val cardBackground: Color
    val borderColor: Color

}

object LightThemeHeaderColors : ThemeHeaderColors {
    override val backgroundGradient = listOf(
        Color(0xFFFDEDD4),
        Color(0xFFD8CAB8),
        Color(0xFFFDEDD4)
    )
    override val primaryTextColor = Color(0xFF4E342E)
    override val cardBackground = Color(0xFFFFFFFF)
    override val borderColor = Color(0xFFE0E0E0)

}

object DarkThemeHeaderColors : ThemeHeaderColors {
    override val backgroundGradient = listOf(
        Color(0xFF2C3E50),
        Color(0xFF34495E),
        Color(0xFF2C3E50)
    )
    override val primaryTextColor = Color(0xFFECF0F1)
    override val cardBackground = Color(0xFF37474F)
    override val borderColor = Color(0xFF455A64)

}


/*object CustomLightThemeColors : ThemeColors {
    override val headerBackgroundGradient = listOf(Color(0xFFF9FBE7), Color(0xFFE8F5E9))
    override val borderColor = Color(0xFFBDBDBD)
    override val profileBackground = Color(0xFFE8F5E9)
    override val textColorPrimary = Color(0xFF212121)
    override val textColorSecondary = Color(0xFF757575)
    override val progressColor = Color(0xFF4CAF50)
    override val progressTrackColor = Color(0xFFC8E6C9)
    override val iconColor = Color(0xFF4CAF50)
}

object CustomDarkThemeColors : ThemeColors {
    override val headerBackgroundGradient = listOf(Color(0xFF263238), Color(0xFF37474F))
    override val borderColor = Color(0xFF546E7A)
    override val profileBackground = Color(0xFF37474F)
    override val textColorPrimary = Color(0xFFECEFF1)
    override val textColorSecondary = Color(0xFFB0BEC5)
    override val progressColor = Color(0xFF80CBC4)
    override val progressTrackColor = Color(0xFF263238)
    override val iconColor = Color(0xFF80CBC4)
}*/






