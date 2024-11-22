package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.presentation.main.temp.ReservationError
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerView(
    viewModel: ReservationViewModel,
    state: MainUiState,
    label: String,
    onTimeSelected: () -> Unit = {},
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
        LabelWithIcon(text = label)


        // Show error message if there is one
        if (state.reservationError != null) {
            ErrorText(state.reservationError.showMessage())
        }


        TimePicker(
            state = timePickerState,
            modifier = Modifier.scale(0.9f)
        )


        if (label.contains("Wybierz godzinę zakończenia")) {
            val error = checkEndTimeError(viewModel, state, timePickerState)

            if (error != null) {
                viewModel.errorState(error) // Set error state in ViewModel
            } else {
                viewModel.changeEndTime(timePickerState)
                viewModel.errorState(null) // Clear error state in ViewModel
                onTimeSelected()
            }

        } else {
            viewModel.changeTime(timePickerState)
        }

    }
}


fun ReservationError.showMessage(): String {
    return when (this) {
        is ReservationError.TimeConflict -> message
        is ReservationError.AttendeesConflict -> message
        is ReservationError.RecurrenceConflict -> message
    }
}


// Helper function to check for end-time specific errors
@OptIn(ExperimentalMaterial3Api::class)
private fun checkEndTimeError(
    viewModel: ReservationViewModel,
    state: MainUiState,
    timePickerState: TimePickerState
): ReservationError? {
    val selectedTime = state.selectedTime
        ?: return ReservationError.TimeConflict("Nie wybrano godziny rozpoczęcia")

    return when {
        selectedTime.hour > timePickerState.hour -> ReservationError.TimeConflict("Niepoprawna godzina zakończenia")
        selectedTime.hour == timePickerState.hour && selectedTime.minute == timePickerState.minute -> ReservationError.TimeConflict(
            "Niepoprawna godzina zakończenia"
        )

        !isMinimallyCorrectTime(
            viewModel,
            selectedTime,
            timePickerState
        ) -> ReservationError.TimeConflict("Niepoprawna godzina zakończenia")

        else -> null // No errors
    }
}

// Checks if selected time is within the allowable range
@OptIn(ExperimentalMaterial3Api::class)
fun isMinimallyCorrectTime(
    viewModel: ReservationViewModel,
    selectedTime: LocalTime,
    timePickerState: TimePickerState
): Boolean {
    if (selectedTime.hour < 8 || selectedTime.hour > 21) return false
    if (selectedTime.hour == 21 && selectedTime.minute > 30) return false

    val minutesBetween = selectedTime.until(
        LocalTime.of(timePickerState.hour, timePickerState.minute),
        ChronoUnit.MINUTES
    )

    return viewModel.checkMaxReservationTime(minutesBetween)
}

@Composable
fun ErrorText(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(color = Color(0xFFFFCDD2), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error Icon",
            tint = Color(0xFFD32F2F),
            modifier = Modifier
                .size(20.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = message,
            color = Color(0xFFD32F2F),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LabelWithIcon(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color(0xFF9B5DE5).copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = Color(0xFF9B5DE5),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color(0xFF9B5DE5),
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}