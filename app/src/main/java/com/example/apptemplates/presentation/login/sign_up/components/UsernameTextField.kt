package com.example.apptemplates.presentation.login.sign_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.apptemplates.core.Constants
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpFormState
import com.example.apptemplates.presentation.login.temp.CustomTextField
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun UsernameTextField(
    viewModel: SignUpViewModel,
    state: SignUpFormState,
    theme: ThemeColors,
) {

    Column {
        // Username TextField with Icon and Label
        CustomTextField(
            value = state.username,
            onValueChange = { viewModel.onUsernameAndEmailFormChange(state.copy(username = it)) },
            isError = state.usernameError != null,
            label = Constants.USERNAME_LABEL,
            labelStyle = AppTextStyles.labelStyle(theme.textColor),
            leadingIcon = Icons.TwoTone.Person,
            textColor = theme.textColor,
            singleLine = true,
            modifier = Modifier,
            errorText = state.usernameError,
            keyboardType = KeyboardType.Text
        )
    }


}