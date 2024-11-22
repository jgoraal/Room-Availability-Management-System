package com.example.apptemplates.presentation.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.R
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.RecurrencePattern
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.reservation.ReservationStatus
import com.example.apptemplates.data.user.User
import com.example.apptemplates.presentation.main.home.components.ReservationDetailsDialog
import com.example.apptemplates.presentation.main.temp.DarkThemeColors
import com.example.apptemplates.presentation.main.temp.DarkThemeComponentsColors
import com.example.apptemplates.presentation.main.temp.LightThemeColors
import com.example.apptemplates.presentation.main.temp.LightThemeComponentsColors
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.presentation.main.temp.ThemeComponentColors
import com.example.apptemplates.presentation.main.temp.TimePeriod
import com.example.apptemplates.ui.theme.getContentBackGround
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Composable
fun HomeView(
    padding: PaddingValues,
    viewModel: HomeViewModel,
    uiState: MainUiState,
    navigateToReservation: () -> Unit = {},
    navigateToProfile: () -> Unit = {}
) {
    /*val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFDEDD4),
            Color(0xFFFCE5D1),  // Light warm sand, soft and gentle at the top
            Color(0xFFD8CAB8),  // Muted beige-gray in the middle
            Color(0xFFA8B9A4),  // Soft sage green toward the bottom
            Color(0xFFFCE5D1),   // Light warm sand again for consistency
            Color(0xFFFDEDD4),
        )
    )


    val contentBackgroundColor = Color.White.copy(alpha = 0.9f) // Semi-transparent white for cards
    val textPrimaryColor = Color(0xFF212121) // Dark gray for text
    val textSecondaryColor = Color(0xFF757575) // Light gray for secondary text
    val iconTintColor = Color(0xFFFFAB91) // Orange-pink tint for icons to match the gradient*/


    ReservationDetailsDialog(
        uiState,
        viewModel
    ) {
        viewModel.showDialog(false)
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(getContentBackGround())
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            HeaderSection(
                uiState.user,
                reservationsLeft = uiState.reservationsLeft ?: 0,
                maxReservations = uiState.maxReservations ?: 0,
                lastReservationDate = uiState.lastReservationDate ?: "Brak",
                navigateToProfile = navigateToProfile
            )
        }


        if (uiState.user?.verified == false) {
            item {
                EmailVerificationReminder()
            }
        }


        item {
            //Spacer(modifier = Modifier.height(2.dp))
        }

        item {
            DateAndReservationsSection(
                uiState = uiState,
                viewModel = viewModel,
                navigateToReservation = { viewModel.showDialog() }
            )
        }

        /*item {
            ReserveRoomButton(navigateToReservation)
        }*/
    }
}

@Composable
fun HeaderSection(
    user: User? = null,
    reservationsLeft: Int = 2,
    maxReservations: Int = 5,
    lastReservationDate: String? = null,
    navigateToProfile: () -> Unit = {}
) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = if (isDarkTheme) DarkThemeColors else LightThemeColors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Kontener z zaokrąglonymi rogami i gradientowym tłem
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = colors.headerBackgroundGradient
                    )
                )
                .border(
                    width = 1.dp,
                    color = colors.borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Zdjęcie profilowe
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(colors.profileBackground)
                        .clickable { navigateToProfile() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.guest_profile_icon),
                        contentDescription = "Zdjęcie profilowe",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Teksty powitania
                Text(
                    text = "Witaj, ${user?.username ?: "Nieznajomy"}!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textColorSecondary
                    ),
                    textAlign = TextAlign.Center
                )

                // Pasek postępu rezerwacji
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val reservationText = if (reservationsLeft < maxReservations) {
                        "Pozostało $reservationsLeft z $maxReservations rezerwacji"
                    } else {
                        "Wykorzystano wszystkie $maxReservations rezerwacje"
                    }

                    Text(
                        text = reservationText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textColorPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = reservationsLeft.toFloat() / maxReservations.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = colors.progressColor,
                        trackColor = colors.progressTrackColor,
                    )
                }

                // Data ostatniej rezerwacji
                lastReservationDate?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Ostatnia rezerwacja",
                            tint = colors.iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ostatnia rezerwacja: $it",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textColorPrimary
                            )
                        )
                    }
                }
            }
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
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning, contentDescription = null, tint = Color.Black
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
fun DateAndReservationsSection(
    uiState: MainUiState, viewModel: HomeViewModel, navigateToReservation: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = if (isDarkTheme) DarkThemeComponentsColors else LightThemeComponentsColors

    // Container for the title, date row, and reservations list
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // To allow gradient background
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = colors.cardGradient
                    )
                )
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp)
            ) {
                // Title section
                Text(
                    text = "Twoje nadchodzące rezerwacje",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryText,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Date row section
                DateSelectionRow(viewModel, state = uiState, colors = colors)

                // Divider to separate dates and reservations
                HorizontalDivider(
                    color = colors.dividerColor.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Reservations list
                ReservationsSection(
                    uiState.filteredReservations,
                    state = uiState,
                    viewModel,
                    navigateToReservation,
                    colors = colors
                )
            }
        }
    }
}


