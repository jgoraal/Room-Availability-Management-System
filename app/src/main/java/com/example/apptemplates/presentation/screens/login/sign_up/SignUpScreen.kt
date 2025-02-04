package com.example.apptemplates.presentation.login.sign_up

import androidx.compose.runtime.Composable
import com.example.apptemplates.presentation.login.sign_up.components.SignUp


@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onNavigateBack: () -> Boolean,
    onNavigateConfirm: () -> Unit
) {


    SignUp(
        viewModel = viewModel,
        onNavigateBack = onNavigateBack,
        onNavigateConfirm = onNavigateConfirm
    )

}