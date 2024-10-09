package com.example.apptemplates.presentation.login.login_menu.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.apptemplates.core.Constants
import com.example.apptemplates.presentation.login.login_menu.AuthViewModel


//  Login Menu Screen Component ====================================================================

@Composable
fun LoginMenu(
    viewModel: AuthViewModel,
    navigateToSignIn: () -> Unit,
    navigateToSignUp: () -> Unit
) {

    val alphaAnimation = setupFadeInAnimations()

    val themeColors = getThemeColors()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = themeColors.gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            HeaderText(
                themeColors = themeColors,
                alpha = alphaAnimation.value,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))

            BottomHeaderText(
                themeColors = themeColors,
                alpha = alphaAnimation.value,
                delayBetweenLetters = 150,
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(Modifier.padding(30.dp))


            MenuButton(
                onClick = { navigateToSignIn() },
                themeColors = themeColors,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent)
                    .animateContentSize()
                    .width(320.dp),
                text = Constants.SIGN_IN_BUTTON
            )


            Spacer(Modifier.padding(8.dp))


            MenuButton(
                onClick = { navigateToSignUp() },
                themeColors = themeColors,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent)
                    .animateContentSize()
                    .width(320.dp),
                text = Constants.SIGN_UP_BUTTON
            )

        }
    }

}

// =================================================================================================