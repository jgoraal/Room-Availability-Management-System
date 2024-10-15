package com.example.apptemplates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.login.sign_in.component.getThemeTopAppBarColors
import com.example.apptemplates.ui.theme.AppTextStyles.topBarSubtitleStyle
import com.example.apptemplates.ui.theme.AppTextStyles.topBarTitleStyle
import com.example.apptemplates.ui.theme.ThemeColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopBarPreview() {



    val themeColors = getThemeTopAppBarColors()





    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)) // Rounded corners for modern look
            .background(themeColors.gradientBrush)
            .padding(horizontal = 16.dp),
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                    Text(
                        text = "Roomy",
                        style = topBarTitleStyle(themeColors.textColor)
                    )


                DynamicDateText(themeColors)

            }
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Icon",
                    tint = themeColors.textColor
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Account Icon",
                    tint = themeColors.textColor
                )
            }
        }
    )
}


@Composable
fun DynamicDateText(themeColors: ThemeColors) {
    // Pobierz bieżącą datę
    val currentDate = Date()

    // Ustal format daty (np. piątek, 27 września)
    val formatter = SimpleDateFormat("EEEE, d MMMM", Locale("pl"))
    val formattedDate = formatter.format(currentDate)

    // Upewnij się, że pierwsza litera dnia tygodnia jest wielka
    val formattedDateCapitalized = formattedDate.replaceFirstChar { it.uppercase() }

    Text(
        text = formattedDateCapitalized,
        style = topBarSubtitleStyle(themeColors.textColor)
    )
}