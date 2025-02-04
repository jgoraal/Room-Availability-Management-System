package com.example.apptemplates.presentation.login.sign_in.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.apptemplates.utils.constant.Constants
import com.example.apptemplates.utils.theme.AppTextStyles
import com.example.apptemplates.utils.theme.ThemeColors

@Composable
fun Header(
    text: String = Constants.SIGN_IN_HEADER,
    theme: ThemeColors,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = AppTextStyles.signInHeader(theme.textColor),
        color = theme.textColor,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}