package com.example.apptemplates.presentation.main.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.R
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.data.user.User
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.util.UUID


/*@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    navigateToReservation: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBarPreview()
        },
        bottomBar = {
            BottomBar(navController)
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
}*/


@Composable
fun HomeView(
    padding: PaddingValues,
    uiState: MainUiState,
    onRefreshRooms: () -> Unit = {},
    navigateToReservation: () -> Unit = {},
    navigateToProfile: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2196F3), Color(0xFF9C27B0))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(padding)
        //.verticalScroll(rememberScrollState())
    ) {
        // Nagłówek z obrazkiem profilowym i powitaniem
        HeaderSection(uiState.user, navigateToProfile)

        Spacer(modifier = Modifier.height(8.dp))

        // Email verification reminder if necessary
        if (uiState.user?.isVerified == false) {
            EmailVerificationReminder()
        }

        // Divider między nagłówkiem a resztą sekcji
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.White.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(8.dp))


        DateAndReservationsSection(
            uiState = uiState,
            navigateToReservation = navigateToReservation
        )


        ReserveRoomButton(navigateToReservation)
    }
}

@Composable
fun HeaderSection(user: User?, navigateToProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Obrazek profilowy
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Zdjęcie profilowe",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f))
                .clickable { navigateToProfile() }
                .padding(8.dp), // Dodany padding do obrazu profilowego
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Tekst powitalny
        Column {
            Text(
                text = "Witaj,",
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                color = Color.White
            )
            Text(
                text = "${user?.username ?: "Nieznajomy"}!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun EmailVerificationReminder() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC107)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Prosimy o potwierdzenie adresu e-mail.",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}


@Composable
fun QuickAccessButtons(
    navigateToReservation: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSettings: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),  // Zmniejszony odstęp
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)  // Mniejsze paddingi
    ) {
        item {
            QuickAccessButton("Rezerwacja", Icons.Default.Event, navigateToReservation)
        }
        item {
            QuickAccessButton("Profil", Icons.Default.Person, navigateToProfile)
        }
        item {
            QuickAccessButton("Ustawienia", Icons.Default.Settings, navigateToSettings)
        }
    }
}

@Composable
fun QuickAccessButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
        modifier = Modifier
            .width(100.dp)  // Keep width consistent
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),  // Smooth rounded edges
        contentPadding = PaddingValues(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)  // Ensure icon remains visible and proportional
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


/*@Composable
fun ReservationsSection(reservations: List<Reservation>, navigateToReservation: () -> Unit) {
    if (reservations.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reservations) { reservation ->
                BookingCard(
                    title = "Pokój: ${reservation.id}",
                    subtitle = "Data: ${reservation.startTime}",
                    status = reservation.status.name,
                    navigateToReservation = navigateToReservation
                )
            }
        }
    } else {
        Text(
            text = "Brak nadchodzących rezerwacji.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun BookingCard(
    title: String,
    subtitle: String,
    status: String,
    navigateToReservation: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToReservation() },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.MeetingRoom,
                contentDescription = null,
                tint = Color(0xFF6200EE),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(text = subtitle, color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Status: $status",
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF333333),
                    fontSize = 12.sp
                )
            }
        }
    }
}*/

@Composable
fun ReserveRoomButton(navigateToReservation: () -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = navigateToReservation,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Zarezerwuj swój pokój", color = Color.White)
    }
    Spacer(modifier = Modifier.height(32.dp))
}


@Composable
fun DateAndReservationsSection(uiState: MainUiState, navigateToReservation: () -> Unit) {
    // Container for both the title, date row, and reservations list
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE)), // Set a nice purple background
        elevation = CardDefaults.elevatedCardElevation(8.dp)  // Elevation for depth
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title section
            Text(
                text = "Twoje nadchodzące rezerwacje",  // Change this to something more creative if needed
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Date row section
            DateSelectionRow()

            // Divider to separate dates and reservations
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 8.dp)
            )


            val res = Reservation(
                roomId = UUID.randomUUID().toString(),
                userId = uiState.user?.uid ?: "",
                status = ReservationStatus.PENDING
            )

            val list = mutableListOf<Reservation>()

            repeat((1..5).random()) {
                list.add(res)
            }

            Log.i("REZERWACJE", uiState.reservations.size.toString())
            Log.i("REZERWACJE", uiState.reservations.toString())

            // Reservations list
            ReservationsSection(list, navigateToReservation)
        }
    }
}

