package com.example.apptemplates.presentation.login.sign_up_confirm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.apptemplates.core.Constants.SIGN_UP_CONFIRM_HEADER
import com.example.apptemplates.presentation.login.sign_in.component.Header
import com.example.apptemplates.presentation.login.sign_in.component.getThemeColors
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmViewModel

@Composable
fun SignUpConfirm(
    viewModel: SignUpConfirmViewModel,
    onNavigateBack: () -> Unit,
    onNavigateConfirm: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val theme = getThemeColors()

    val state by viewModel.signUpFormState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = theme.gradientBrush)
            .systemBarsPadding()
            .padding(bottom = 16.dp)  // Ensure space at the bottom of the layout
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .verticalScroll(rememberScrollState())
                .wrapContentSize(),  // Allow content to scroll if it overflows
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center  // Ensure content starts from the top
        ) {


            Header(
                text = SIGN_UP_CONFIRM_HEADER,
                theme = theme,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            Spacer(Modifier.height(16.dp))


            PasswordTextField(
                viewModel = viewModel,
                state = state,
                theme = theme,
            )


            PasswordConfirmTextField(
                viewModel = viewModel,
                state = state,
                theme = theme,
            )


            Spacer(Modifier.padding(top = 5.dp))

            ConfirmButton(
                viewModel = viewModel,
                state = state,
                onNavigateBack = onNavigateBack,
                onConfirm = { showDialog = true },
                theme = theme,
                focusManager = focusManager,
            )



            ConfirmUserDataDialog(
                viewModel = viewModel,
                state = state,
                theme = theme,
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onConfirm = onNavigateConfirm
            )


        }
    }


}