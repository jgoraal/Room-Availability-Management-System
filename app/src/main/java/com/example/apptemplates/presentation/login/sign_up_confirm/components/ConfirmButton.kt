package com.example.apptemplates.presentation.login.sign_up_confirm.components

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
import com.example.apptemplates.core.Constants.CONFIRM_BUTTON
import com.example.apptemplates.presentation.login.sign_up.validation.SignUpFormState
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmViewModel
import com.example.apptemplates.presentation.login.temp.CustomButton
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun ConfirmButton(
    viewModel: SignUpConfirmViewModel,
    state: SignUpFormState,
    onNavigateBack: () -> Unit,
    onConfirm: () -> Unit,
    theme: ThemeColors,
    focusManager: FocusManager,
) {


    Column {
        CustomButton(
            onClick = {

                focusManager.clearFocus()

                viewModel.signUp(state.copy(), onConfirm, onNavigateBack)

            },
            text = CONFIRM_BUTTON,
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