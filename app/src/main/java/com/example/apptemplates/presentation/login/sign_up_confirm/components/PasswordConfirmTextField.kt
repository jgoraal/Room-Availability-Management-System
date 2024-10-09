package com.example.apptemplates.presentation.login.sign_up_confirm.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.apptemplates.core.Constants.PASSWORD_CONFIRM_LABEL
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpFormState
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmViewModel
import com.example.apptemplates.presentation.login.temp.CustomTextField
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun PasswordConfirmTextField(
    viewModel: SignUpConfirmViewModel,
    state: SignUpFormState,
    theme: ThemeColors
) {

    Column {
        // Repeat Password TextField with Icon and Label
        CustomTextField(
            value = state.passwordConfirm,
            onValueChange = { viewModel.onFormChange(state.copy(passwordConfirm = it)) },
            isError = state.passwordConfirmError != null,
            label = PASSWORD_CONFIRM_LABEL,
            labelStyle = AppTextStyles.labelStyle(theme.textColor),
            leadingIcon = Icons.TwoTone.Lock,
            textColor = theme.textColor,
            containerColor = Color.Transparent,
            singleLine = true,
            keyboardType = KeyboardType.Password,
            modifier = Modifier,
            errorText = state.passwordConfirmError,
            trailingIcon = Icons.TwoTone.Lock,
            visualTransformation = PasswordVisualTransformation()
        )

    }


}