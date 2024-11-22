package com.example.apptemplates.presentation.main.temp

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

// Jasny motyw
object LightThemeComponentsColors : ThemeComponentColors {
    override val cardBackground = Color(0xFFFDF5E6) // Jasny kremowy
    override val cardGradient = listOf(Color(0xFFFDEDD4), Color(0xFFFCE5D1)) // Zgodny z tłem
    override val primaryText = Color(0xFF4E342E) // Ciepły brąz
    override val secondaryText = Color(0xFF5D4037) // Ciemniejszy brąz
    override val accentColor = Color(0xFFA8B9A4) // Delikatna zieleń
    override val dividerColor = Color(0xFFBCAAA4) // Jasny beż
    override val iconColor = Color(0xFF6D4C41) // Brązowy
    override val selectedBackground =
        listOf(Color(0xFFA8B9A4), Color(0xFFD8CAB8)) // Gradient dla wybranych elementów
}

// Ciemny motyw
object DarkThemeComponentsColors : ThemeComponentColors {
    override val cardBackground = Color(0xFF2E3B4E) // Ciemny niebieski
    override val cardGradient = listOf(Color(0xFF2C3E50), Color(0xFF34495E)) // Zgodny z tłem
    override val primaryText = Color(0xFFECF0F1) // Jasny szary
    override val secondaryText = Color(0xFFBDC3C7) // Szary
    override val accentColor = Color(0xFF16A085) // Turkus
    override val dividerColor = Color(0xFF4B79A1) // Niebieski
    override val iconColor = Color(0xFF16A085) // Turkus
    override val selectedBackground =
        listOf(Color(0xFF16A085), Color(0xFF4B79A1)) // Gradient dla wybranych elementów
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
    override val cardBackground = Color(0xFFFDF5E6) // Jasny kremowy
    override val cardGradient = listOf(Color(0xFFFDEDD4), Color(0xFFFCE5D1)) // Zgodny z tłem
    override val primaryText = Color(0xFF4E342E) // Ciepły brąz
    override val secondaryText = Color(0xFF5D4037) // Ciemniejszy brąz
    override val accentColor = Color(0xFFA8B9A4) // Delikatna zieleń
    override val iconColor = Color(0xFF6D4C41) // Brązowy
    override val selectedBackground =
        listOf(Color(0xFFA8B9A4), Color(0xFFD8CAB8)) // Gradient dla wybranych elementów

    override val timelineBackgroundGradient = listOf(
        Color(0xFFFDEDD4),
        Color(0xFFFCE5D1)
    )
    override val timelineTitleColor = Color(0xFF5D4037) // Ciepły, ciemny brąz
    override val dividerColor = Color(0xFF8D6E63) // Przygaszony brąz
    override val noAvailableTextColor = Color(0xFF5A6D5A) // Stonowany szaro-zielony
}

object DarkThemeTimeLineColors : ThemeTimeLineColors {
    override val cardBackground = Color(0xFF2E3B4E) // Ciemny niebieski
    override val cardGradient = listOf(Color(0xFF2C3E50), Color(0xFF34495E)) // Zgodny z tłem
    override val primaryText = Color(0xFFECF0F1) // Jasny szary
    override val secondaryText = Color(0xFFBDC3C7) // Szary
    override val accentColor = Color(0xFF16A085) // Turkus
    override val iconColor = Color(0xFF16A085) // Turkus
    override val selectedBackground =
        listOf(Color(0xFF16A085), Color(0xFF4B79A1)) // Gradient dla wybranych elementów

    override val timelineBackgroundGradient = listOf(
        Color(0xFF2C3E50),
        Color(0xFF34495E)
    )
    override val timelineTitleColor = Color(0xFFECF0F1) // Jasny szary
    override val dividerColor = Color(0xFF4B79A1) // Niebieski
    override val noAvailableTextColor = Color(0xFFBDC3C7) // Jasny szary
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

    // Kolory komponentów
    override val cardBackground = Color(0xFFFFF8E1) // Jasny kremowy
    override val primaryText = Color(0xFF5D4037) // Ciepły brąz
    override val secondaryText = Color(0xFF8D6E63) // Przygaszony brąz
    override val accentColor = Color(0xFFA1887F) // Delikatny różowo-brązowy
    override val borderColor = Color(0xFFD7CCC8) // Jasny beż
    override val iconColor = Color(0xFF6D4C41) // Brązowy
    override val buttonColor = Color(0xFF8D6E63) // Brązowy dla przycisków
    override val errorColor = Color(0xFFD32F2F) // Czerwony dla błędów
}


object DarkThemeReservationColors : ThemeReservationColors {
    override val backgroundGradient = listOf(Color(0xFF2C3E50), Color(0xFF34495E))

    // Kolory komponentów
    override val cardBackground = Color(0xFF37474F) // Ciemny szaro-niebieski
    override val primaryText = Color(0xFFCFD8DC) // Jasny szary
    override val secondaryText = Color(0xFFB0BEC5) // Przygaszony szary
    override val accentColor = Color(0xFF78909C) // Szaro-niebieski
    override val borderColor = Color(0xFF546E7A) // Ciemny szary
    override val iconColor = Color(0xFF90A4AE) // Szaro-niebieski
    override val buttonColor = Color(0xFF607D8B) // Szaro-niebieski dla przycisków
    override val errorColor = Color(0xFFE57373) // Jasny czerwony dla błędów
}





