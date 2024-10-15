package com.example.apptemplates.presentation.login.sign_up.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.apptemplates.core.Constants
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.form.UIState
import com.example.apptemplates.navigation.event.NavigationEvent
import com.example.apptemplates.presentation.login.sign_in.component.CommonEmailTextField
import com.example.apptemplates.presentation.login.sign_in.component.CommonLoadingSnackbar
import com.example.apptemplates.presentation.login.sign_in.component.CommonValidateButton
import com.example.apptemplates.presentation.login.sign_in.component.Header
import com.example.apptemplates.presentation.login.sign_in.component.getThemeColors
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel

@Composable
fun SignUp(
    viewModel: SignUpViewModel,
    onNavigateBack: () -> Boolean,
    onNavigateConfirm: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val theme = getThemeColors()

    val state by viewModel.state.collectAsState()

    val navigationEvent by viewModel.navigationEvent.collectAsState(initial = null)

    val isLoading = state.uiState == UIState.Loading


    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {

            is NavigationEvent.NavigateOnSuccess -> {
                onNavigateConfirm()
                viewModel.resetNavigation() // Reset after handling navigation
            }

            is NavigationEvent.ShowError -> {
                // Optionally show an error message (Toast, Snackbar, etc.)
                val error = (navigationEvent as NavigationEvent.ShowError).message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                viewModel.resetNavigation()
            }

            null -> { /* No event to handle */
            }
        }
    }


    Scaffold(
        topBar = { /* Optional top bar or toolbar*/ },
        snackbarHost = { CommonLoadingSnackbar(isLoading, theme) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = theme.gradientBrush)
                .systemBarsPadding()
                .padding(innerPadding)  // Ensure space at the bottom of the layout
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
                    text = Constants.SIGN_UP_HEADER,
                    theme = theme,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )

                Spacer(Modifier.height(16.dp))


                CommonUsernameTextField(
                    value = state.username,
                    onValueChange = {
                        if (state.uiState is UIState.Timeout) return@CommonUsernameTextField else viewModel.onStateChange(
                            state.copy(username = it)
                        )
                    },
                    isError = (state.errors[FormKey.USERNAME] != null || state.errors[FormKey.DATABASE_USERNAME] != null),
                    errorText = state.errors[FormKey.USERNAME]
                        ?: state.errors[FormKey.DATABASE_USERNAME],
                    theme = theme
                )

                CommonEmailTextField(
                    value = state.email,
                    onValueChange = {
                        if (state.uiState is UIState.Timeout) return@CommonEmailTextField else viewModel.onStateChange(
                            state.copy(email = it)
                        )
                    },
                    isError = (state.errors[FormKey.EMAIL] != null || state.errors[FormKey.DATABASE_EMAIL] != null),
                    errorText = state.errors[FormKey.EMAIL] ?: state.errors[FormKey.DATABASE_EMAIL],
                    theme = theme
                )


                Spacer(Modifier.padding(top = 5.dp))



                CommonValidateButton(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.authenticate()
                    }, theme = theme, text = Constants.VALIDATE_BUTTON
                )


            }
        }

    }


}