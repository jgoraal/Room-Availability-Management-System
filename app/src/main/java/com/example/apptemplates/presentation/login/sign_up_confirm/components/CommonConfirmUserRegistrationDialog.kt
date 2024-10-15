package com.example.apptemplates.presentation.login.sign_up_confirm.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Lock
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Visibility
import androidx.compose.material.icons.twotone.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.apptemplates.core.Constants.DIALOG_CANCEL_BUTTON
import com.example.apptemplates.core.Constants.DIALOG_CONFIRM_BUTTON
import com.example.apptemplates.core.Constants.DIALOG_TEXT
import com.example.apptemplates.core.Constants.DIALOG_TITLE
import com.example.apptemplates.core.Constants.EMAIL_LABEL
import com.example.apptemplates.core.Constants.PASSWORD_LABEL
import com.example.apptemplates.core.Constants.USERNAME_LABEL
import com.example.apptemplates.form.FormState
import com.example.apptemplates.ui.theme.AppTextStyles
import com.example.apptemplates.ui.theme.ThemeColors

@Composable
fun CommonConfirmUserRegistrationDialog(
    state: FormState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean,
    theme: ThemeColors
) {


    val context = LocalContext.current
    var toastMessage by remember { mutableStateOf<String?>(null) }

    if (toastMessage != null) {
        LaunchedEffect(toastMessage) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            toastMessage = null
        }
    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = DIALOG_TITLE,
                    style = AppTextStyles.dialogTitleStyle(theme.textColor),
                )
            },
            text = {
                Column {
                    Text(
                        text = DIALOG_TEXT,
                        style = AppTextStyles.dialogTextStyle(theme.textColor),
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Display the user's data in a detailed manner
                    UserDataOutlinedField(
                        label = USERNAME_LABEL,
                        value = state.username,
                        icon = Icons.TwoTone.Person,
                        theme = theme
                    )
                    UserDataOutlinedField(
                        label = EMAIL_LABEL,
                        value = state.email,
                        icon = Icons.TwoTone.Email,
                        theme = theme
                    )
                    PasswordOutlinedField(password = state.password, theme = theme)
                }
            },
            confirmButton = {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = onConfirm,
                        modifier = Modifier
                            .background(
                                theme.buttonBackgroundColor,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        border = BorderStroke(2.dp, theme.textColor),
                    ) {
                        Text(
                            text = DIALOG_CONFIRM_BUTTON,
                            style = AppTextStyles.buttonStyle(theme.textColor),
                            textAlign = TextAlign.Center
                        )
                    }
                }


            },
            dismissButton = {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(
                            text = DIALOG_CANCEL_BUTTON,
                            style = AppTextStyles.buttonStyle(theme.textColor)
                        )
                    }
                }

            },
            containerColor = theme.buttonBackgroundColor
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataOutlinedField(
    label: String,
    value: String,
    icon: ImageVector,
    theme: ThemeColors,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = {
            Text(
                text = label,
                style = AppTextStyles.labelStyle(theme.textColor),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "$label Icon",
                tint = theme.textColor
            )
        },
        readOnly = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = theme.textColor,
            unfocusedBorderColor = theme.textColor,
            focusedTextColor = theme.textColor,
            disabledTextColor = theme.textColor,
            containerColor = Color.Transparent
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordOutlinedField(
    password: String,
    theme: ThemeColors,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = if (isPasswordVisible) password else "●".repeat(password.length),
        onValueChange = {},
        label = {
            Text(
                text = PASSWORD_LABEL,
                style = AppTextStyles.labelStyle(theme.textColor)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.TwoTone.Lock,
                contentDescription = "Password Icon",
                tint = theme.textColor
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (isPasswordVisible) Icons.TwoTone.Visibility else Icons.TwoTone.VisibilityOff,
                contentDescription = if (isPasswordVisible) "Schowaj Hasło" else "Pokaż Hasło",
                tint = theme.textColor,
                modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible }
            )
        },
        readOnly = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = theme.textColor,
            unfocusedBorderColor = theme.textColor,
            focusedTextColor = theme.textColor,
            disabledTextColor = theme.textColor,
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}