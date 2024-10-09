package com.example.apptemplates.presentation.login.login_menu.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors


//  Menu Button ====================================================================================
@Composable
fun MenuButton(
    onClick: () -> Unit,
    themeColors: ThemeColors,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(10.dp),
    border: BorderStroke? = BorderStroke(1.dp, themeColors.textColor),
    text: String,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = themeColors.buttonBackgroundColor
        ),
        shape = shape,
        border = border,
    ) {


        Text(
            text = text,
            style = AppTextStyles.buttonStyle(themeColors.textColor),
        )

    }
}

// =================================================================================================