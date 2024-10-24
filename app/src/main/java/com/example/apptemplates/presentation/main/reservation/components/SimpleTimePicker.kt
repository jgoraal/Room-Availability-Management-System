package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerView(
    viewModel: ReservationViewModel,
    label: String,
    modifier: Modifier = Modifier
) {

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
            .background(color = Color(0xFFF4F2FF), shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color(0xFF9B5DE5), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)

        TimePicker(
            state = timePickerState,
            modifier = Modifier.scale(0.9f)
        )




        if (label.contains("Wybierz godzinę zakończenia")) {
            viewModel.changeEndTime(timePickerState)
        } else {
            viewModel.changeTime(timePickerState)
        }

    }
}

