package com.example.apptemplates.presentation.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.data.room.Equipment
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.presentation.main.home.ActiveRooms
import com.example.apptemplates.presentation.main.home.HomeViewModel
import com.example.apptemplates.presentation.main.home.getUpcomingDate
import com.example.apptemplates.presentation.main.reservation.components.adjustRoomName
import com.example.apptemplates.presentation.main.reservation.getFloorName
import com.example.apptemplates.presentation.main.reservation.getNameInPolish
import com.example.apptemplates.presentation.main.temp.DarkThemeReservationColors
import com.example.apptemplates.presentation.main.temp.LightThemeReservationColors
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReservationDetailsDialog(
    state: MainUiState,
    viewModel: HomeViewModel,
    onDismiss: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = if (isDarkTheme) DarkThemeReservationColors else LightThemeReservationColors

    val showConfirmReservationCancel = remember { mutableStateOf(false) }
    val showRequestAdditionalEquipment = remember { mutableStateOf(false) }

    if (state.showReservationDetailsDialog && state.selectedReservation != null) {
        val reservation = state.selectedReservation

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Szczegóły Rezerwacji",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = colors.primaryText
                    )
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = colors.cardBackground,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    // 1. Sekcja Sala
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colors.accentColor.copy(alpha = 0.15f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MeetingRoom,
                                contentDescription = "Sala",
                                tint = colors.iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = reservation.roomId.getRoomName(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primaryText
                                )
                            )
                        }
                    }

                    // 2. Sekcja Data i Godzina
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colors.backgroundGradient.last().copy(alpha = 0.15f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            // Data
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = "Data",
                                    tint = colors.iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = reservation.getDate(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = colors.primaryText
                                    )
                                )
                            }

                            // Godzina
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Godzina",
                                    tint = colors.iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = (reservation.startTime to reservation.endTime).getPolishedTime(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = colors.primaryText
                                    )
                                )
                            }
                        }
                    }

                    // 3. Sekcja Kontakt (zamiast Wyposażenia)
                    // Załóżmy, że mamy dostęp do danych kontaktowych np. reservation.contactName, reservation.contactEmail
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colors.accentColor.copy(alpha = 0.1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = "Kontakt",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primaryText
                                )
                            )

                            // Ikona i e-mail
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "E-mail",
                                    tint = colors.iconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = reservation.roomId.getContactEmail(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = colors.primaryText
                                    ),
                                    maxLines = 1, // Ogranicza tekst do jednej linii
                                    overflow = TextOverflow.Ellipsis, // Dodaje "..." na końcu, jeśli tekst jest za długi
                                    modifier = Modifier.horizontalScroll(rememberScrollState()) // Włącza przewijanie w poziomie
                                )
                            }
                        }
                    }


                    // 4. Status Rezerwacji
                    val statusColor = when (reservation.status) {
                        ReservationStatus.CONFIRMED -> Color(0xFF4CAF50)
                        ReservationStatus.PENDING -> Color(0xFFFFC107)
                        else -> Color(0xFFF44336)
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = statusColor.copy(alpha = 0.1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Status",
                                tint = statusColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = reservation.status.getPolishedStatus(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            )
                        }
                    }

                    // 5. Przyciski akcji
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        // Przycisk Anulowania Rezerwacji
                        Button(
                            onClick = {
                                showConfirmReservationCancel.value = true
                                showRequestAdditionalEquipment.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.errorColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Anuluj",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Anuluj Rezerwację", style = MaterialTheme.typography.bodyMedium)
                        }

                        // Przycisk dodatkowego wyposażenia (jeśli brak w sali)
                        /*if (!reservation.roomId.hasAdditionalEquipment()) {
                            Button(
                                onClick = {
                                    showConfirmReservationCancel.value = false
                                    showRequestAdditionalEquipment.value = true
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.buttonColor,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = "Wyposażenie",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Poproś o Wyposażenie", style = MaterialTheme.typography.bodyMedium)
                            }
                        }*/
                    }
                }
            },
            confirmButton = {},
            dismissButton = {},
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        // Dodatkowe dialogi (anulowanie, wyposażenie)
        if (showConfirmReservationCancel.value && !showRequestAdditionalEquipment.value) {
            CancelReservationDialog(
                onConfirm = {
                    viewModel.cancelReservation()
                    showConfirmReservationCancel.value = false
                    onDismiss()
                },
                onDismissRequest = {
                    showConfirmReservationCancel.value = false
                }
            )
        }

        /*if (showRequestAdditionalEquipment.value && !showConfirmReservationCancel.value) {
            RequestAdditionalEquipmentDialog(
                state,
                onConfirm = {
                    viewModel.requestAdditionalEquipment()
                    showRequestAdditionalEquipment.value = false
                    onDismiss()
                },
                onDismissRequest = {
                    showRequestAdditionalEquipment.value = false
                }
            )
        }*/
    }
}



