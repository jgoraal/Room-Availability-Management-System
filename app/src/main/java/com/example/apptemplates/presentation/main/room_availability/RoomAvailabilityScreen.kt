package com.example.apptemplates.presentation.main.room_availability

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun RoomAvailabilityScreen(
    viewModel: RoomAvailabilityViewModel = RoomAvailabilityViewModel(),
    navController: NavController
) {

    val state by viewModel.state.collectAsState()


    /*Scaffold(
        topBar = { TopBarPreview() },
        bottomBar = { BottomBar(navController) },
        content = { padding ->
            RoomAvailabilityView(
                state = state,
                viewModel = viewModel,
                modifier = Modifier.padding(padding)
            )
        }
    )*/

}

@Composable
fun RoomAvailabilityView(
    state: MainUiState,
    viewModel: RoomAvailabilityViewModel,
    modifier: Modifier
) {


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Existing items like calendar, time picker, etc.
        item {
            Text(
                text = "Sprawdź dostępność sal",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            FloorSelector(
                selectedFloor = state.selectedFloorName,
                onFloorChange = { floor ->
                    viewModel.fetchReservations(
                        floor,
                        state.selectedRoomNumber,
                        state.selectedDateCheck
                    )
                }
            )
        }
        item {
            RoomSelector(
                selectedRoom = state.selectedRoomNumber,
                onRoomChange = { room ->
                    viewModel.fetchReservations(
                        state.selectedFloorName,
                        room,
                        state.selectedDateCheck
                    )
                }
            )
        }
        item {
            DatePicker(
                selectedDate = state.selectedDateCheck,
                onDateChange = { date ->
                    viewModel.fetchReservations(
                        state.selectedFloorName,
                        state.selectedRoomNumber,
                        date
                    )
                }
            )
        }
        item {
            Timeline(
                reservations = state.reservations
            )
        }
    }


}


@Composable
fun RoomSelector(
    selectedRoom: String,
    onRoomChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text("Select Room", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { expanded = true }) {
            Text(text = selectedRoom)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Room A", "Room B", "Room C").forEach { room ->
                DropdownMenuItem(
                    onClick = {
                        onRoomChange(room)
                        expanded = false
                    },
                    text = { Text(text = room) }
                )
            }
        }
    }
}


@Composable
fun FloorSelector(
    selectedFloor: String,
    onFloorChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text("Select Floor", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { expanded = true }) {
            Text(text = selectedFloor)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Parter", "2", "3").forEach { floor ->
                DropdownMenuItem(
                    onClick = {
                        onFloorChange(floor)
                        expanded = false
                    },
                    text = { Text(text = floor) }
                )
            }
        }
    }
}


@Composable
fun DatePicker(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    Column {
        Text("Select Date")
        // Use an actual date picker here
        Button(onClick = { /* Show date picker dialog */ }) {
            Text(text = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        }
    }
}

@Composable
fun Timeline(
    reservations: List<Reservation>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(500.dp)
            .padding(16.dp)
    ) {
        Text("Timeline", style = MaterialTheme.typography.headlineLarge)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reservations) { reservation ->
                ReservationCard(reservation)
            }
        }
    }
}

fun formatTimeFromLong(timestamp: Long): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())  // Adjust to system's default timezone
        .format(formatter)
}

@Composable
fun ReservationCard(reservation: Reservation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = reservation.id,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "${formatTimeFromLong(reservation.startTime)} - ${
                    formatTimeFromLong(
                        reservation.endTime
                    )
                }",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
            )
        }
    }
}


