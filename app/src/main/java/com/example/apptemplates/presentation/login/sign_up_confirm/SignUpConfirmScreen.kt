package com.example.apptemplates.presentation.login.sign_up_confirm

import androidx.compose.runtime.Composable
import com.example.apptemplates.presentation.login.sign_up_confirm.components.SignUpConfirm

@Composable
fun SignUpConfirmScreen(
    viewModel: SignUpConfirmViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateConfirm: () -> Unit = {}
) {


    SignUpConfirm(
        viewModel = viewModel,
        onNavigateBack = onNavigateBack,
        onNavigateConfirm = onNavigateConfirm
    )


}