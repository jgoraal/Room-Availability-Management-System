package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.temp.MainUiState

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberOfAttendeesView(
    viewModel: ReservationViewModel = ReservationViewModel(),
    state: MainUiState = MainUiState(),
    onAttendeesChange: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var attendees by remember { mutableStateOf(state.selectedAttendees.toString()) }

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFFF9F9F9), // Neutral background
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                Color(0xFFB0BEC5),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        // Title
        Text(
            text = "Liczba uczestników",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F) // Dark gray for contrast
            )
        )

        // Compact Attendees Input Field
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(100.dp)
                .height(70.dp) // Adjusted height for better text visibility
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF3E5F5)) // Light purple background
                .border(1.dp, Color(0xFFBA68C8), RoundedCornerShape(12.dp)) // Purple border
        ) {
            TextField(
                value = attendees,
                onValueChange = {
                    val number = it.toIntOrNull()
                    when {
                        number == null || number < 1 -> attendees = "1" // Minimum value
                        number > viewModel.getMaximumAttendees() -> attendees =
                            viewModel.getMaximumAttendees().toString() // Maximum value
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
                    fontSize = 24.sp, // Larger font size for clear visibility
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, // Ensures text is centered horizontally
                    color = Color(0xFF6A1B9A) // Vibrant purple for input text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp), // Ensures no clipping of text vertically
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // Ensures no additional background
                    focusedIndicatorColor = Color.Transparent, // No underlines
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF6A1B9A) // Purple cursor
                )
            )
        }



        // Increase/Decrease Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrease Button
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
                        color = Color(0xFFBBDEFB), // Light blue background
                        shape = RoundedCornerShape(50)
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease Attendees",
                    tint = Color(0xFF1976D2) // Blue icon color
                )
            }

            // Current Attendees Count
            Text(
                text = attendees.ifEmpty { "1" },
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp, // Larger font for better readability
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F) // Dark gray for readability
                )
            )

            // Increase Button
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
                        color = Color(0xFFBBDEFB), // Light blue background
                        shape = RoundedCornerShape(50)
                    )
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase Attendees",
                    tint = Color(0xFF1976D2) // Blue icon color
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Additional Info or Feedback
        Text(
            text = "Możesz dodać od 1 do ${viewModel.getMaximumAttendees()} uczestników.",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = Color(0xFF616161) // Neutral gray for subtle feedback
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}



