package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.reservation.getNameInPolish
import com.example.apptemplates.presentation.main.temp.DarkThemeTimeLineColors
import com.example.apptemplates.presentation.main.temp.LightThemeTimeLineColors
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.presentation.main.temp.ThemeTimeLineColors


@Composable
fun NoAvailableRoomsMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFCE4EC), // Soft pink top gradient
                        Color(0xFFF8BBD0)  // Slightly darker pink bottom gradient
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE91E63), // Soft border color matching the gradient
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "No Rooms Available",
            tint = Color(0xFFD81B60), // Pink color to match the gradient
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Brak dostępnych sal. Spróbuj zmienić filtry.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF880E4F), // Deep pink for text
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/*@Composable
fun NoAvailableRoomsMessage(colors: ThemeReservationColors) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = colors.backgroundGradient
                )
            )
            .border(
                width = 1.dp,
                color = colors.borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Brak dostępnych sal",
            tint = colors.errorColor,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Brak dostępnych sal. Spróbuj zmienić filtry.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = colors.primaryText,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}*/


/*
@Composable
fun AvailableRoomsList(state: MainUiState, viewModel: ReservationViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors: ThemeTimeLineColors =
        if (isDarkTheme) DarkThemeTimeLineColors else LightThemeTimeLineColors
    val roomCardHeight = 320.dp
    val availableRoomCount = state.availableRooms.size
    val totalHeight = if (availableRoomCount > 0) {
        (roomCardHeight * availableRoomCount).coerceAtMost(500.dp)
    } else {
        roomCardHeight
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8ECD1), // Warm beige at the top
                        Color(0xFFF3E2B6)  // Soft light brown at the bottom
                    )
                )
            )
            .border(
                BorderStroke(1.dp, Color(0xFFE0CFA3)),
                RoundedCornerShape(16.dp)
            ) // Subtle border
            //.shadow(6.dp, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Dostępne pokoje",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037) // Rich brown for the title
            ),
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        if (state.availableRooms.isEmpty()) {
            NoAvailableRoomsMessage()
        } else {
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
                        facilities = room.equipment.map { it.type },
                        floor = room.floor,
                        selectedRoom = { viewModel.changeSelectedRoom(room) }
                    )
                }
            }
        }
    }
}
*/

@Composable
fun AvailableRoomsList(state: MainUiState, viewModel: ReservationViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors: ThemeTimeLineColors =
        if (isDarkTheme) DarkThemeTimeLineColors else LightThemeTimeLineColors

    val roomCardHeight = 220.dp
    val availableRoomCount = state.availableRooms.size
    val totalHeight = if (availableRoomCount > 0) {
        (roomCardHeight * availableRoomCount).coerceAtMost(500.dp)
    } else {
        roomCardHeight
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = colors.timelineBackgroundGradient
                )
            )
            .border(
                BorderStroke(1.dp, colors.dividerColor),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Dostępne sale",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.timelineTitleColor
            ),
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        if (state.availableRooms.isEmpty()) {
            NoAvailableRoomsMessage(colors.noAvailableTextColor)
        } else {
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
                        facilities = room.equipment.map { it.type },
                        floor = room.floor,
                        selectedRoom = { viewModel.changeSelectedRoom(room) },
                        colors = colors,
                        ignoreEquipment = state.ignoreEquipment
                    )
                }
            }
        }
    }
}