@Composable
fun RoomInfoSection(state: MainUiState) {
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
                Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.primary)

                Text(
                    text = state.selectedRoomToReserve?.name?.adjustRoomName() ?: "Nieznana sala",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1
                )

                Text(
                    text = state.selectedRoomToReserve?.floor.getFloorName(),
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Liczba miejsc: ${state.selectedRoomToReserve?.capacity ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}



@Composable
private fun CancelReservationDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = "Potwierdzenie Anulowania",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E) // Rich brown for the title
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Czy na pewno chcesz anulować rezerwację?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF6D4C41), // Warm brown for text
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = "Ten krok nie może zostać cofnięty.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFD32F2F), // Red to emphasize irreversible action
                        fontSize = 14.sp
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336), // Red for danger
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(text = "Anuluj Rezerwację")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Cofnij",
                    color = Color(0xFF6D4C41), // Warm brown for dismiss
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)) // Rounded corners for dialog
    )
}


@Composable
private fun RequestAdditionalEquipmentDialog(
    state: MainUiState,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = "Poproś o Dodatkowe Wyposażenie",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E) // Rich brown for the title
                )
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Wybierz dodatkowe wyposażenie, które chciałbyś zarezerwować.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF6D4C41), // Warm brown for text
                        fontSize = 16.sp
                    )
                )
                // Example of a selectable list for equipment
                val equipmentOptions = state.getEquipmentOptions()
                val selectedOptions = remember { mutableStateListOf<EquipmentType>() }

                equipmentOptions.forEach { equipment ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (state.selectedAdditionalEquipment.contains(equipment)) {
                                    state.selectedAdditionalEquipment.remove(equipment)
                                } else {
                                    state.selectedAdditionalEquipment.add(equipment)
                                }
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedOptions.contains(equipment),
                            onCheckedChange = {
                                if (selectedOptions.contains(equipment)) {
                                    selectedOptions.remove(equipment)
                                } else {
                                    selectedOptions.add(equipment)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF6D4C41),
                                uncheckedColor = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = equipment.getNameInPolish(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF5D4037)
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D4C41), // Rich brown for confirmation
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(text = "Zatwierdź Wyposażenie")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Anuluj",
                    color = Color(0xFF6D4C41), // Warm brown for dismiss
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)) // Rounded corners for dialog
    )
}


@Preview(showBackground = true)
@Composable
private fun AsdPreview() {
    // Provide a mock reservation and a mock view model
    val mockReservation = Reservation(
        id = "123",
        roomId = "123",
        startTime = System.currentTimeMillis() / 1000, // Mock timestamp
        endTime = System.currentTimeMillis() / 1000 + 3600, // Mock timestamp + 1 hour
        status = ReservationStatus.PENDING // Mock status
    )

    val mockRoom = listOf(
        Room(
            id = "123",
            name = "Sala 101",
            equipment = listOf(
                Equipment(EquipmentType.COMPUTER),
                Equipment(EquipmentType.PROJECTOR),
                Equipment(EquipmentType.WHITEBOARD)
            )
        )
    )

    ActiveRooms.setRooms(mockRoom)

    // Wrapping with a MaterialTheme to ensure the preview works correctly
    MaterialTheme {
        Asd(
            reservation = mockReservation,
            onDismiss = {}
        )
    }
}

