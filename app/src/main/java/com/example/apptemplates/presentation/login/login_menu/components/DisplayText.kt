package com.example.apptemplates.presentation.login.login_menu.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.example.apptemplates.core.Constants
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors


//  Header Text ====================================================================================

@Composable
fun HeaderText(
    text: String = Constants.WELCOME_TEXT,
    themeColors: ThemeColors,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = AppTextStyles.headerStyle(themeColors.textColor),
        modifier = modifier.alpha(alpha)
    )
}

// =================================================================================================


//  Bottom Header Text =============================================================================

@Composable
fun BottomHeaderText(
    themeColors: ThemeColors,
    alpha: Float,
    delayBetweenLetters: Int,
    modifier: Modifier = Modifier
) {


    if (alpha >= 0.5f) {
        WaveText(
            text = Constants.APPEALING_TEXT,
            textStyle = AppTextStyles.subTextStyle(themeColors.textColor),
            modifier = modifier,
            delayBetweenLetters = delayBetweenLetters
        )
    }

}

// =================================================================================================