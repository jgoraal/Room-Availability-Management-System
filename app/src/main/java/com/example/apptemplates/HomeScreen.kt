package com.example.apptemplates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(padding: PaddingValues) {
    // Professional Gradient: Deep Indigo to Light Lavender
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFBBDEFB), Color(0xFFCE93D8))
    )

    // HomeScreen layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(padding)
            .verticalScroll(rememberScrollState()),
    ) {
        // Clean, readable header
        Text(
            text = "Room Occupancy Overview",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Minimalist, focused Room Availability Summary with space and depth
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            RoomStatusCard("Available", 10, Color(0xFF4CAF50)) // Green for available
            RoomStatusCard("Occupied", 5, Color(0xFFFF5722)) // Orange for occupied
            RoomStatusCard("Reserved", 3, Color(0xFFFFC107)) // Yellow for reserved
        }

        // Search Bar with appropriate text contrast
        OutlinedTextField(
            value = "",
            onValueChange = { /* Handle search input */ },
            label = { Text("Search for a room", color = Color.White) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = Color.White
            )
        )

        // Subtle, shadowed section for Upcoming Bookings
        Text(
            text = "Upcoming Bookings",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            items(listOf("Conference Room A - 10:00 AM", "Meeting Room B - 1:00 PM")) { booking ->
                BookingCard(booking)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons with proper layout and emphasis
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ActionButton("Available Rooms", Icons.Default.Lock)
            ActionButton("Reserve a Room", Icons.Default.Add)
        }


        ActionButton("Available Rooms", Icons.Default.Lock)
        ActionButton("Available Rooms", Icons.Default.Lock)
        ActionButton("Available Rooms", Icons.Default.Lock)
        ActionButton("Available Rooms", Icons.Default.Lock)
        ActionButton("Available Rooms", Icons.Default.Lock)


    }
}

// Supporting Composables for Status and Bookings with subtle shadows
@Composable
fun RoomStatusCard(status: String, count: Int, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = status, color = Color.White, fontSize = 16.sp)
            Text(
                text = "$count",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BookingCard(booking: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Text(
            text = booking,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF333333),
            fontSize = 16.sp
        )
    }
}

@Composable
fun ActionButton(label: String, icon: ImageVector) {
    Button(
        onClick = { /* Handle button click */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
        modifier = Modifier
            .fillMaxWidth(0.45f)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Color.White)
    }
}




