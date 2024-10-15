package com.example.apptemplates

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.presentation.login.sign_in.component.getThemeTopAppBarColors

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {

    val theme = getThemeTopAppBarColors()
    val isDarkTheme = isSystemInDarkTheme()

    // List of bottom bar items (you can customize this list)
    val items = listOf(
        BottomBarItem(Icons.Default.Home, "Home"),
        BottomBarItem(Icons.Default.Search, "Search"),
        BottomBarItem(Icons.Default.Favorite, "Favorites"),
        BottomBarItem(Icons.Default.Person, "Profile")
    )

    // Remember the currently selected item index
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(
        Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        NavigationBar(
            modifier = Modifier
                .background(theme.gradientBrush)
                .padding(horizontal = 8.dp), // Apply horizontal padding for spacing
            containerColor = Color.Transparent,
            contentColor = theme.textColor,
            tonalElevation = 10.dp
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(28.dp) // Increase icon size slightly
                                .scale(if (isSelected) 1.2f else 1f)
                                .padding(4.dp) // Add padding around icon
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontFamily = montserratFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp, // Slightly larger font size for better readability
                            modifier = Modifier.padding(bottom = 4.dp) // Add some padding below the text
                        )
                    },
                    selected = isSelected,
                    onClick = { selectedIndex = index },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = theme.textColor,
                        unselectedIconColor = if (isDarkTheme) Color(0xFFD1D1D1) else Color(
                            0xFF3C4043
                        ),
                        selectedTextColor = theme.textColor,
                        unselectedTextColor = if (isDarkTheme) Color(0xFFD1D1D1) else Color(
                            0xFF3C4043
                        ),
                        indicatorColor = Color.Transparent
                    ),
                    alwaysShowLabel = true
                )
            }
        }
    }
}

// Helper data class for BottomBar items
data class BottomBarItem(val icon: ImageVector, val label: String)



