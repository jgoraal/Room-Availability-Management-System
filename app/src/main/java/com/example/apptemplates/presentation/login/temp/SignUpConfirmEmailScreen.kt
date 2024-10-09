package com.example.apptemplates.presentation.login.temp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

    val isEmailVerified by viewModel.isEmailVerified.collectAsState()

    /*if (isEmailVerified) {
        Text("Email verified successfully!")
        // Możesz tutaj dodać przekierowanie do głównego okna, np.:
        // onEmailVerified()
    } else {
        AlertDialog(
            onDismissRequest = { *//* Do something *//* },
            title = { Text(text = "Potwierdź email") },
            text = { Text("Nie możesz kontynuować bez potwierdzenia adresu e-mail.") },
            confirmButton = {
                Button(onClick = { *//* Ponowne wysyłanie maila weryfikacyjnego *//* }) {
                    Text("Wyślij ponownie e-mail weryfikacyjny")
                }
            }
        )
    }*/



    if (showDialog) {
        EmailVerificationDialog(
            viewModel = viewModel,
            onDismissRequest = { showDialog = true },
            onVerificationSuccess = {
                showDialog = true
                // Proceed to the next screen or action
            }
        )
    }


}