/*@Composable
fun DateAndReservationsSection(
    uiState: MainUiState, viewModel: HomeViewModel, navigateToReservation: () -> Unit
) {
    // Container for the title, date row, and reservations list
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // To allow gradient background
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFB5C9A3), // Soft olive green at the top
                            Color(0xFF8FA58D)  // Muted teal for a gentle contrast at the bottom
                        )
                    )
                )
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp)
            ) {
                // Title section
                Text(
                    text = "Twoje nadchodzące rezerwacje",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D2D2D), // Dark charcoal gray for good contrast
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Date row section
                DateSelectionRow(viewModel, state = uiState)

                // Divider to separate dates and reservations
                HorizontalDivider(
                    color = Color(0xFF8C867D).copy(alpha = 0.5f), // Subtle taupe tone for divider
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Reservations list
                ReservationsSection(
                    uiState.filteredReservations, state = uiState, viewModel, navigateToReservation
                )
            }
        }
    }
}*/


/*@Composable
fun DateSelectionRow(viewModel: HomeViewModel, state: MainUiState) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(TimePeriod.entries) { period ->
            PeriodCard(
                periodLabel = period.getPeriodName(),
                isSelected = period == state.reservationTimePeriods,
                onClick = {
                    viewModel.changeReservationTimePeriod(period)
                    viewModel.filterReservations()
                }
            )
        }
    }
}*/

@Composable
fun DateSelectionRow(viewModel: HomeViewModel, state: MainUiState, colors: ThemeComponentColors) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(TimePeriod.entries) { period ->
            PeriodCard(
                periodLabel = period.getPeriodName(),
                isSelected = period == state.reservationTimePeriods,
                onClick = {
                    viewModel.changeReservationTimePeriod(period)
                    viewModel.filterReservations()
                },
                colors = colors
            )
        }
    }
}


private fun TimePeriod.getPeriodName(): String {
    return when (this) {
        TimePeriod.TODAY -> "Dzisiaj"
        TimePeriod.TOMORROW -> "Jutro"
        TimePeriod.WEEK -> "Tydzień"
        TimePeriod.MONTH -> "Miesiąc"
        TimePeriod.ALL -> "Wszystkie"
    }
}

/*@Composable
fun PeriodCard(periodLabel: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF8FA58D) else Color.Transparent
        ),
        modifier = Modifier
            .size(width = 90.dp, height = 40.dp)
            .background(
                brush = if (isSelected) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF7E57C2),
                            Color(0xFF8FA58D)
                        ) // Muted gradient for selected
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF5F5F5),
                            Color(0xFFE0E0E0)
                        ) // Very light taupe for unselected
                    )
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(16.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = periodLabel,
                color = if (isSelected) Color.White else Color(0xFF8C867D), // Taupe for unselected text
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}*/

