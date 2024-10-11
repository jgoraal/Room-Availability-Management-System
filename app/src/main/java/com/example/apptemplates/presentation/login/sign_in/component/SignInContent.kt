import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.apptemplates.form.FormKey
import com.example.apptemplates.presentation.login.sign_in.SignInViewModel
import com.example.apptemplates.presentation.login.sign_in.component.CommonEmailTextField
import com.example.apptemplates.presentation.login.sign_in.component.CommonPasswordTextField
import com.example.apptemplates.presentation.login.sign_in.component.CommonValidateButton
import com.example.apptemplates.presentation.login.sign_in.component.Header
import com.example.apptemplates.presentation.login.sign_in.component.PasswordResetText
import com.example.apptemplates.presentation.login.sign_in.component.getThemeColors
import com.example.apptemplates.presentation.login.sign_in.validation.UIState

@Composable
fun SignIn(
    viewModel: SignInViewModel,
    navigateToHome: () -> Unit,
    navigateBack: () -> Unit,
    navigateToReset: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val theme = getThemeColors()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Show Toast when max attempts are exceeded
    if (state.errors.isNotEmpty()) {
        LaunchedEffect(Unit) {
            Toast.makeText(
                context,
                state.errors.values.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            // Optional top bar or toolbar
        },
        snackbarHost = {
            if (state.uiState == UIState.Loading) {
                Snackbar(
                    action = {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                ) {
                    Text(text = "Loading, please wait...")
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = theme.gradientBrush)
                .systemBarsPadding()
                .padding(paddingValues)
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

                // Header
                Header(
                    theme = theme,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )

                Spacer(Modifier.height(16.dp))

                // Email Input
                CommonEmailTextField(
                    value = state.email,
                    onValueChange = { viewModel.onStateChange(state.copy(email = it)) },
                    isError = (state.errors[FormKey.EMAIL] != null || state.errors[FormKey.DATABASE_EMAIL] != null),
                    errorText = state.errors[FormKey.EMAIL] ?: state.errors[FormKey.DATABASE_EMAIL],
                    theme = theme
                )

                // Password Input
                CommonPasswordTextField(
                    value = state.password,
                    onValueChange = { viewModel.onStateChange(state.copy(password = it)) },
                    isError = state.errors[FormKey.PASSWORD] != null,
                    errorText = state.errors[FormKey.PASSWORD],
                    theme = theme
                )

                Spacer(Modifier.padding(10.dp))

                // Sign In Button
                CommonValidateButton(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.authenticate()
                    },
                    theme = theme
                )

                Spacer(Modifier.padding(5.dp))

                // Password Reset
                PasswordResetText(
                    state = state,
                    theme = theme,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp),
                    onResetClick = navigateToReset
                )
            }
        }
    }
}
