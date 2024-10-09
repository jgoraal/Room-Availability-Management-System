package com.example.apptemplates.presentation.login.temp

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String,
    color: Color,
    border: BorderStroke,
    textColor: Color,
    textStyles: TextStyle,
    modifier: Modifier = Modifier,
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        border = border,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyles
        )
    }

}