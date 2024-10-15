package com.example.apptemplates.navigation.route

private object Routes {

    // Login Graph Route
    const val AUTH = "auth"
    const val LOGIN_MENU = "login_menu"
    const val SIGN_IN = "sign_in"
    const val RESET_PASSWORD = "reset_password"
    const val SIGN_UP = "sign_up"
    const val SIGN_UP_CONFIRM = "sign_up_confirm"
    const val SIGN_UP_CONFIRM_EMAIL = "sign_up_confirm_email"


    // Main Graph Route
    const val MAIN = "main"
    const val HOME = "home"
    const val RESERVATION = "reservation"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"

}

sealed class AppScreen(val route: String) {

    data object Auth : AppScreen(Routes.AUTH) {
        data object LoginMenu : AppScreen(Routes.LOGIN_MENU)
        data object SignIn : AppScreen(Routes.SIGN_IN)
        data object ResetPassword : AppScreen(Routes.RESET_PASSWORD)
        data object SignUp : AppScreen(Routes.SIGN_UP)
        data object SignUpConfirm : AppScreen(Routes.SIGN_UP_CONFIRM)
        data object SignUpConfirmEmail : AppScreen(Routes.SIGN_UP_CONFIRM_EMAIL)
    }

    data object Main : AppScreen(Routes.MAIN) {
        data object Home : AppScreen(Routes.HOME)
        data object Reservation : AppScreen(Routes.RESERVATION)
        data object Profile : AppScreen(Routes.PROFILE)
        data object Settings : AppScreen(Routes.SETTINGS)

    }
}