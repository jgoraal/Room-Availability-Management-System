package com.example.apptemplates.presentation.login.sign_in.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.login.sign_in.validation.SignInState
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun PasswordResetText(
    state: SignInState,
    theme: ThemeColors,
    modifier: Modifier = Modifier,
    onResetClick: () -> Unit
) {

    if (state.attempts > 0) {

        // Row for "Nie pamiętasz hasła?" and "Resetuj"
        Row(
            modifier = modifier
        ) {
            BasicText(
                text = "Nie pamiętasz hasła? próba = ${state.attempts}",
                style = TextStyle(
                    color = theme.textColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = TextUnit.Unspecified
                ),
                modifier = Modifier.padding(end = 4.dp)  // Space between texts
            )

            BasicText(
                text = "Resetuj",
                style = TextStyle(
                    color = theme.textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit.Unspecified
                ),
                modifier = Modifier
                    .clickable(onClick = onResetClick)
                    .padding(horizontal = 4.dp)// Only "Resetuj" is clickable
            )
        }

    }


}