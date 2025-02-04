package com.example.apptemplates.presentation.login.temp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel

@Composable
fun SignUpConfirmEmailScreen(
    viewModel: SignUpViewModel
) {
    var showDialog by remember { mutableStateOf(true) }





    if (showDialog) {
        EmailVerificationDialog(
            viewModel = viewModel,
            onDismissRequest = { showDialog = true },
            onVerificationSuccess = {
                showDialog = true

            }
        )
    }


}

