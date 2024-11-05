package com.example.apptemplates.presentation.main.room_availability

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.presentation.main.reservation.generator.generateRandomReservations
import com.example.apptemplates.presentation.main.reservation.generator.generateRandomRoomIds
import com.example.apptemplates.presentation.main.reservation.generator.generateRealisticLessonsForRooms
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun RoomAvailabilityView(
    state: MainUiState,
    viewModel: RoomAvailabilityViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header Title
        Text(
            text = "Check Room Availability",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Floor Selector
        FloorSelector(
            selectedFloor = state.selectedFloorName,
            onFloorChange = { floor ->
                viewModel.fetchReservations(floor, state.selectedRoomNumber, state.selectedDateCheck)
            }
        )

        // Room Selector
        RoomSelector(
            selectedRoom = state.selectedRoomNumber,
            onRoomChange = { room ->
                viewModel.fetchReservations(state.selectedFloorName, room, state.selectedDateCheck)
            }
        )

        // Date Picker
        DatePicker(
            selectedDate = state.selectedDateCheck,
            onDateChange = { date ->
                viewModel.fetchReservations(state.selectedFloorName, state.selectedRoomNumber, date)
            }
        )

        // Timeline of Room Reservations
        TimelineSection(reservations = reservations)
    }
}

@Composable
fun FloorSelector(
    selectedFloor: String,
    onFloorChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SelectorField(
        label = "Select Floor",
        selectedOption = selectedFloor,
        options = listOf("Ground Floor", "1st Floor", "2nd Floor", "3rd Floor"),
        onOptionSelected = onFloorChange
    )
}

@Composable
fun RoomSelector(
    selectedRoom: String,
    onRoomChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SelectorField(
        label = "Select Room",
        selectedOption = selectedRoom,
        options = listOf("Room A", "Room B", "Room C"),
        onOptionSelected = onRoomChange
    )
}

@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
) {
    Column {
        Text("Select Date", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Button(
            onClick = { /* Show date picker dialog */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun SelectorField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(text = selectedOption, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}

@Composable
fun TimelineSection(reservations: List<Reservation>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8EAF6), Color(0xFF9FA8DA))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Text(
            text = "Room Schedule",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A237E)
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(reservations) { reservation ->
                ReservationSlotCard(reservation) 
            }
        }
    }
}

@Composable
fun ReservationSlotCard(reservation: Reservation) {
    val isReserved = listOf(true, false).random() // Replace with your actual logic to check if reserved

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (isReserved) Color(0xFFFFCDD2) else Color(0xFFC8E6C9)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon
            Icon(
                imageVector = if (isReserved) Icons.Default.EventBusy else Icons.Default.EventAvailable,
                contentDescription = if (isReserved) "Reserved" else "Available",
                tint = if (isReserved) Color(0xFFD32F2F) else Color(0xFF388E3C),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = if (isReserved) "Reserved" else "Available",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = if (isReserved) Color(0xFFD32F2F) else Color(0xFF388E3C)
                    )
                )
                Text(
                    text = "${formatTimeFromLong(reservation.startTime)} - ${formatTimeFromLong(reservation.endTime)}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }
    }
}



fun formatTimeFromLong(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}



@Preview(showBackground = true)
@Composable
private fun avail() {
    RoomAvailabilityView(MainUiState(), RoomAvailabilityViewModel())
}


val roomIds = generateRandomRoomIds(10)
val lessons = generateRealisticLessonsForRooms(roomIds)
val reservations = generateRandomReservations(roomIds, lessons).filter { r ->
    r.dayOfWeek == LocalDate.now().dayOfWeek && r.status != ReservationStatus.CANCELED && r.roomId == roomIds.first()
}.sortedBy { it.startTime }