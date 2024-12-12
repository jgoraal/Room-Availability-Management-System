package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.reservation.getFloorName
import com.example.apptemplates.presentation.main.temp.MainUiState

@Composable
fun DialogRoomReservation(
    state: MainUiState,
    viewModel: ReservationViewModel,
    navigateOnSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    if (state.selectedRoomToReserve != null) {
        val isDarkTheme = isSystemInDarkTheme()

        // Kolory tła dialogu
        val dialogBackground = if (isDarkTheme) Color(0xFF1E272C) else Color(0xFFFFF3E0)
        val dialogTitleColor = if (isDarkTheme) Color(0xFFECF0F1) else Color(0xFF3E2723)
        val dialogTextColor = if (isDarkTheme) Color(0xFFBDC3C7) else Color(0xFF5D4037)

        // Gradient przycisku potwierdzenia
        val confirmButtonGradient = if (isDarkTheme) {
            Brush.horizontalGradient(
                colors = listOf(Color(0xFF00695C), Color(0xFF2E7D32))
            )
        } else {
            Brush.horizontalGradient(
                colors = listOf(Color(0xFFFFA726), Color(0xFFFFC107))
            )
        }

        // Kolory ikonek
        val iconColor = if (isDarkTheme) Color(0xFF81D4FA) else Color(0xFFFF7043)

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Potwierdzenie rezerwacji",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold, color = dialogTitleColor
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
                        text = "Czy na pewno chcesz zarezerwować tę salę?",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold, color = dialogTextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RoomInfoSection(state, iconColor)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.confirmReservation(navigateOnSuccess)
                        onDismiss()
                    }, shape = RoundedCornerShape(8.dp), // Zmniejszony zaokrąglenie
                    modifier = Modifier
                        .height(40.dp) // Mniejsza wysokość
                        .width(120.dp) // Mniejsza szerokość
                        .background(
                            confirmButtonGradient, RoundedCornerShape(8.dp)
                        ), // Zmniejszony rozmiar gradientu
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "Potwierdź", fontWeight = FontWeight.Bold, color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Anuluj", color = if (isDarkTheme) Color(0xFF81A1C1) else Color.Gray
                    )
                }
            },

            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .background(dialogBackground, RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun RoomInfoSection(state: MainUiState, iconColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Home, contentDescription = null, tint = iconColor
                )

                Text(
                    text = state.selectedRoomToReserve?.name?.adjustRoomName() ?: "Nieznana sala",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1
                )

                Text(
                    text = state.selectedRoomToReserve?.floor.getFloorName(),
                    style = MaterialTheme.typography.titleMedium.copy( // Zwiększony styl
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}


/*
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

private fun Int?.getFloorName(): String {
    return when (this) {
        1 -> "Parter"
        else -> "Piętro ${this?.minus(1)}"
    }
}*/

