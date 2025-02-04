package com.example.apptemplates.presentation.login.sign_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.example.apptemplates.presentation.login.temp.CustomTextField
import com.example.apptemplates.utils.constant.Constants
import com.example.apptemplates.utils.theme.AppTextStyles
import com.example.apptemplates.utils.theme.ThemeColors

@Composable
fun CommonUsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorText: String?,
    theme: ThemeColors,
) {
    Column {
        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            label = Constants.USERNAME_LABEL,
            labelStyle = AppTextStyles.labelStyle(theme.textColor),
            leadingIcon = Icons.TwoTone.Person,
            textColor = theme.textColor,
            containerColor = Color.Transparent,
            singleLine = true,
            keyboardType = KeyboardType.Text,
            modifier = Modifier,
            errorText = errorText
        )
    }
}