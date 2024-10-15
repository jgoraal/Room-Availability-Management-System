package com.example.apptemplates.presentation.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.BottomBarPreview
import com.example.apptemplates.TopBarPreview
import com.example.apptemplates.form.HomeUiState
import com.example.apptemplates.form.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToReservation: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBarPreview()
        },
        bottomBar = {
            BottomBarPreview()
        },
        content = { padding ->
            HomeView(
                padding,
                uiState,
                onRefreshRooms = { viewModel.refreshRooms() },
                navigateToReservation,
                navigateToProfile,
                navigateToSettings,
                onLogout
            )
        }
    )
}

@Composable
fun HomeView(
    padding: PaddingValues,
    uiState: HomeUiState,
    onRefreshRooms: () -> Unit,
    navigateToReservation: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    // Gradient tła
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFBBDEFB), Color(0xFFCE93D8))
    )

    // Główny widok
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Witaj użytkowniku
        item {
            Text(
                text = "Witaj, ${uiState.user?.username ?: "Gość"}!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            )
        }

        // Oddzielna linia
        item {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        // Informacje o salach - sekcja nagłówka
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = Color(0xFFF1F1F1).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp) // Wewnętrzny padding, aby nadać przestrzeń tekstowi
            ) {
                Text(
                    text = "Room Occupancy Overview",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Oddzielna linia
        item {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        // Sekcja statusu sal (jeśli dane są dostępne)
        if (uiState.rooms.isNotEmpty()) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    RoomStatusCard(
                        "Available",
                        uiState.rooms.count { it.availability },
                        Color(0xFF4CAF50)
                    )
                    RoomStatusCard(
                        "Occupied",
                        uiState.rooms.count { it.availability },
                        Color(0xFFFF5722)
                    )
                    RoomStatusCard(
                        "Reserved",
                        uiState.rooms.count { it.availability },
                        Color(0xFFFFC107)
                    )
                }
            }
        } else {
            // Wyświetlanie loadera lub informacji, że brak danych
            item {
                if (uiState.screenState == ScreenState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Ładowanie danych...", color = Color.White)
                    }
                } else if (uiState.screenState is ScreenState.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nie udało się załadować danych",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Sekcja rezerwacji (jeśli dane są dostępne)
        if (uiState.reservations.isNotEmpty()) {
            item {
                Text(
                    text = "Upcoming Bookings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(uiState.reservations) { reservation ->
                BookingCard("${reservation.purpose} - ${reservation.startTime}")
            }
        }

        // Sekcja akcji
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(listOf("Rezerwacja Sali", "Dostępne Sale", "Profil", "Ustawienia")) { label ->
                    ActionButton(label, Icons.Default.Add) // Możesz zmienić ikonę na odpowiednią
                }
            }
        }
    }
}

@Composable
fun RoomStatusCard(status: String, count: Int, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .padding(8.dp)
            .width(110.dp), // Ustawienie szerokości dla spójnego wyglądu kart
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
            .width(150.dp) // Ustaw szerokość dla spójności
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Color.White)
    }
}