@Composable
private fun Asd(
    reservation: Reservation,
    onDismiss: () -> Unit
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Szczegóły Rezerwacji",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF4E342E) // Rich brown for the title
                    )
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        //.padding(8.dp)
                        .background(
                            color = Color(0xFFFFFBF2), // Warm soft background
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(8.dp)
                ) {
                    // Room Name with Icon and Modern Styling
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .background(
                                color = Color(0xFFFDE9E0), // Subtle beige background
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = "Sala",
                            tint = Color(0xFF6D4C41), // Warm brown for the icon
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = reservation.id.getRoomName(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4E342E) // Dark brown for room name
                            )
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp), // Add spacing between items
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFECB3), // Soft pastel yellow for the unified container
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            ) // Padding for better content alignment
                    ) {
                        // Reservation Date
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Event, // Calendar icon for date
                                contentDescription = "Data",
                                tint = Color(0xFF5D4037), // Warm brown for the icon
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = reservation.getDate(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D4037) // Warm brown for text
                                )
                            )
                        }

                        // Reservation Time
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule, // Clock icon for time
                                contentDescription = "Godzina",
                                tint = Color(0xFF5D4037), // Warm brown for the icon
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = (reservation.startTime to reservation.endTime).getPolishedTime(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D4037) // Warm brown for text
                                )
                            )
                        }
                    }


                    // Equipment Details Section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFF3F9D2), // Soft pastel green for a gentle background
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        // Section Title
                        Text(
                            text = "Wyposażenie",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D4037) // Warm brown for title
                            ),
                            modifier = Modifier.padding(bottom = 8.dp) // Space below title
                        )

                        // Equipment List
                        val equipmentList = reservation.id.getEquipment()
                        if (equipmentList.isEmpty()) {
                            // Display message if no equipment is available
                            Text(
                                text = "Brak dodatkowego wyposażenia",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    color = Color(0xFF757575) // Muted gray for a subtle look
                                )
                            )
                        } else {
                            // Display each equipment with its icon and name
                            equipmentList.forEach { equipment ->
                                val (name, icon) = equipment.getPairComponents()
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = name,
                                        tint = Color(0xFF5D4037), // Warm brown for the icon
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 14.sp,
                                            color = Color(0xFF5D4037) // Warm brown for text
                                        )
                                    )
                                }
                            }
                        }
                    }


                    // Reservation Status Section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = when (reservation.status) {
                                    ReservationStatus.CONFIRMED -> Color(0xFFE8F5E9) // Light green for confirmed
                                    ReservationStatus.PENDING -> Color(0xFFFFF3E0) // Light yellow for pending
                                    else -> Color(0xFFFFEBEE) // Light red for unconfirmed
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Status",
                            tint = when (reservation.status) {
                                ReservationStatus.CONFIRMED -> Color(0xFF4CAF50) // Green for confirmed
                                ReservationStatus.PENDING -> Color(0xFFFFC107) // Yellow for pending
                                else -> Color(0xFFF44336) // Red for unconfirmed
                            },
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = reservation.status.getPolishedStatus(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (reservation.status) {
                                    ReservationStatus.CONFIRMED -> Color(0xFF4CAF50) // Green for confirmed
                                    ReservationStatus.PENDING -> Color(0xFFFFC107) // Yellow for pending
                                    else -> Color(0xFFF44336) // Red for unconfirmed
                                }
                            )
                        )
                    }

                    // Action Buttons Section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp), // Space between buttons
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp) // Padding for spacing around buttons
                    ) {
                        // Cancel Reservation Button
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336), // Vibrant red for cancel
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)) // Rounded corners for a modern look
                                .padding(horizontal = 16.dp) // Horizontal padding to align with content
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth() // Ensures alignment within button
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancel Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Anuluj Rezerwację",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center // Center text alignment
                                    )
                                )
                            }
                        }

                        // Request Additional Equipment Button
                        Button(
                            onClick = { /* Handle request additional equipment */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6D4C41), // Rich brown for harmony
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)) // Rounded corners for consistency
                                .padding(horizontal = 16.dp) // Horizontal padding to align with content
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth() // Ensures alignment within button
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Build, // Replace with appropriate icon for equipment
                                    contentDescription = "Equipment Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Poproś o Wyposażenie",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center // Center text alignment
                                    )
                                )
                            }
                        }
                    }


                }


            },
            confirmButton = {
                /* Column {
                     Button(
                         onClick = {
                             // Mock cancel reservation action
                             onDismiss()
                         },
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(vertical = 4.dp),
                         colors = ButtonDefaults.buttonColors(
                             containerColor = Color(0xFFF44336), // Red for cancel
                             contentColor = Color.White
                         )
                     ) {
                         Text(text = "Anuluj Rezerwację")
                     }

                     Button(
                         onClick = {
                             // Mock request additional equipment action
                             onDismiss()
                         },
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(vertical = 4.dp),
                         colors = ButtonDefaults.buttonColors(
                             containerColor = Color(0xFF4E342E), // Rich brown for harmony
                             contentColor = Color.White
                         )
                     ) {
                         Text(text = "Poproś o Wyposażenie")
                     }
                 }*/
            },
            dismissButton = {
                /*TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = "Zamknij",
                        color = Color(0xFF8D6E63) // Subtle muted brown
                    )
                }*/
            },
            modifier = Modifier
                .padding(2.dp)
                .clip(RoundedCornerShape(16.dp)) // Rounded corners for the dialog
        )


    }
}


