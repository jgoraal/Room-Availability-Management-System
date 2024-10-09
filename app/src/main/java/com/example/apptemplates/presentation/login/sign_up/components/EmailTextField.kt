package com.example.apptemplates.presentation.login.sign_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.example.apptemplates.core.Constants
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpFormState
import com.example.apptemplates.presentation.login.temp.CustomTextField
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun EmailTextField(
    viewModel: SignUpViewModel,
    state: SignUpFormState,
    theme: ThemeColors
) {

    Column {
        // Email TextField with Icon and Label
        CustomTextField(
            value = state.email,
            onValueChange = { viewModel.onUsernameAndEmailFormChange(state.copy(email = it)) },
            isError = state.emailError != null,
            label = Constants.EMAIL_LABEL,
            labelStyle = AppTextStyles.labelStyle(theme.textColor),
            leadingIcon = Icons.TwoTone.Email,
            textColor = theme.textColor,
            containerColor = Color.Transparent,
            singleLine = true,
            keyboardType = KeyboardType.Email,
            modifier = Modifier,
            errorText = state.emailError,
        )

    }


}