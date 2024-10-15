package com.example.apptemplates.presentation.login.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.form.UIState


@Composable
fun TestScreen(
    viewModel: TestViewModel,
) {
    val formState by viewModel.state.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black)
    ) {

        // Pole dla adresu email
        TextField(
            value = formState.username,
            onValueChange = { username ->
                viewModel.onStateChange(formState.copy(username = username))
            },
            label = { Text("Username") },
            isError = (formState.errors[FormKey.USERNAME] != null || formState.errors[FormKey.DATABASE_USERNAME] != null),
            modifier = Modifier.fillMaxWidth()
        )
        if (formState.errors[FormKey.USERNAME] != null || formState.errors[FormKey.DATABASE_USERNAME] != null) {
            Text(
                text = formState.errors[FormKey.USERNAME]
                    ?: formState.errors[FormKey.DATABASE_USERNAME] ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pole dla adresu email
        TextField(
            value = formState.email,
            onValueChange = { email ->
                if (formState.uiState is UIState.Timeout) return@TextField else viewModel.onStateChange(
                    formState.copy(email = email)
                )
            },
            label = { Text("Email") },
            isError = formState.errors[FormKey.EMAIL] != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (formState.errors[FormKey.EMAIL] != null || formState.errors[FormKey.DATABASE_EMAIL] != null) {
            Text(
                text = formState.errors[FormKey.EMAIL]
                    ?: formState.errors[FormKey.DATABASE_EMAIL] ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pole dla hasła
        TextField(
            value = formState.password,
            onValueChange = { password ->
                viewModel.onStateChange(formState.copy(password = password))
            },
            label = { Text("Hasło") },
            visualTransformation = PasswordVisualTransformation(),
            isError = formState.errors[FormKey.PASSWORD] != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (formState.errors[FormKey.PASSWORD] != null) {
            Text(
                text = formState.errors[FormKey.PASSWORD] ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pole dla potwierdzenia hasła (jeśli jest)
        TextField(
            value = formState.confirmPassword,
            onValueChange = { confirmPassword ->
                viewModel.onStateChange(formState.copy(confirmPassword = confirmPassword))
            },
            label = { Text("Potwierdź Hasło") },
            visualTransformation = PasswordVisualTransformation(),
            isError = formState.errors[FormKey.CONFIRM_PASSWORD] != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (formState.errors[FormKey.CONFIRM_PASSWORD] != null) {
            Text(
                text = formState.errors[FormKey.CONFIRM_PASSWORD] ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Liczba prób logowania: ${formState.attempts}", color = Color.White)

        if (formState.uiState is UIState.Timeout) {
            Text(
                text = (formState.uiState as UIState.Timeout).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelLarge
            )

            val remainingTime = formState.timeoutRemaining.toString()
            Text("Zablokowany na $remainingTime sekund", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Przycisk do zatwierdzenia formularza
        Button(
            onClick = {
                viewModel.authenticate()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = true // Dezaktywacja przycisku w przypadku timeoutu
        ) {
            Text("Zatwierdź")
        }

        // Informacja o statusie
        when (formState.uiState) {
            is UIState.Loading -> {
                Text("Ładowanie...")
            }

            is UIState.Idle -> {
                Text("Gotowe do logowania")
            }

            else -> {}
        }
    }
}
