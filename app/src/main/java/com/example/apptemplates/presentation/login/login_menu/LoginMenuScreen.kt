package com.example.apptemplates.presentation.login.login_menu

import androidx.compose.runtime.Composable
import com.example.apptemplates.presentation.login.login_menu.components.LoginMenu


@Composable
fun LoginMenuScreen(
    viewModel: AuthViewModel,
    navigateToSignIn: () -> Unit,
    navigateToSignUp: () -> Unit
) {

    LoginMenu(
        viewModel = viewModel,
        navigateToSignIn = navigateToSignIn,
        navigateToSignUp = navigateToSignUp
    )

}