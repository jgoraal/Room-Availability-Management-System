package com.example.apptemplates.presentation.login.sign_in.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.login.temp.CustomButton
import com.example.apptemplates.utils.constant.Constants
import com.example.apptemplates.utils.theme.AppTextStyles
import com.example.apptemplates.utils.theme.ThemeColors

@Composable
fun CommonValidateButton(
    onClick: () -> Unit,
    text: String = Constants.FORGOT_PASSWORD_BUTTON,
    theme: ThemeColors
) {

    Column {
        CustomButton(
            onClick = onClick,
            text = text,
            color = theme.buttonBackgroundColor,
            border = BorderStroke(2.dp, theme.textColor),
            textColor = theme.textColor,
            textStyles = AppTextStyles.buttonStyle(theme.textColor),
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .width(250.dp),
        )
    }

}