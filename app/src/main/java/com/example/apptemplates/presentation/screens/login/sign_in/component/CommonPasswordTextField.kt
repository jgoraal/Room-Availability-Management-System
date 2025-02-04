package com.example.apptemplates.presentation.login.sign_in.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.apptemplates.presentation.login.temp.CustomTextField
import com.example.apptemplates.utils.constant.Constants
import com.example.apptemplates.utils.theme.AppTextStyles
import com.example.apptemplates.utils.theme.ThemeColors

@Composable
fun CommonPasswordTextField(
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
            label = Constants.PASSWORD_CONFIRM_LABEL,
            labelStyle = AppTextStyles.labelStyle(theme.textColor),
            leadingIcon = Icons.TwoTone.Lock,
            textColor = theme.textColor,
            containerColor = Color.Transparent,
            singleLine = true,
            modifier = Modifier,
            errorText = errorText,
            trailingIcon = Icons.TwoTone.Lock,
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
        )
    }

}