/*@Composable
fun DateRow() {
    val currentDate = LocalDate.now()
    val daysOfWeek = List(7) { currentDate.plusDays(it.toLong()) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(daysOfWeek.size) { index ->
            if (index == 0) {
                DateCard(dayOfWeek = daysOfWeek[index], isToday = true)
            } else {
                DateCard(dayOfWeek = daysOfWeek[index], isToday = false)
            }
        }
    }
}

@Composable
fun DateCard(dayOfWeek: LocalDate, isToday: Boolean) {
    val shortDayOfWeek = when (dayOfWeek.dayOfWeek.value) {
        1 -> "Pon"
        2 -> "Wto"
        3 -> "Śro"
        4 -> "Czw"
        5 -> "Pią"
        6 -> "Sob"
        7 -> "Niedz"
        else -> ""
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF9C27B0)),  // Another accent color
        modifier = Modifier
            .width(65.dp)
            .height(75.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = if (isToday) "Dzisiaj" else shortDayOfWeek,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dayOfWeek.dayOfMonth.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}*/


@Composable
fun DateSelectionRow() {
    val timePeriods = listOf("Dzisiaj", "Jutro", "Tydzień", "Miesiąc", "Wszystkie")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)  // Tight spacing for a clean look
    ) {
        items(timePeriods.size) { index ->
            PeriodCard(periodLabel = timePeriods[index])
        }
    }
}

@Composable
fun PeriodCard(periodLabel: String) {
    Card(
        shape = RoundedCornerShape(16.dp),  // Smaller rounded corners for a modern look
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6A74E0)),
        modifier = Modifier
            .width(85.dp)  // Small, compact width
            .height(30.dp),  // Short height for elegance
        elevation = CardDefaults.elevatedCardElevation(2.dp)  // Minimal elevation for subtle depth
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)  // Very tight padding to maximize space
        ) {
            Text(
                text = periodLabel,
                fontSize = 14.sp,  // Small, clean font size
                fontWeight = FontWeight.SemiBold,  // Semi-bold for a modern feel
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1  // Ensuring it fits in one line
            )
        }
    }
}


@Composable
fun ReservationsSection(reservations: List<Reservation>, navigateToReservation: () -> Unit) {
    if (reservations.isNotEmpty()) {


        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(reservations) { reservation ->
                BookingCard(
                    status = "Pending",
                    roomName = "Pokoj numer 4",
                    startTime = "12:00",
                    endTime = "12:30",
                    reservationId = reservation.id,
                    floorNumber = "4",
                    navigateToReservation = navigateToReservation
                )
            }
        }
    } else {
        Text(
            text = "Brak nadchodzących rezerwacji.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@Composable
fun BookingCard(
    status: String,
    roomName: String,
    floorNumber: String,
    startTime: String,
    endTime: String,
    reservationId: String,
    navigateToReservation: () -> Unit
) {
    // Define status color based on the status value
    val statusColor = when (status) {
        "Confirmed" -> Color(0xFF4CAF50)  // Green for confirmed
        "Pending" -> Color(0xFFFFC107)    // Yellow for pending
        "Cancelled" -> Color(0xFFF44336)  // Red for cancelled
        else -> Color.Gray                // Default grey for unknown status
    }

    // Card with updated color
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3E8E8)),  // Softer background color
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .clickable { navigateToReservation() },
        elevation = CardDefaults.elevatedCardElevation(6.dp),  // More elevated for a modern look
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Row for status indicator and times
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Indicator and Status Text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = status,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = statusColor
                    )
                }

                // Start and End Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Start Time",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = startTime,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "End Time",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = endTime,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Room Name and Floor
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = roomName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MeetingRoom,  // You can replace this with a "floor" icon if available
                    contentDescription = "Floor Number",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Piętro: $floorNumber",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Reservation ID
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ID: $reservationId",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}


/*// Status Chip Composable for better visual emphasis
@Composable
fun StatusChip(status: String) {
    Box(
        modifier = Modifier
            .background(
                color = when (status) {
                    "CONFIRMED" -> Color(0xFF4CAF50)
                    "PENDING" -> Color(0xFFFFC107)
                    "CANCELLED" -> Color(0xFFE57373)
                    else -> Color.Gray
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}*/


