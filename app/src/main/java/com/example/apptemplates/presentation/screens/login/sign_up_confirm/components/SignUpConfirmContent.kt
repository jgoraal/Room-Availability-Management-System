package com.example.apptemplates.presentation.login.sign_up_confirm.components

import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.login.sign_in.component.CommonLoadingSnackbar
import com.example.apptemplates.presentation.login.sign_in.component.CommonPasswordTextField
import com.example.apptemplates.presentation.login.sign_in.component.CommonValidateButton
import com.example.apptemplates.presentation.login.sign_in.component.Header
import com.example.apptemplates.presentation.login.sign_in.component.getThemeColors
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmViewModel
import com.example.apptemplates.presentation.navigation.event.NavigationEvent
import com.example.apptemplates.presentation.state.FormKey
import com.example.apptemplates.presentation.state.UIState
import com.example.apptemplates.utils.constant.Constants.CONFIRM_BUTTON
import com.example.apptemplates.utils.constant.Constants.SIGN_UP_CONFIRM_HEADER

@Composable
fun SignUpConfirm(
    viewModel: SignUpConfirmViewModel,
    onNavigateBack: () -> Unit,
    onNavigateConfirm: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val theme = getThemeColors()
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val navigationEvent by viewModel.navigationEvent.collectAsState(initial = null)
    val isLoading = state.uiState == UIState.Loading



    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {

            is NavigationEvent.NavigateOnSuccess -> {
                Toast.makeText(context, "Rejestracja przebiegła pomyślnie", Toast.LENGTH_LONG)
                    .show()
                onNavigateConfirm()
                viewModel.resetNavigation()
                viewModel.clearState()
            }

            is NavigationEvent.ShowError -> {

                val error = (navigationEvent as NavigationEvent.ShowError).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                viewModel.resetNavigation()
            }

            null -> {}
        }
    }

    Scaffold(
        topBar = {},
        snackbarHost = { CommonLoadingSnackbar(isLoading, theme) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = theme.gradientBrush)
                .systemBarsPadding()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
                    .verticalScroll(rememberScrollState())
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Header(
                    text = SIGN_UP_CONFIRM_HEADER,
                    theme = theme,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )

                Spacer(Modifier.height(16.dp))



                CommonPasswordTextField(
                    value = state.password,
                    onValueChange = {
                        if (state.uiState is UIState.Timeout) return@CommonPasswordTextField else viewModel.onStateChange(
                            state.copy(password = it)
                        )
                    },
                    isError = state.errors[FormKey.PASSWORD] != null,
                    errorText = state.errors[FormKey.PASSWORD],
                    theme = theme
                )


                CommonPasswordTextField(
                    value = state.confirmPassword,
                    onValueChange = {
                        if (state.uiState is UIState.Timeout) return@CommonPasswordTextField else viewModel.onStateChange(
                            state.copy(confirmPassword = it)
                        )
                    },
                    isError = state.errors[FormKey.CONFIRM_PASSWORD] != null,
                    errorText = state.errors[FormKey.CONFIRM_PASSWORD],
                    theme = theme
                )


                Spacer(Modifier.padding(top = 5.dp))



                CommonValidateButton(
                    onClick = {
                        if (state.errors.isEmpty()) showDialog = true
                    },
                    theme = theme, text = CONFIRM_BUTTON
                )


                CommonConfirmUserRegistrationDialog(
                    state = state,
                    onConfirm = {
                        showDialog = false
                        focusManager.clearFocus()
                        viewModel.authenticate()
                    },
                    onDismiss = { showDialog = false },
                    showDialog = showDialog,
                    theme = theme
                )


            }
        }

    }


}