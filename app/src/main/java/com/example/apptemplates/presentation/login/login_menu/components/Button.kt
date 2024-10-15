package com.example.apptemplates.presentation.login.login_menu.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
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
    icon: ImageVector
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
        //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Icon aligned to the start (left)
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = themeColors.textColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Text aligned to the center of the button
            Text(
                text = text,
                style = AppTextStyles.buttonStyle(themeColors.textColor),
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}

// =================================================================================================