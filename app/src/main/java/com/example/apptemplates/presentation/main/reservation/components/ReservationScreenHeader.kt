package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReservationHeader() {
    val isDarkTheme = isSystemInDarkTheme()

    // Definicja gradientów na podstawie motywu
    val gradientColors = if (isDarkTheme) {
        // Gradient "Turquoise Flow" dla ciemnego motywu
        listOf(
            Color(0xFF136A8A), // #136a8a
            Color(0xFF267871)  // #267871
        )
    } else {
        // Gradient "Dance To Forget" dla jasnego motywu
        listOf(
            Color(0xFFCC3E45), // Ciemniejszy czerwony
            Color(0xFFC2B021)  // Ciemniejszy żółty
        )
    }

    // Kolor obramowania
    val borderColor = if (isDarkTheme) {
        Color(0xFF267871) // Jeden z kolorów gradientu dla spójności
    } else {
        Color(0xFFFF4E50) // Jeden z kolorów gradientu dla spójności
    }

    // Kolor tekstu
    val textColor = if (isDarkTheme) {
        Color(0xFFFFFFFF) // Biały dla ciemnego motywu
    } else {
        Color(0xFF3E2723) // Głęboki brąz dla jasnego motywu
    }

    // Styl tekstu
    val textStyle = MaterialTheme.typography.headlineMedium.copy(
        color = textColor,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                )
            )
            .border(
                BorderStroke(2.dp, borderColor),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center // Wyśrodkowanie zawartości w Box
    ) {
        Text(
            text = "Złóż rezerwację",
            style = textStyle,
        )
    }
}