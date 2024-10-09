package com.example.apptemplates.presentation.login.sign_up_confirm.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.apptemplates.core.Constants.PASSWORD_LABEL
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpFormState
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmViewModel
import com.example.apptemplates.presentation.login.temp.CustomTextField
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun PasswordTextField(
    viewModel: SignUpConfirmViewModel,
    state: SignUpFormState,
    theme: ThemeColors
) {

    Column {
        // Password TextField with Icon and Label
        CustomTextField(
            value = state.password,
            onValueChange = { viewModel.onFormChange(state.copy(password = it)) },
            isError = state.passwordError != null,
            label = PASSWORD_LABEL,
            labelStyle = AppTextStyles.labelStyle(theme.textColor),
            leadingIcon = Icons.TwoTone.Lock,
            textColor = theme.textColor,
            singleLine = true,
            modifier = Modifier,
            errorText = state.passwordError,
            keyboardType = KeyboardType.Password,
            trailingIcon = Icons.TwoTone.Lock,
            visualTransformation = PasswordVisualTransformation()
        )
    }

}