package com.example.apptemplates.presentation.login.sign_in.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.apptemplates.form.FormState
import com.example.apptemplates.form.UIState
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun PasswordResetText(
    state: FormState,
    theme: ThemeColors,
    modifier: Modifier = Modifier,
    onResetClick: () -> Unit
) {

    if (state.attempts > 0) {

        // Row for "Nie pamiętasz hasła?" and "Resetuj"
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically // Ensure text and button are aligned
        ) {
            BasicText(
                text = "Nie pamiętasz hasła?",
                style = AppTextStyles.hintStyle(theme.textColor),
                //modifier = Modifier.padding(end = 4.dp)  // Space between texts
            )

            TextButton(
                onClick = {
                    if (state.uiState !is UIState.Timeout)
                        onResetClick()
                },
            ) {
                Text(
                    text = "Resetuj",
                    style = AppTextStyles.labelStyle(
                        if (theme.textColor == Color.White) Color(
                            0xFFFFF8E1
                        ) else Color(0xFF2E2E2E)
                    )
                        .copy(textDecoration = TextDecoration.Underline),
                )
            }
        }

        if (state.uiState is UIState.Timeout) {
            val remainingTime = state.timeoutRemaining.toString()
            Text(
                text = "Zablokowany na $remainingTime sekund",
                style = AppTextStyles.errorStyle(Color(0xFFba0f30)),
                modifier = Modifier.padding(top = 4.dp) // Add padding for spacing
            )
        }
    }
}