package com.example.apptemplates.presentation.login.temp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel

@Composable
fun EmailVerificationDialog(
    viewModel: SignUpViewModel,
    onDismissRequest: () -> Unit,
    onVerificationSuccess: () -> Unit
) {

    val isEmailVerified by viewModel.isEmailVerified.collectAsState()


    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Verify Your Email",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (isEmailVerified) {
                    false -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(64.dp)
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Please verify your email by clicking the link we sent you.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    true -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Email verified",
                            tint = Color(0xFF4CAF50),  // Custom Green color
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Email verified successfully!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        // You can add a small animation here if needed for success feedback
                    }

                    else -> {
                        Text(
                            text = "Verification failed. Please try again.",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { viewModel.resendEmailVerification() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Resend Verification Email", style = MaterialTheme.typography.titleLarge)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel", style = MaterialTheme.typography.titleLarge)
            }
        },
        shape = RoundedCornerShape(16.dp)  // Rounded corners for a more modern look
    )

}