@Composable
fun PeriodCard(
    periodLabel: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: ThemeComponentColors
) {
    val backgroundBrush = if (isSelected) {
        Brush.horizontalGradient(colors = colors.selectedBackground)
    } else {
        Brush.horizontalGradient(colors = listOf(Color.Transparent, Color.Transparent))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(width = 90.dp, height = 40.dp)
            .background(
                brush = backgroundBrush,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(16.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = periodLabel,
                color = if (isSelected) colors.primaryText else colors.secondaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


/*@Composable
fun ReservationsSection(
    reservations: List<Reservation>,
    state: MainUiState,
    viewModel: HomeViewModel,
    navigateToReservation: () -> Unit
) {
    if (reservations.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reservations) { reservation ->
                BookingCard(
                    status = reservation.status.changeStatusName(),
                    roomName = reservation.roomId.changeRoomName(state),
                    startTime = reservation.startTime.toTime(),
                    endTime = reservation.endTime.toTime(),
                    reservationId = reservation.id,
                    floorNumber = reservation.roomId.getFloorName(state),
                    upcomingDate = reservation.getUpcomingDate(),
                    navigateToReservation = {
                        navigateToReservation()
                        viewModel.setSelectedReservation(reservation)
                    }
                )
            }
        }
    } else {
        // No reservations message with improved styling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(
                    Color(0xFFE2E8DD),
                    shape = RoundedCornerShape(12.dp)
                ) // Ciepły szaro-zielony
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EventBusy,
                    contentDescription = "No Reservations Icon",
                    tint = Color(0xFF6B8E6D), // Oliwkowo-zielony odcień dla ikony
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Brak nadchodzących rezerwacji.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF5A6D5A), // Stonowany szaro-zielony dla tekstu
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}*/

@Composable
fun ReservationsSection(
    reservations: List<Reservation>,
    state: MainUiState,
    viewModel: HomeViewModel,
    navigateToReservation: () -> Unit,
    colors: ThemeComponentColors
) {
    if (reservations.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reservations) { reservation ->
                BookingCard(
                    status = reservation.status.changeStatusName(),
                    roomName = reservation.roomId.changeRoomName(state),
                    startTime = reservation.startTime.toTime(),
                    endTime = reservation.endTime.toTime(),
                    reservationId = reservation.id,
                    floorNumber = reservation.roomId.getFloorName(state),
                    upcomingDate = reservation.getUpcomingDate(),
                    navigateToReservation = {
                        navigateToReservation()
                        viewModel.setSelectedReservation(reservation)
                    },
                    colors = colors
                )
            }
        }
    } else {
        // No reservations message with improved styling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .background(
                    colors.cardBackground,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EventBusy,
                    contentDescription = "Brak rezerwacji",
                    tint = colors.iconColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Brak nadchodzących rezerwacji.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.primaryText,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


private fun ReservationStatus.changeStatusName(): String {
    return when (this) {
        ReservationStatus.CONFIRMED -> "Potwierdzone"
        ReservationStatus.PENDING -> "Oczekujące"
        ReservationStatus.CANCELED -> "Anulowane"
        else -> "Inne"
    }
}

private fun Reservation.getUpcomingDate(): String {

    return when (this.isRecurring) {
        true -> this.recurrencePattern.getUpcomingDate(this.startTime)
        false -> {

            val today = LocalDate.now()
            val startReservation =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneOffset.UTC)
                    .toLocalDate()

            if (today.isAfter(startReservation)) {
                return "Koniec"
            }

            this.startTime.parseDate()
        }
    }
}

fun RecurrencePattern?.getUpcomingDate(startTime: Long): String {
    if (this == null) return "?.?.??"

    val today = LocalDate.now()
    val startReservation =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneOffset.UTC).toLocalDate()
    val endReservation =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(this.endDate), ZoneOffset.UTC).toLocalDate()

    return when {
        today.isBefore(startReservation) -> startTime.parseDate()
        today.isAfter(endReservation) -> "Koniec"
        else -> {
            // Calculate days between today and the start of the recurrence
            val daysBetween = ChronoUnit.DAYS.between(startReservation, today).toInt()
            val pattern = when (this.frequency) {
                RecurrenceFrequency.WEEKLY -> 7
                RecurrenceFrequency.BIWEEKLY -> 14
                RecurrenceFrequency.MONTHLY -> 28
            }

            // Find the next recurrence date
            val nextOccurrenceOffset = pattern - (daysBetween % pattern)
            val nextOccurrence = today.plusDays(nextOccurrenceOffset.toLong())

            // Ensure it doesn't exceed the end date
            if (nextOccurrence.isAfter(endReservation)) {
                "Koniec"
            } else {
                nextOccurrence.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                    .parseDate() + " Kolejna -> " + nextOccurrence.plusDays(pattern.toLong())
                    .atStartOfDay()
                    .toInstant(ZoneOffset.UTC).toEpochMilli().parseDate()
            }
        }
    }
}


private fun Long.parseDate(): String {
    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)

    val dayOfWeek = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "Pon, "
        DayOfWeek.TUESDAY -> "Wto, "
        DayOfWeek.WEDNESDAY -> "Śro, "
        DayOfWeek.THURSDAY -> "Czw, "
        DayOfWeek.FRIDAY -> "Pią, "
        DayOfWeek.SATURDAY -> "Sob, "
        DayOfWeek.SUNDAY -> "Niedz, "
        null -> "?, "
    }

    return dayOfWeek + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

