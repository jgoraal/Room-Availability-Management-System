package com.example.apptemplates

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    // Google Font Provider Setup
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    // Font for the NavigationBar labels (Montserrat)
    val montserrat = GoogleFont("Montserrat")
    val montserratFontFamily = FontFamily(
        Font(
            googleFont = montserrat,
            fontProvider = provider,
            weight = FontWeight.W500, // Medium weight for label text
        )
    )

    // Define a gradient for the BottomBar background
    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3))
    )

    // List of bottom bar items (you can customize this list)
    val items = listOf(
        BottomBarItem(Icons.Default.Home, "Home"),
        BottomBarItem(Icons.Default.Search, "Search"),
        BottomBarItem(Icons.Default.Favorite, "Favorites"),
        BottomBarItem(Icons.Default.Person, "Profile")
    )

    // Remember the currently selected item index
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Animation scale for selected icon
    val scale by animateFloatAsState(targetValue = if (selectedIndex == 0) 1.2f else 1f, label = "")

    Box(Modifier.fillMaxWidth().padding(0.dp)) { // Add padding around the BottomBar for rounded corners to display correctly
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundGradient), // Apply gradient background
            containerColor = Color.Transparent, // Transparent container color
            contentColor = Color.White, // Icon and text color
            tonalElevation = 10.dp // Elevation for shadow effect
        ) {
            // Iterate over each item and create a BottomBar icon and label
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) Color.White else Color.LightGray, // Highlight selected item
                            modifier = Modifier
                                .size(24.dp)
                                .scale(if (isSelected) 1.2f else 1f) // Scale the icon when selected
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontFamily = montserratFontFamily, // Apply custom font (Montserrat)
                            fontWeight = FontWeight.Medium, // Medium weight for the labels
                            color = if (isSelected) Color.White else Color.LightGray,
                            fontSize = 12.sp // Small font for labels
                        )
                    },
                    selected = isSelected,
                    onClick = { selectedIndex = index },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.LightGray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.LightGray
                    ),
                    alwaysShowLabel = true // Show labels even when not selected
                )
            }
        }
    }
}

// Helper data class for BottomBar items
data class BottomBarItem(val icon: ImageVector, val label: String)



