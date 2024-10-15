package com.example.apptemplates.presentation.login.password_reset

import androidx.compose.runtime.Composable
import com.example.apptemplates.presentation.login.password_reset.components.PasswordReset

@Composable
fun PasswordResetScreen(
    viewModel: ResetPasswordViewModel,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {


    PasswordReset(
        viewModel = viewModel,
        onConfirm = onConfirm,
        onBack = onBack
    )

}