package com.example.apptemplates.presentation.screens.home.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.apptemplates.presentation.login.sign_in.component.getThemeTopAppBarColors
import com.example.apptemplates.presentation.navigation.route.AppScreen
import com.example.apptemplates.utils.constant.Constants
import com.example.apptemplates.utils.theme.AppTextStyles.topBarSubtitleStyle
import com.example.apptemplates.utils.theme.AppTextStyles.topBarTitleStyle
import com.example.apptemplates.utils.theme.ThemeColors

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarPreview(
    navController: NavController,
    onNavigate: (String) -> Unit
) {
    val themeColors = getThemeTopAppBarColors()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .background(themeColors.gradientBrush),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (currentRoute?.equals("home", ignoreCase = true) == true) {
                        Constants.TAG
                    } else {
                        getTopBarTitle(currentRoute)
                    },
                    style = topBarTitleStyle(themeColors.textColor),
                    textAlign = TextAlign.Center
                )
                DynamicDateText(themeColors)
            }
        },

        actions = {
            IconButton(onClick = {
                if (currentRoute != AppScreen.Main.Profile.route) {
                    onNavigate(AppScreen.Main.Profile.route)
                }
            }) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Account Icon",
                    tint = themeColors.textColor
                )
            }
        }
    )
}

@Composable
fun DynamicDateText(themeColors: ThemeColors) {
    val currentDate = Date()
    val formatter = SimpleDateFormat("EEEE, d MMMM", Locale("pl"))
    val formattedDate = formatter.format(currentDate)
    val formattedDateCapitalized = formattedDate.replaceFirstChar { it.uppercase() }

    Text(
        text = formattedDateCapitalized,
        style = topBarSubtitleStyle(themeColors.textColor),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun getTopBarTitle(route: String?): String {
    return when (route) {
        AppScreen.Main.Home.route -> "Strona Główna"
        AppScreen.Main.Reservation.route -> "Zarezerwuj"
        AppScreen.Main.RoomAvailability.route -> "Kalendarz"
        AppScreen.Main.Profile.route -> "Profil"
        AppScreen.Main.Settings.route -> "Ustawienia"
        AppScreen.Main.LogOut.route -> "Wyloguj"
        AppScreen.Main.More.route -> "Więcej"
        else -> Constants.TAG
    }
}