@Composable
fun NoAvailableRoomsMessage(textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        textColor.copy(alpha = 0.1f),
                        textColor.copy(alpha = 0.05f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = textColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Brak dostępnych sal.",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                lineHeight = 22.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}


/*@Composable
fun AvailableRoomsList(state: MainUiState, viewModel: ReservationViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors: ThemeReservationColors =
        if (isDarkTheme) DarkThemeReservationColors else LightThemeReservationColors

    val roomCardHeight = 320.dp
    val availableRoomCount = state.availableRooms.size
    val totalHeight = if (availableRoomCount > 0) {
        (roomCardHeight * availableRoomCount).coerceAtMost(500.dp)
    } else {
        roomCardHeight
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = colors.backgroundGradient
                )
            )
            .border(
                BorderStroke(1.dp, colors.borderColor),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Dostępne pokoje",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            ),
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        if (state.availableRooms.isEmpty()) {
            NoAvailableRoomsMessage(colors)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(totalHeight)
                    .padding(bottom = 16.dp)
            ) {
                items(state.availableRooms) { room ->
                    EnhancedRoomCard(
                        room = room,
                        colors = colors,
                        selectedRoom = { viewModel.changeSelectedRoom(room) }
                    )
                }
            }
        }
    }
}*/


/*@Composable
fun EnhancedRoomCard(
    roomName: String,
    capacity: Int,
    facilities: List<EquipmentType>,
    floor: Int,
    colors: ThemeTimeLineColors = LightThemeTimeLineColors,
    selectedRoom: () -> Unit
) {

    val allFacilities = EquipmentType.entries.toList()


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            //.clickable { selectedRoom() }
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFDF4E3) // Soft warm beige
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, Color(0xFFB0BEC5)) // Light gray border
    ) {
        Row(
            modifier = Modifier
                //.fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                RoomInfoSection(roomName, floor, capacity)
                Spacer(modifier = Modifier.height(8.dp))
                FacilitiesSection(allFacilities, facilities)
            }


        }
    }
}


@Composable
fun RoomInfoSection(roomName: String, floor: Int, capacity: Int, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.padding(start = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconWithBackground(Icons.Default.Home, Color(0xFF8D6E63))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sala ${roomName.split(" ").last()}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF5D4037),
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconWithBackground(Icons.Default.Stairs, Color(0xFF8D6E63))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Piętro: $floor",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF5D4037)
                ),
                maxLines = 1
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconWithBackground(Icons.Default.Groups, Color(0xFF8D6E63))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$capacity Osób",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF5D4037)
                )
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FacilitiesSection(
    allFacilities: List<EquipmentType>,
    availableFacilities: List<EquipmentType>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Wyposażenie",
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8D6E63)),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            allFacilities.forEach { facility ->
                val isAvailable = facility in availableFacilities
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = if (isAvailable) "Dostępne" else "Niedostępne",
                        tint = if (isAvailable) Color(0xFF66BB6A) else Color(0xFFEF9A9A), // Green for available, red for unavailable
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = facility.getNameInPolish(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isAvailable) Color(0xFF5D4037) else Color.Gray,
                            textDecoration = if (isAvailable) null else TextDecoration.LineThrough // Strikethrough for unavailable
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun IconWithBackground(icon: ImageVector, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}*/


@Composable
fun EnhancedRoomCard(
    roomName: String,
    capacity: Int,
    facilities: List<EquipmentType>,
    floor: Int,
    ignoreEquipment: Boolean = false,
    colors: ThemeTimeLineColors = DarkThemeTimeLineColors,
    selectedRoom: () -> Unit
) {
    val allFacilities = EquipmentType.entries.toList()
    val isDarkTheme = isSystemInDarkTheme()
    val cardGradient = if (isDarkTheme) {
        listOf(
            Color(0xFF1E3A3A), // Głęboki szmaragdowy
            Color(0xFF29665D), // Morska zieleń
            Color(0xFF3D9970)  // Stonowana zieleń
        )
    } else {
        listOf(
            Color(0xFFFFF9E6), // Bardzo jasny waniliowy
            Color(0xFFFFE7B8), // Ciepły pastelowy złoty
            Color(0xFFE3B87B)  // Delikatny miodowy brąz
        )
    }




    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { selectedRoom() }
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, colors.dividerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(cardGradient))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nazwa sali i piętro w jednym wierszu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = roomName.adjustRoomName(),
                    style = MaterialTheme.typography.headlineSmall.copy( // Większa czcionka
                        color = colors.primaryText,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = floor.adjustFloorNumber(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = colors.primaryText,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Liczba miejsc w sali
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (ignoreEquipment) Arrangement.Start else Arrangement.Center, // Wyrównanie elementów na środku
                modifier = Modifier
                    .fillMaxWidth() // Zajmuje pełną szerokość
                    .padding(top = 4.dp)
                    .padding(horizontal = if (ignoreEquipment) 16.dp else 0.dp)
            ) {
                IconWithBackground(Icons.Default.People, colors.iconColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Miejsca $capacity",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = colors.primaryText,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Wyposażenie
            if (!ignoreEquipment) {
                FacilitiesSection(allFacilities, facilities, colors)
            }

        }
    }
}

fun Int.adjustFloorNumber(): String {
    return when (this) {
        1 -> "Parter" // Parter oznaczany jako "Parter"
        2 -> "Piętro I"      // Pierwsze piętro
        3 -> "Piętro II"     // Drugie piętro
        4 -> "Piętro III"    // Trzecie piętro
        5 -> "Piętro IV"     // Czwarte piętro
        6 -> "Piętro V"      // Piąte piętro (i tak dalej)
        else -> "N/A" // W przypadku nieznanych wartości
    }
}

fun String.adjustRoomName(): String {
    return "Sala " + this.split(" ").last()
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FacilitiesSection(
    allFacilities: List<EquipmentType>,
    availableFacilities: List<EquipmentType>,
    colors: ThemeTimeLineColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Wyposażenie",
            style = MaterialTheme.typography.bodySmall.copy(
                color = colors.primaryText,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            allFacilities.forEach { facility ->
                val isAvailable = facility in availableFacilities
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = if (isAvailable) "Dostępne" else "Niedostępne",
                        tint = if (isAvailable) Color.Green else Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = facility.getNameInPolish(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isAvailable) colors.primaryText else colors.secondaryText,
                            textDecoration = if (isAvailable) null else TextDecoration.LineThrough
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun IconWithBackground(icon: ImageVector, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun S() {
    EnhancedRoomCard("Pokoj 2", 10, listOf(EquipmentType.COMPUTER, EquipmentType.WHITEBOARD), 3) { }
}


/*@Composable
fun EnhancedRoomCard(
    room: Room,
    colors: ThemeReservationColors,
    selectedRoom: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { selectedRoom() }
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, colors.borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = colors.backgroundGradient
                    )
                )
                .padding(16.dp)
        ) {
            // Informacje o pokoju
            RoomInfoSection(
                roomName = room.name,
                floor = room.floor,
                capacity = room.capacity,
                colors = colors
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Sekcja udogodnień
            FacilitiesSection(
                allFacilities = EquipmentType.entries.toList(),
                availableFacilities = room.equipment.map { it.type },
                colors = colors
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Przycisk rezerwacji
            Button(
                onClick = { selectedRoom() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.buttonColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "Zarezerwuj",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}*/

/*@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FacilitiesSection(
    allFacilities: List<EquipmentType>,
    availableFacilities: List<EquipmentType>,
    colors: ThemeReservationColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Udogodnienia",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.primaryText,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            allFacilities.forEach { facility ->
                val isAvailable = facility in availableFacilities
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = if (isAvailable) "Dostępne" else "Niedostępne",
                        tint = if (isAvailable) colors.accentColor else colors.secondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = facility.getNameInPolish(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (isAvailable) colors.primaryText else colors.secondaryText,
                            textDecoration = if (isAvailable) null else TextDecoration.LineThrough
                        )
                    )
                }
            }
        }
    }
}*/


/*@Composable
fun RoomInfoSection(
    roomName: String,
    floor: Int,
    capacity: Int,
    colors: ThemeReservationColors,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconWithBackground(Icons.Default.Home, colors.iconColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sala ${roomName.split(" ").last()}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = colors.primaryText,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconWithBackground(Icons.Default.Stairs, colors.iconColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Piętro: $floor",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colors.secondaryText
                ),
                maxLines = 1
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconWithBackground(Icons.Default.Groups, colors.iconColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$capacity Osób",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = colors.secondaryText
                )
            )
        }
    }
}*/