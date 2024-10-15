package com.example.apptemplates.presentation.temp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Sample Reservation Data Class
data class Reservation(
    val id: String,
    val roomId: String,
    val startTime: Date,
    val endTime: Date,
    val status: ReservationStatus
)

enum class ReservationStatus {
    RESERVED, AVAILABLE, OCCUPIED
}

// Sample RoomSchedule Data Class
data class RoomSchedule(
    val roomId: String,
    val roomName: String,
    val reservations: List<Reservation>
)

@Preview(showBackground = true)
@Composable
fun RoomSchedulerPreview() {
    val sampleReservations = listOf(
        Reservation("1", "Room A", Date(), Date(System.currentTimeMillis() + 3600000), ReservationStatus.RESERVED),
        Reservation("2", "Room A", Date(System.currentTimeMillis() + 7200000), Date(System.currentTimeMillis() + 10800000), ReservationStatus.AVAILABLE)
    )

    val roomSchedules = listOf(
        RoomSchedule("1", "Conference Room A", sampleReservations),
        RoomSchedule("2", "Meeting Room B", sampleReservations)
    )

    RoomSchedulerView(roomSchedules = roomSchedules)
}

@Composable
fun RoomSchedulerView(roomSchedules: List<RoomSchedule>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(roomSchedules) { roomSchedule ->
            RoomScheduleRow(roomSchedule)
        }
    }
}

@Composable
fun RoomScheduleRow(roomSchedule: RoomSchedule) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = roomSchedule.roomName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(roomSchedule.reservations) { reservation ->
                ReservationCard(reservation)
            }
        }
    }
}

@Composable
fun ReservationCard(reservation: Reservation) {
    val backgroundColor = when (reservation.status) {
        ReservationStatus.RESERVED -> Color.Red
        ReservationStatus.AVAILABLE -> Color.Green
        ReservationStatus.OCCUPIED -> Color.Yellow
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .width(120.dp)
            .height(60.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${reservation.startTime.formatTime()} - ${reservation.endTime.formatTime()}",
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

fun Date.formatTime(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(this)
}