package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apptemplates.presentation.main.reservation.ReservationState
import com.example.apptemplates.presentation.main.reservation.getNameInPolish

@Composable
fun AvailableRoomsList(state: ReservationState) {
    val roomCardHeight = 320.dp  // Approximate height of each room card
    val availableRoomCount = state.availableRooms.size
    val totalHeight = if (availableRoomCount > 0) {
        (roomCardHeight * availableRoomCount).coerceAtMost(500.dp) // Restrict max height
    } else {
        roomCardHeight // Minimum height when no rooms are available
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(totalHeight) // Set calculated height
    ) {
        Text(
            text = "Dostępne pokoje",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        if (state.availableRooms.isEmpty()) {
            NoAvailableRoomsMessage()
        } else {
            // Using LazyColumn with a constrained height
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(totalHeight)
                    .padding(bottom = 16.dp)
            ) {
                items(state.availableRooms) { room ->
                    EnhancedRoomCard(
                        roomName = room.name,
                        capacity = room.capacity,
                        facilities = room.equipment.map { it.type.getNameInPolish() },
                        floor = room.floor
                    )
                }
            }
        }
    }
}


@Composable
fun NoAvailableRoomsMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "No Rooms Available",
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Brak dostępnych pokoi. Spróbuj zmienić filtry lub datę.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}


@Composable
fun EnhancedRoomCard(roomName: String, capacity: Int, facilities: List<String>, floor: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp) // Fixed height for all cards
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDFFFD6) // Base color for the card
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Gradient overlay to blend into the card background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFDFFFD6).copy(alpha = 0.5f),
                                Color(0xFFE8F5E9).copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(2f)
                ) {
                    // Room Name and Floor Information
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFF4CAF50), shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Room Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = roomName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color(0xFF1B5E20),
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1
                            )
                        }

                        // Floor Information with an icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stairs, // Using an icon to represent the floor
                                contentDescription = "Floor Icon",
                                tint = Color(0xFF43A047),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Piętro: $floor",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF1B5E20)
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }

                    // Capacity
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF66BB6A), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Capacity Icon",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$capacity Osób",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF43A047))
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtle Divider
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0xFFC8E6C9)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // Facilities Title
                    Text(
                        text = "Udogodnienia",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.align(Alignment.End)
                    )

                    // Facilities List
                    facilities.forEach { facility ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Facility Icon",
                                tint = Color(0xFF66BB6A),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = facility,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF2E7D32)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun s(modifier: Modifier = Modifier) {
    EnhancedRoomCard("Pokoj 2", 10, listOf("Projektor", "Komputer"), 1)
}









