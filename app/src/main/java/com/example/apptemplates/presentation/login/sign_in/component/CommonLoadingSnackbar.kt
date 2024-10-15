package com.example.apptemplates.presentation.login.sign_in.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun CommonLoadingSnackbar(
    isLoading: Boolean,
    theme: ThemeColors
) {
    if (isLoading) {
        Snackbar(
            containerColor = theme.buttonBackgroundColor, // Set a soft, muted color for a more modern look
            contentColor = Color.White, // White content to contrast the dark background
            shape = RoundedCornerShape(8.dp), // Rounded corners for a modern appearance
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start // Align elements in a natural reading order
            ) {
                // CircularProgressIndicator on the left side of the text
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(20.dp), // Slightly smaller for elegance
                    color = theme.textColor,
                    strokeWidth = 2.dp
                )

                // Loading text
                Text(
                    text = "Ładowanie, proszę poczekać...",
                    style = AppTextStyles.dialogTextStyle(theme.textColor)
                )
            }
        }
    }
}