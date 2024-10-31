package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.temp.MainUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberOfAttendeesView(
    viewModel: ReservationViewModel,
    state: MainUiState,
    modifier: Modifier = Modifier
) {

    var attendees by remember { mutableStateOf(state.selectedAttendees.toString()) }

    val focusManager = LocalFocusManager.current


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
            .background(
                color = Color(0xFFF4F2FF),
                shape = RoundedCornerShape(16.dp)
            )
            .border(2.dp, Color(0xFF9B5DE5), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = "Number of Attendees", style = MaterialTheme.typography.bodyMedium)

        // Attendees Input Field
        TextField(
            value = attendees,
            onValueChange = {
                val number = it.toIntOrNull()

                if (number == null) {
                    attendees = ""
                } else if (number in 1..100) { // Restrict between 1 and 100
                    attendees = number.toString()
                    viewModel.changeAttendees(number)
                }
            },
            label = { Text("Enter Attendees") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFD4BFFD),
                focusedIndicatorColor = Color(0xFF9B5DE5)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Optional buttons to increase or decrease the number of attendees
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {

                val number = attendees.toIntOrNull()

                if (number != null && number > 1) {
                    focusManager.clearFocus()
                    attendees = (number - 1).toString()
                    viewModel.changeAttendees(number - 1)
                }


            }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease Attendees")
            }

            Text(
                text = attendees.ifEmpty { "1" },
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF4A4E69)
            )

            IconButton(onClick = {

                val number = attendees.toIntOrNull()

                if (number != null && number < 100) {
                    focusManager.clearFocus()
                    attendees = (number + 1).toString()
                    viewModel.changeAttendees(number + 1)
                }


            }) {
                Icon(Icons.Default.Add, contentDescription = "Increase Attendees")
            }
        }
    }
}
