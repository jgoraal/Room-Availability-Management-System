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
import com.example.apptemplates.presentation.state.FormState
import com.example.apptemplates.presentation.state.UIState
import com.example.apptemplates.utils.theme.AppTextStyles
import com.example.apptemplates.utils.theme.ThemeColors

@Composable
fun PasswordResetText(
    state: FormState,
    theme: ThemeColors,
    modifier: Modifier = Modifier,
    onResetClick: () -> Unit
) {

    if (state.attempts > 0) {


        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = "Nie pamiętasz hasła?",
                style = AppTextStyles.hintStyle(theme.textColor),
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
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}