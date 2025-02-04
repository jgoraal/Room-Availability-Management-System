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
import com.example.apptemplates.utils.theme.AppTextStyles
import com.example.apptemplates.utils.theme.ThemeColors


@Composable
fun CommonLoadingSnackbar(
    isLoading: Boolean,
    theme: ThemeColors
) {
    if (isLoading) {
        Snackbar(
            containerColor = theme.buttonBackgroundColor,
            contentColor = Color.White,
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(20.dp),
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