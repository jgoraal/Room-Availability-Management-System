package com.example.apptemplates.presentation.screens.home.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.presentation.screens.home.base.MainUiState
import com.example.apptemplates.presentation.screens.home.reservation.ReservationViewModel
import com.example.apptemplates.presentation.screens.home.room_availability.TopDownElement




@Composable
fun NumberOfAttendeesSelector(
    viewModel: ReservationViewModel,
    state: MainUiState,
    onSuccess: () -> Unit
) {
    val isNumberOfAttendeesVisible = remember { mutableStateOf(false) }


    TopDownElement(
        visible = isNumberOfAttendeesVisible,
        imageVector = Icons.Default.Groups,
        title = state.selectedAttendees.getAttendeesText(),
        titleStyle = MaterialTheme.typography.bodyLarge
    ) {
        NumberOfAttendeesView(viewModel, state, onSuccess)
    }


}


private fun Int.getAttendeesText(): String {
    return when (this) {
        1 -> "Wybierz liczbę uczestników"
        else -> "Wybrano $this uczestników"
    }
}



@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberOfAttendeesView(
    viewModel: ReservationViewModel = ReservationViewModel(),
    state: MainUiState = MainUiState(),
    onAttendeesChange: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()


    val titleColor = if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
    val backgroundColor = if (isDarkTheme) Color(0xFF2C3E50) else Color(0xFFFFF8E1)
    val borderColor = if (isDarkTheme) Color(0xFF48C9B0) else Color(0xFF8D6E63)
    val buttonBackgroundColor = if (isDarkTheme) Color(0xFF34495E) else Color(0xFFFDEDD4)
    val buttonIconColor = if (isDarkTheme) Color(0xFF1ABC9C) else Color(0xFF6D4C41)

    var attendees by remember { mutableStateOf(state.selectedAttendees.toString()) }

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isDarkTheme) listOf(
                        Color(0xFF34495E),
                        Color(0xFF2C3E50)
                    ) else listOf(Color(0xFFFCE5D1), Color(0xFFFDEDD4))
                ),
                shape = RoundedCornerShape(16.dp)
            )

            .padding(20.dp)
    ) {

        Text(
            text = "Liczba uczestników",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
        )


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(100.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
        ) {
            TextField(
                value = attendees,
                onValueChange = {
                    val number = it.toIntOrNull()
                    when {
                        number == null || number < 1 -> attendees = "1"
                        number > viewModel.getMaximumAttendees() -> attendees =
                            viewModel.getMaximumAttendees().toString()

                        else -> {
                            attendees = number.toString()
                            viewModel.changeAttendees(number)
                            onAttendeesChange()
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = titleColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = titleColor
                )
            )
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val number = attendees.toIntOrNull()
                    if (number != null && number > 1) {
                        attendees = (number - 1).toString()
                        viewModel.changeAttendees(number - 1)
                        onAttendeesChange()
                    }
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .background(
                        color = buttonBackgroundColor,
                        shape = RoundedCornerShape(50)
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease Attendees",
                    tint = buttonIconColor
                )
            }

            Text(
                text = attendees.ifEmpty { "1" },
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
            )

            IconButton(
                onClick = {
                    val number = attendees.toIntOrNull()
                    if (number != null && number < viewModel.getMaximumAttendees()) {
                        attendees = (number + 1).toString()
                        viewModel.changeAttendees(number + 1)
                        onAttendeesChange()
                    }
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .background(
                        color = buttonBackgroundColor,
                        shape = RoundedCornerShape(50)
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase Attendees",
                    tint = buttonIconColor
                )
            }
        }


        Text(
            text = "Możesz dodać od 1 do ${viewModel.getMaximumAttendees()} uczestników.",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = titleColor.copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}




