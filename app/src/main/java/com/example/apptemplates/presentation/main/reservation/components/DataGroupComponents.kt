package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RoomPreferences
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.reservation.getFloorName
import com.example.apptemplates.presentation.main.reservation.getNameInPolish
import com.example.apptemplates.presentation.main.reservation.translateRecurringFrequency
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.format.DateTimeFormatter

@Composable
fun SelectedData(viewModel: ReservationViewModel, state: MainUiState, vararg show: Boolean) {
    val isDarkTheme = isSystemInDarkTheme()
    val containerBrush = if (isDarkTheme) {
        Brush.verticalGradient(colors = listOf(Color(0xFF34495E), Color(0xFF2C3E50)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFFDEDD4), Color(0xFFFCE5D1)))
    }
    val borderColor = if (isDarkTheme) Color(0xFF1ABC9C) else Color(0xFF8D6E63)
    val iconTint = if (isDarkTheme) Color(0xFF48C9B0) else Color(0xFF6D4C41)
    val labelColor = if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
    val valueColor = if (isDarkTheme) Color(0xFFB0C4DE) else Color(0xFF6D4C41)

    if (state.showTimePicker) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(containerBrush)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Date Row (Start and End Date)
            if (state.selectedDate != null) {
                DataGroup(
                    items = listOf(
                        DataRowItem(
                            icon = Icons.Default.EventAvailable,
                            label = "Data rozpoczęcia",
                            value = state.selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        ),
                        DataRowItem(
                            icon = Icons.Default.EventBusy,
                            label = "Data zakończenia",
                            value = (state.endRecurrenceDate ?: state.selectedDate)
                                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        )
                    ),
                    iconTint = iconTint,
                    labelColor = labelColor,
                    valueColor = valueColor
                )
            }

            // Time Rows (Start and End Time)
            if ((state.selectedTime != null || state.selectedEndTime != null) && show[0]) {
                DataGroup(
                    items = listOf(
                        DataRowItem(
                            icon = Icons.Default.Schedule,
                            label = "Czas rozpoczęcia",
                            value = state.selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                                ?: ""
                        ),
                        DataRowItem(
                            icon = Icons.Default.Schedule,
                            label = "Czas zakończenia",
                            value = state.selectedEndTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                                ?: ""
                        )
                    ),
                    iconTint = iconTint,
                    labelColor = labelColor,
                    valueColor = valueColor
                )
            }

            // Recurring and Frequency Rows
            if (show[2] && state.isRecurring && viewModel.canUserMakeRecurringReservation()) {
                DataGroup(
                    items = listOf(
                        DataRowItem(
                            icon = Icons.Default.EventRepeat,
                            label = "Cykliczność",
                            value = if (state.isRecurring) "Tak" else "Nie"
                        ),
                        DataRowItem(
                            icon = Icons.Default.EventRepeat,
                            label = "Częstotliwość",
                            value = translateRecurringFrequency(
                                state.recurringFrequency ?: RecurrenceFrequency.WEEKLY
                            )
                        )
                    ),
                    iconTint = iconTint,
                    labelColor = labelColor,
                    valueColor = valueColor
                )
            }

            // Attendees Row
            if (state.selectedAttendees > 1 && show[1]) {
                DataRow(
                    icon = Icons.Default.People,
                    label = "Liczba uczestników",
                    value = state.selectedAttendees.toString(),
                    iconTint = iconTint,
                    labelColor = labelColor,
                    valueColor = valueColor
                )
            }

            // Floor and Equipment Rows
            if (show[3]) {

                if (state.selectedFloor != null) {
                    DataRow(
                        icon = Icons.Default.Business,
                        label = "Piętro",
                        value = state.selectedFloor.getFloorName(),
                        iconTint = iconTint,
                        labelColor = labelColor,
                        valueColor = valueColor
                    )
                }



                if (state.selectedEquipment.isNotEmpty()) {
                    DataRow(
                        icon = Icons.Default.RoomPreferences,
                        label = "Wyposażenie",
                        value = state.selectedEquipment.joinToString(", ") { it.getNameInPolish() }
                            .ifEmpty { "Nie wybrano" },
                        iconTint = iconTint,
                        labelColor = labelColor,
                        valueColor = valueColor
                    )
                }
            }
        }
    }


}

@Composable
fun DataGroup(
    items: List<DataRowItem>,
    iconTint: Color,
    labelColor: Color,
    valueColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = valueColor.copy(alpha = 0.05f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = "${item.label} Icon",
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = labelColor
                )
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = valueColor
                    )
                )
            }
        }
    }
}

@Composable
fun DataRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    labelColor: Color,
    valueColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = valueColor.copy(alpha = 0.05f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(color = labelColor)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(color = valueColor)
            )
        }
    }
}


data class DataRowItem(val icon: ImageVector, val label: String, val value: String)