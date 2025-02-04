package com.example.apptemplates.presentation.screens.home.base

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.state.ScreenState
import com.example.apptemplates.presentation.state.UiError
import com.example.apptemplates.utils.theme.ThemeColors


@Composable
fun CommonErrorSnackBar(
    showError: Boolean,
    screenState: ScreenState,
    theme: ThemeColors,
    onDismissError: () -> Unit
) {
    val uiError = when (screenState) {
        is ScreenState.Error -> screenState.uiError
        else -> null
    }

    val errorMessage = when (uiError) {
        is UiError.NetworkError -> uiError.message
        is UiError.DatabaseError -> uiError.message
        is UiError.ValidationError -> "Błąd walidacji: ${uiError.message}"
        is UiError.UnknownError -> uiError.message
        is UiError.PermissionError -> uiError.message
        else -> null
    }

    if (showError && errorMessage != null) {
        Snackbar(
            containerColor = Color(0xFFFFEBEE),
            contentColor = Color(0xFFB71C1C),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(16.dp),
            action = {}
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error Icon",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))


                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFB71C1C)
                    )
                )
            }
        }
    }
}
