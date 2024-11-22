package com.example.apptemplates.navigation.route

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.apptemplates.navigation.route.Routes.HOME
import com.example.apptemplates.navigation.route.Routes.LOG_OUT
import com.example.apptemplates.navigation.route.Routes.MAIN
import com.example.apptemplates.navigation.route.Routes.MORE
import com.example.apptemplates.navigation.route.Routes.PROFILE
import com.example.apptemplates.navigation.route.Routes.RESERVATION
import com.example.apptemplates.navigation.route.Routes.ROOM_AVAILABILITY
import com.example.apptemplates.navigation.route.Routes.SETTINGS

private object Routes {

    // Login Graph Route
    const val AUTH = "auth"
    const val LOGIN_MENU = "login_menu"
    const val SIGN_IN = "sign_in"
    const val RESET_PASSWORD = "reset_password"
    const val SIGN_UP = "sign_up"
    const val SIGN_UP_CONFIRM = "sign_up_confirm"


    // Main Graph Route
    const val MAIN = "main"
    const val HOME = "home"
    const val RESERVATION = "reservation"
    const val ROOM_AVAILABILITY = "room_availability"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val LOG_OUT = "log_out"
    const val MORE = "more"

}

// AppScreen.kt - Define Routes
sealed class AppScreen(
    val route: String,
    val icon: ImageVector? = null,
    val label: String? = null
) {

    data object Loading : AppScreen("loading")

    data object Auth : AppScreen("auth") {
        data object LoginMenu : AppScreen("login_menu")
        data object SignIn : AppScreen("sign_in")
        data object ResetPassword : AppScreen("reset_password")
        data object SignUp : AppScreen("sign_up")
        data object SignUpConfirm : AppScreen("sign_up_confirm")
    }

    data object Main : AppScreen(MAIN) {
        data object Home : AppScreen(HOME, Icons.Default.Home, "Strona Główna")
        data object Reservation :
            AppScreen(RESERVATION, Icons.AutoMirrored.Filled.Assignment, "Rezerwacje")

        data object RoomAvailability :
            AppScreen(ROOM_AVAILABILITY, Icons.Default.CalendarMonth, "Dostępność")

        data object Profile : AppScreen(PROFILE, Icons.Default.AccountCircle, "Profil")
        data object Settings : AppScreen(SETTINGS, Icons.Default.Settings, "Ustawienia")
        data object LogOut : AppScreen(LOG_OUT, Icons.AutoMirrored.Filled.Logout, "Wyloguj")
        data object More : AppScreen(MORE, Icons.Default.Menu, "Więcej")
    }
}
