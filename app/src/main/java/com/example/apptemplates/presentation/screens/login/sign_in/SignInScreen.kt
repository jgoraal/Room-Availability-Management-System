package com.example.apptemplates.presentation.login.sign_in

import SignIn
import androidx.compose.runtime.Composable


@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit,
    navigateToReset: () -> Unit
) {


    SignIn(
        viewModel = viewModel,
        navigateToHome = navigateToHome,
        navigateBack = navigateBack,
        navigateToReset = navigateToReset
    )


}




