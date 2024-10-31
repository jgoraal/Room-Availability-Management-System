package com.example.apptemplates.presentation.login.login_menu

import androidx.compose.runtime.Composable
import com.example.apptemplates.presentation.login.login_menu.components.LoginMenu


@Composable
fun LoginMenuScreen(
    navigateToSignIn: () -> Unit,
    navigateToSignUp: () -> Unit
) {

    LoginMenu(
        navigateToSignIn = navigateToSignIn,
        navigateToSignUp = navigateToSignUp
    )

}