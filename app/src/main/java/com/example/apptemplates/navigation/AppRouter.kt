package com.example.apptemplates.navigation

import androidx.navigation.NamedNavArgument

private object Routes {

    // Login Graph Route
    const val AUTH = "auth"
    const val LOGIN_MENU = "login_menu"
    const val SIGN_IN = "sign_in"
    const val RESET_PASSWORD = "reset_password"
    const val SIGN_UP = "sign_up"
    const val SIGN_UP_CONFIRM = "sign_up_confirm"
    const val SIGN_UP_CONFIRM_EMAIL = "sign_up_confirm_email"


    // Second Graph Route
    const val MAIN = "main"
    const val HOME = "home"

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

        data object Home : TopLevelDestination(
            route = Routes.HOME,
            title = 1,
//            selectedIcon = AppIcons.HomeFilled,
//            unselectedIcon = AppIcons.HomeOutlined,
        )
    }
}

sealed class TopLevelDestination(
    val route: String,
    val title: Int? = null,
//    val selectedIcon: ImageVector? = null,
//    val unselectedIcon: ImageVector? = null,
    val navArguments: List<NamedNavArgument> = emptyList()
)