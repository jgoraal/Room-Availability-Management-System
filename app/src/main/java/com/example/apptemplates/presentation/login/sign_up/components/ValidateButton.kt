package com.example.apptemplates.presentation.login.sign_up.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.dp
import com.example.apptemplates.core.Constants.VALIDATE_BUTTON
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpFormState
import com.example.apptemplates.presentation.login.temp.CustomButton
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun ValidateButton(
    viewModel: SignUpViewModel,
    state: SignUpFormState,
    focusManager: FocusManager,
    theme: ThemeColors,
    onNavigateConfirm: () -> Unit
) {
    Column {
        CustomButton(
            onClick = {
                focusManager.clearFocus()
                viewModel.goToPasswordFields(state.copy(), onNavigateConfirm)
            },
            text = VALIDATE_BUTTON,
            color = theme.buttonBackgroundColor,
            border = BorderStroke(2.dp, theme.textColor),
            textColor = theme.textColor,
            textStyles = AppTextStyles.buttonStyle(theme.textColor),
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .width(320.dp),
        )
    }
}