private fun String.getRoomName(): String {
    return ActiveRooms.getRooms().value.find { room -> room.id == this }?.name?.getPolishedName()
        ?: ""
}

private fun String.getPolishedName(): String {
    return "Sala " + this.split(" ").last()
}

private fun Reservation.getDate(): String {

    return when (this.isRecurring) {
        true -> this.recurrencePattern.getUpcomingDate(this.startTime)
        false -> this.startTime.getNonRecurringDate()
    }

}

private fun Long.getNonRecurringDate(): String {
    val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

    // Define custom day abbreviations
    val dayAbbreviations = mapOf(
        DayOfWeek.MONDAY to "Pon",
        DayOfWeek.TUESDAY to "Wto",
        DayOfWeek.WEDNESDAY to "Śro",
        DayOfWeek.THURSDAY to "Czw",
        DayOfWeek.FRIDAY to "Pią",
        DayOfWeek.SATURDAY to "Sob",
        DayOfWeek.SUNDAY to "Nie"
    )

    val dayAbbreviation = dayAbbreviations[localDateTime.dayOfWeek] ?: ""

    // Format date with custom day abbreviation
    val formattedDate = localDateTime.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("pl"))
    )

    return "$dayAbbreviation, $formattedDate"
}

private fun ReservationStatus.getPolishedStatus(): String {
    return when (this) {
        ReservationStatus.CONFIRMED -> "Potwierdzona"
        ReservationStatus.PENDING -> "Oczekująca"
        ReservationStatus.CANCELED -> "Anulowana"
    }
}

private fun Pair<Long, Long>.getPolishedTime(): String {
    val startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(first), ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("HH:mm"))
    val endTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(second), ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("HH:mm"))
    return "$startTime - $endTime"
}

private fun String.getEquipment(): List<EquipmentType> {
    return ActiveRooms.getRooms().value.find { it.id == this }
        ?.equipment
        ?.map { it.type }
        ?: emptyList() // Return an empty list if no equipment is found
}

private fun EquipmentType.getPairComponents(): Pair<String, ImageVector> {
    return when (this) {
        EquipmentType.COMPUTER -> "Komputery" to Icons.Default.Computer
        EquipmentType.PROJECTOR -> "Projektory" to Icons.Default.Videocam // Replace with a suitable icon
        EquipmentType.WHITEBOARD -> "Tablica" to Icons.Default.Dashboard // Replace with a suitable icon
    }
}

private fun MainUiState.getEquipmentOptions(): List<EquipmentType> {

    val eq =
        ActiveRooms.getRooms().value.find { it.id == this.selectedReservation?.roomId }?.equipment?.map { it.type }
            ?: return EquipmentType.entries.toList()

    return EquipmentType.entries.filter { it !in eq }

}


private fun String.hasAdditionalEquipment(): Boolean {
    val equipment =
        ActiveRooms.getRooms().value.find { it.id == this }?.equipment?.map { it.type }
            ?: return false// Find the room with the given ID
    return EquipmentType.entries.all { it in equipment }
        ?: false // Check if the room has any matching equipment
}


private fun String.getContactEmail(): String {
    val email = ActiveRooms.getRooms().value.find { it.id == this }?.contactEmail ?: "Brak adresu e-mail"
    return email.ifBlank { "Brak adresu e-mail" }
}