package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.reservation.getNameInPolish
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DialogRoomReservation(
    state: MainUiState,
    viewModel: ReservationViewModel,
    navigateOnSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    if (state.selectedRoomToReserve != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "Anuluj")
                    }
                    Button(
                        onClick = {
                            viewModel.confirmReservation(navigateOnSuccess)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Potwierdź")
                    }
                }
            },
            dismissButton = {},
            title = {
                Text(
                    text = "Potwierdzenie rezerwacji",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Czy na pewno chcesz zarezerwować ten pokój?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    RoomInfoSection(state)
                    Spacer(modifier = Modifier.height(12.dp))
                    ReservationDetailsSection(state)
                }
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun RoomInfoSection(state: MainUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Pokój: ${state.selectedRoomToReserve?.name ?: "Nieznany"}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Piętro: ${state.selectedRoomToReserve?.floor ?: "Brak"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Udogodnienia: ${state.selectedRoomToReserve?.equipment?.joinToString(", ") { it.type.getNameInPolish() } ?: "Brak"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ReservationDetailsSection(state: MainUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReservationDetailRow(
            Icons.Default.CalendarToday,
            "Data",
            state.selectedDate?.formatDate() ?: "Brak"
        )
        ReservationDetailRow(
            Icons.Default.Schedule,
            "Godzina rozpoczęcia",
            state.selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Brak"
        )
        ReservationDetailRow(
            Icons.Default.Schedule,
            "Godzina zakończenia",
            state.selectedEndTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "Brak"
        )
        ReservationDetailRow(Icons.Default.People, "Uczestnicy", state.selectedAttendees.toString())
        ReservationDetailRow(
            Icons.Default.EventRepeat,
            "Cykliczność",
            if (state.isRecurring) "Tak" else "Nie"
        )
    }
}

@Composable
fun ReservationDetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


private fun LocalDate.formatDate(): String {
    val formattedDate = this.format(
        DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy")
            .withLocale(java.util.Locale.forLanguageTag("pl"))

    )

    return formattedDate.replaceFirstChar { it.uppercase() }
}