private fun String.changeRoomName(state: MainUiState): String {
    val room = state.rooms.find { it.id == this }

    if (room == null) {
        return "Pokój ???"
    }

    val parts = room.name.split(" ")

    return "Pokój ${parts[1]}"
}

private fun Long.toTime(): String {
    val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC).toLocalTime()
    return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}

private fun String.getFloorName(state: MainUiState): String {
    val room = state.rooms.find { it.id == this }

    if (room == null) {
        return "Piętro: ???"
    }

    return "Piętro: " + when (room.floor) {
        1 -> "Parter"
        2 -> "1."
        3 -> "2."
        else -> "???"
    }
}

/*@Composable
fun BookingCard(
    status: String,
    roomName: String,
    floorNumber: String,
    startTime: String,
    endTime: String,
    reservationId: String,
    upcomingDate: String, // New parameter for upcoming date
    navigateToReservation: () -> Unit
) {
    // Define status color based on the status value
    val statusColor = when (status) {
        "Potwierdzone" -> Color(0xFF4CAF50)  // Green for confirmed
        "Oczekujące" -> Color(0xFFFFC107)    // Yellow for pending
        "Anulowane" -> Color(0xFFF44336)     // Red for cancelled
        else -> Color.Gray                   // Default grey for unknown status
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)), // Light, neutral background
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { navigateToReservation() },
        elevation = CardDefaults.elevatedCardElevation(10.dp), // Slightly higher elevation
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Status Indicator Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = statusColor
                    )
                }
                Text(
                    text = "$startTime - $endTime",
                    fontSize = 15.sp,
                    color = Color(0xFF4D4D4D) // Dark gray for better readability
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Room Information with Divider
            Text(
                text = roomName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF333333) // Dark charcoal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MeetingRoom,
                    contentDescription = "Floor Number",
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = floorNumber,
                    fontSize = 14.sp,
                    color = Color(0xFF757575) // Muted gray for subtler appearance
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                color = Color(0xFFBDBDBD).copy(alpha = 0.3f), // Light gray divider
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Upcoming Date Section
            if (upcomingDate.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event, // Icon representing a calendar or date
                        contentDescription = "Upcoming Date",
                        tint = Color(0xFF6D4C41), // Dark brown to align with the overall theme
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Space between icon and date text
                    Text(
                        text = upcomingDate,
                        fontSize = 14.sp,
                        color = Color(0xFF4D4D4D), // Muted dark gray for text
                        fontStyle = FontStyle.Italic
                    )
                }
            }


            // Reservation ID
            Text(
                text = "ID: $reservationId",
                fontSize = 12.sp,
                color = Color(0xFF757575),
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}*/

@Composable
fun BookingCard(
    status: String,
    roomName: String,
    floorNumber: String,
    startTime: String,
    endTime: String,
    reservationId: String,
    upcomingDate: String,
    navigateToReservation: () -> Unit,
    colors: ThemeComponentColors
) {
    // Define status color based on the status value
    val statusColor = when (status) {
        "Potwierdzone" -> Color(0xFF4CAF50)  // Green for confirmed
        "Oczekujące" -> Color(0xFFFFC107)    // Yellow for pending
        "Anulowane" -> Color(0xFFF44336)     // Red for cancelled
        else -> colors.primaryText           // Default text color for unknown status
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { navigateToReservation() },
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Status Indicator Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = statusColor
                    )
                }
                Text(
                    text = "$startTime - $endTime",
                    fontSize = 15.sp,
                    color = colors.secondaryText
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Room Information with Divider
            Text(
                text = roomName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colors.primaryText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MeetingRoom,
                    contentDescription = "Numer piętra",
                    tint = colors.iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = floorNumber,
                    fontSize = 14.sp,
                    color = colors.secondaryText
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                color = colors.dividerColor.copy(alpha = 0.3f),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Upcoming Date Section
            if (upcomingDate.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Data rezerwacji",
                        tint = colors.iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = upcomingDate,
                        fontSize = 14.sp,
                        color = colors.secondaryText,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            // Reservation ID
            Text(
                text = "ID: $reservationId",
                fontSize = 12.sp,
                color = colors.secondaryText,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ds() {
    HomeView(PaddingValues(0.dp), HomeViewModel(), MainUiState())
}


