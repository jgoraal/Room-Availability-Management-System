package com.example.apptemplates.presentation.login.temp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Visibility
import androidx.compose.material.icons.twotone.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    label: String,
    labelStyle: TextStyle = MaterialTheme.typography.labelMedium,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    textColor: Color,
    containerColor: Color = Color.Transparent,
    focusedIndicatorColor: Color = textColor,
    unfocusedIndicatorColor: Color = textColor,
    errorContainerColor: Color = Color.Transparent,
    errorCursorColor: Color = Color.Transparent,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    errorText: String?
) {

    var showPassword by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        label = { Text(text = label, color = textColor, style = labelStyle) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "$label Icon",
                tint = textColor,
                modifier = modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        },
        trailingIcon = {
            if (trailingIcon != null && value.isNotBlank()) {
                Icon(
                    imageVector = if (showPassword) Icons.TwoTone.Visibility else Icons.TwoTone.VisibilityOff,
                    contentDescription = if (showPassword) "Show Password" else "Hide Password",
                    tint = textColor,
                    modifier = modifier
                        .size(24.dp)
                        .clickable { showPassword = !showPassword }
                )
            }
        },
        textStyle = TextStyle(color = textColor),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = containerColor,
            focusedIndicatorColor = focusedIndicatorColor,
            unfocusedIndicatorColor = unfocusedIndicatorColor,
            errorContainerColor = errorContainerColor,
            errorCursorColor = errorCursorColor,
        ),
        singleLine = singleLine,
        visualTransformation = if (showPassword) VisualTransformation.None else visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .width(320.dp)
            .padding(16.dp)
    )

    if (isError) {
        CustomTextError(errorText, modifier = modifier)
    }


}


@Composable
fun CustomTextError(
    text: String?,
    style: TextStyle = MaterialTheme.typography.labelSmall,
    color: Color = Color.Red,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {

    Text(
        text = text ?: "",
        style = style,
        color = color,
        textAlign = textAlign,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 2.dp)
    )

}