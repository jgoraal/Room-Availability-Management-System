package com.example.apptemplates.presentation.screens.home.base

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.apptemplates.presentation.login.sign_in.component.getThemeTopAppBarColors
import com.example.apptemplates.presentation.navigation.route.AppScreen
import com.example.apptemplates.presentation.state.ScreenState
import com.example.apptemplates.utils.DarkThemeComponentsColors
import com.example.apptemplates.utils.LightThemeComponentsColors
import com.example.apptemplates.utils.ThemeComponentColors
import com.example.apptemplates.utils.theme.montserratFontFamily

@Composable
fun BottomBar(
    navController: NavController,
    currentScreenState: MutableState<MainUiState>,
    onNavigate: (String) -> Unit,
    onOptionsClick: () -> Unit
) {
    val themeBrush = getThemeTopAppBarColors()
    val isDarkTheme = isSystemInDarkTheme()


    val backgroundGradient = themeBrush.gradientBrush

    val theme = if (isDarkTheme) DarkThemeComponentsColors else LightThemeComponentsColors

    val items = listOf(
        AppScreen.Main.Home,
        AppScreen.Main.RoomAvailability,
        AppScreen.Main.Reservation,
        AppScreen.Main.More
    )

    NavigationBar(
        modifier = Modifier
            .background(backgroundGradient)
            .padding(horizontal = 8.dp),
        containerColor = Color.Transparent,
        contentColor = theme.primaryText,
        tonalElevation = 10.dp
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                icon = { NavigationBarItemIcon(screen, currentRoute, theme) },
                label = { NavigationBarItemText(screen, currentRoute, theme) },
                selected = selected,
                onClick = {
                    if (currentScreenState.value.screenState == ScreenState.Loading) return@NavigationBarItem
                    if (currentRoute != screen.route) {
                        if (screen.route == AppScreen.Main.More.route) onOptionsClick()
                        else onNavigate(screen.route)
                    }
                },
                colors = navigationBarItemColors(theme, isDarkTheme)
            )
        }
    }
}

@Composable
private fun NavigationBarItemIcon(
    screen: AppScreen,
    currentRoute: String?,
    theme: ThemeComponentColors
) {
    val selected = currentRoute == screen.route
    val scale = if (selected) 1.3f else 1f
    Icon(
        imageVector = screen.icon!!,
        contentDescription = screen.label!!,
        modifier = Modifier
            .size(24.dp)
            .scale(scale),
        tint = if (selected) theme.primaryText else theme.primaryText.copy(alpha = 0.6f)
    )
}

@Composable
private fun NavigationBarItemText(
    screen: AppScreen,
    currentRoute: String?,
    theme: ThemeComponentColors
) {
    val selected = currentRoute == screen.route
    Text(
        text = screen.label!!,
        fontFamily = montserratFontFamily,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        fontSize = if (selected) 12.sp else 10.sp,
        color = if (selected) theme.primaryText else theme.secondaryText
    )
}

@Composable
private fun navigationBarItemColors(
    theme: ThemeComponentColors,
    isDarkTheme: Boolean
): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = theme.iconColor,
        unselectedIconColor = theme.iconColor.copy(alpha = 0.6f),
        selectedTextColor = theme.primaryText,
        unselectedTextColor = theme.secondaryText,
        indicatorColor = Color.Transparent
    )
}

