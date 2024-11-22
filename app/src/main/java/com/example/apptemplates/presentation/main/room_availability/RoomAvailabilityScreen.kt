package com.example.apptemplates.presentation.main.room_availability

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.presentation.main.reservation.components.CalendarView
import com.example.apptemplates.presentation.main.reservation.components.getDaysInMonth
import com.example.apptemplates.presentation.main.temp.DarkThemeTimeLineColors
import com.example.apptemplates.presentation.main.temp.LightThemeTimeLineColors
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.presentation.main.temp.ThemeTimeLineColors
import com.example.apptemplates.ui.theme.getContentBackGround
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


@Composable
fun RoomAvailabilityView(
    state: MainUiState,
    viewModel: RoomAvailabilityViewModel,
    padding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    viewModel.canSeeRoomAvailability()

    val showFloorSelector = remember { mutableStateOf(false) }
    val showRoomSelector = remember { mutableStateOf(false) }
    val isDatePickerVisible = remember { mutableStateOf(false) }
    val isFloorSelectorVisible = remember { mutableStateOf(false) }
    val selectedFloor = remember { mutableStateOf<String?>(null) }
    val selectedRoom = remember { mutableStateOf<String?>(null) }
    val isRoomSelectorVisible = remember { mutableStateOf(false) }
    val isButtonVisible = remember { mutableStateOf(false) }
    val canSeeRoomAvailability = remember { mutableStateOf(viewModel.canSeeRoomAvailability()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(getContentBackGround())
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {


        if (canSeeRoomAvailability.value) {

            // Header Title
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFDEDD4),
                                    Color(0xFFD8CAB8),
                                    Color(0xFFFDEDD4)
                                )
                            )
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sprawdź dostępność sali",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF4E342E)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }


            // Date Picker using TopDownElement
            item {

                TopDownElement(
                    visible = isDatePickerVisible,
                    title = if (state.selectedDate != null) {
                        "Wybrana data ${state.selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
                    } else {
                        "Wybierz datę"
                    },
                    imageVector = Icons.Default.Event,
                    content = {
                        DatePicker(
                            viewModel = viewModel,
                            startFromSunday = false,
                            onDateChange = {
                                isDatePickerVisible.value = false
                                showFloorSelector.value = true
                                //selectedDate.value = "Wybrano datę ${state.selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
                            }
                        )
                    }
                )
            }

            // Floor Selector using TopDownElement
            if (showFloorSelector.value) {
                item {

                    TopDownElement(
                        visible = isFloorSelectorVisible,
                        title = selectedFloor.value ?: "Wybierz piętro",
                        imageVector = Icons.Default.Domain,
                        content = {
                            val floors = listOf(
                                "Parter" to 1,
                                "1. Piętro" to 2,
                                "2. Piętro" to 3,
                                "Wszystkie" to null
                            )

                            floors.forEach { (floor, number) ->
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.changeFloor(number)
                                        if (state.selectedFloorNumber != number) {
                                            viewModel.changeRoom(null)
                                            selectedRoom.value = null
                                        }
                                        selectedFloor.value = "Wybrane piętro: $floor"
                                        viewModel.fetchRooms()
                                        isFloorSelectorVisible.value = false
                                        showRoomSelector.value = true
                                    },
                                    modifier = Modifier
                                        //.background(Color(0xFFF1F8E9)) // Light neutral background
                                        .clip(RoundedCornerShape(8.dp))
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween // Aligns the text and icon
                                        ) {
                                            // Floor Name
                                            Text(
                                                text = floor,
                                                color = if (selectedFloor.value?.contains(floor) == true) Color(
                                                    0xFF2E7D32
                                                ) else Color(0xFF757575), // Green for selected, Gray for others
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )

                                            // Check Icon - Only show for selected floor
                                            if (selectedFloor.value?.contains(floor) == true) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color(0xFF66BB6A), // Lighter green for check icon
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                )
                            }


                        }
                    )
                }
            }


            // Room Selector using TopDownElement
            if (showRoomSelector.value) {
                item {
                    TopDownElement(
                        visible = isRoomSelectorVisible,
                        title = selectedRoom.value ?: "Wybierz salę",
                        imageVector = Icons.Default.MeetingRoom,
                        content = {
                            if (state.selectedFloorNumber == null) {
                                val groupedRooms = state.rooms.groupBy { it.floor }

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 250.dp) // Increased height for better usability
                                        .padding(horizontal = 8.dp)
                                        .background(
                                            color = Color(0xFFF9F9F9), // Neutral background
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .border(1.dp, Color(0xFFB0BEC5), RoundedCornerShape(12.dp))
                                ) {
                                    groupedRooms.forEach { (floor, rooms) ->
                                        item {
                                            // Floor Header
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color(0xFFF1F8E9)) // Light green background for headers
                                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                                contentAlignment = Alignment.CenterStart
                                            ) {
                                                Text(
                                                    text = floor.getFloorName(),
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF2E7D32) // Deep green for header text
                                                    )
                                                )
                                            }
                                        }
                                        items(rooms) { room ->
                                            // Room Items
                                            DropdownMenuItem(
                                                onClick = {
                                                    viewModel.changeRoom(room)
                                                    isRoomSelectorVisible.value = false
                                                    selectedRoom.value =
                                                        "Wybrana sala: ${room.name.getRoomName()}"
                                                    isButtonVisible.value = true
                                                },
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 4.dp
                                                ),
                                                text = {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        Text(
                                                            text = room.name.getRoomName(),
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                color = Color(0xFF37474F), // Dark gray for room names
                                                                fontWeight = FontWeight.Normal
                                                            )
                                                        )
                                                        Icon(
                                                            imageVector = Icons.Default.MeetingRoom,
                                                            contentDescription = "Room Icon",
                                                            tint = Color(0xFF757575), // Muted gray icon
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 250.dp)
                                        .padding(horizontal = 8.dp)
                                        .background(
                                            color = Color(0xFFF9F9F9), // Neutral background
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .border(1.dp, Color(0xFFB0BEC5), RoundedCornerShape(12.dp))
                                ) {
                                    items(state.rooms) { room ->
                                        DropdownMenuItem(
                                            onClick = {
                                                viewModel.changeRoom(room)
                                                isRoomSelectorVisible.value = false
                                                selectedRoom.value =
                                                    "Wybrana sala: ${room.name.getRoomName()}"
                                                isButtonVisible.value = true
                                            },
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            ),
                                            text = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = room.name.getRoomName(),
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            color = Color(0xFF37474F), // Dark gray for room names
                                                            fontWeight = FontWeight.Normal
                                                        )
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Default.MeetingRoom,
                                                        contentDescription = "Room Icon",
                                                        tint = Color(0xFF757575), // Muted gray icon
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }

                    )
                }
            }

            if (isButtonVisible.value && state.selectedRoom != null) {
                item {
                    CheckButton(viewModel)
                }
            }


            // Timeline Section of Room Reservations
            item {
                TimelineSection(state)
            }
        } else {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFDEDD4),
                                    Color(0xFFD8CAB8),
                                    Color(0xFFFDEDD4)
                                )
                            )
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sprawdź dostępność sali",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color(0xFF4E342E)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(2) {
                Text("")
            }

            item {

                ConfirmEmailPrompt()

            }
        }


    }
}

private fun Int.getFloorName(): String {
    return when (this) {
        1 -> "Parter"
        2 -> "1. Piętro"
        3 -> "2. Piętro"
        else -> ""
    }
}

private fun String.getRoomName(): String {
    return "Sala " + this.split(" ").last()
}

@Composable
fun CheckButton(viewModel: RoomAvailabilityViewModel) {
    Button(
        onClick = { viewModel.checkAvailability() },
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFAED581),
                        Color(0xFF8BC34A)
                    ), // Zgaszona oliwkowa i jasna zieleń
                    startX = 0f,
                    endX = 1000f
                )
            )
        /*.shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))*/, // Subtelny cień
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {
        Text(
            text = "Sprawdź dostępność",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF2E7D32), // Głęboka zieleń dla kontrastu
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@Composable
fun ConfirmEmailPrompt() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFFF7EFE4) // Warm beige background
            )
            .border(
                width = 1.dp,
                color = Color(0xFFBCAAA4), // Light brown border
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = "Confirm Email",
            tint = Color(0xFF8D6E63), // Warm brown icon color
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Proszę potwierdzić swój adres e-mail, aby sprawdzić dostępność sal.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF6D4C41), // Dark brown for text
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        /*Button(
            onClick = { *//* Handle resend email confirmation *//* },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD7CCC8), // Muted beige for button background
                contentColor = Color(0xFF4E342E) // Dark brown text color
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = "Wyślij ponownie e-mail potwierdzający")
        }*/
    }
}


@Composable
fun DatePicker(
    viewModel: RoomAvailabilityViewModel,
    startFromSunday: Boolean,
    onDateChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    val currentCalendar = Calendar.getInstance()
    val calendar by remember { mutableStateOf(Calendar.getInstance()) }
    var dates by remember {
        mutableStateOf(
            getDaysInMonth(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)
            )
        )
    }
    val selectedDate = viewModel.state.collectAsState().value.selectedDate

    // Determine if the previous button should be shown (only if the displayed month is after the current month)
    val canGoBack = calendar.get(Calendar.YEAR) > currentCalendar.get(Calendar.YEAR) ||
            (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) > currentCalendar.get(Calendar.MONTH))

    // Function to update the month
    fun updateMonth(monthOffset: Int) {
        calendar.add(Calendar.MONTH, monthOffset)
        dates = getDaysInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
    }

    CalendarView(
        selectedDate = selectedDate,
        month = calendar.time,
        date = dates,
        displayNext = true,
        displayPrev = canGoBack,
        onClickNext = { updateMonth(1) },  // Go to the next month
        onClickPrev = {
            if (canGoBack) {
                updateMonth(-1)
            }
        },  // Go to the previous month
        onClick = {
            viewModel.changeDate(it)
            onDateChange()
        },
        startFromSunday = startFromSunday,
        modifier = Modifier,
        year = calendar.get(Calendar.YEAR),
        monthh = calendar.get(Calendar.MONTH)
    )

}

/*@Composable
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
            onClick = {  Show date picker dialog  },
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
}*/

/*@Composable
fun TimelineSection(state: MainUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (state.times != null) 400.dp else 280.dp)
            .padding(vertical = 16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5EFE6),
                        Color(0xFFE0D5C2)
                    ) // Naturalny gradient w pastelowych tonach
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Terminarz Sali" + if (state.selectedRoom != null) state.selectedRoom.name.drop(
                    state.selectedRoom.name.indexOf(" ")
                ) else "",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF5D4037) // Ciepły, ciemny brąz
                ),
                textAlign = TextAlign.Center
            )
        }

        HorizontalDivider(
            color = Color(0xFF8D6E63).copy(alpha = 0.6f), // Przygaszony brąz dla subtelniejszego efektu
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentHeight()
        ) {
            if (!state.times.isNullOrEmpty()) {
                items(state.times) { time ->
                    ReservationSlotCard(time)
                }
            } else {
                item {
                    NoAvailableMessage()
                }
            }
        }
    }
}*/

@Composable
fun TimelineSection(state: MainUiState) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors: ThemeTimeLineColors =
        if (isDarkTheme) DarkThemeTimeLineColors else LightThemeTimeLineColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (state.times != null) 400.dp else 280.dp)
            .padding(vertical = 16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = colors.timelineBackgroundGradient
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Terminarz Sali " + (state.selectedRoom?.name?.substringAfter(" ") ?: ""),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colors.timelineTitleColor
                ),
                textAlign = TextAlign.Center
            )
        }

        HorizontalDivider(
            color = colors.dividerColor.copy(alpha = 0.6f),
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentHeight()
        ) {
            if (!state.times.isNullOrEmpty()) {
                items(state.times) { time ->
                    ReservationSlotCard(time)
                }
            } else {
                item {
                    NoAvailableMessage()
                }
            }
        }
    }
}


@Composable
fun ReservationSlotCard(time: Triple<Long, Long, Boolean>) {
    val start = formatTimeFromLong(time.first)
    val end = formatTimeFromLong(time.second)
    val isReserved = time.third

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 4.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (isReserved) Color(0xFFFDEDEC) else Color(0xFFE8F5E9) // Delikatne odcienie różu i zieleni
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isReserved) Icons.Default.EventBusy else Icons.Default.EventAvailable,
                contentDescription = if (isReserved) "Zarezerwowane" else "Dostępne",
                tint = if (isReserved) Color(0xFFC62828) else Color(0xFF2E7D32), // Przygaszone czerwone i zielone tony
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = if (isReserved) "Zarezerwowane" else "Dostępne",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isReserved) Color(0xFFC62828) else Color(0xFF2E7D32)
                    )
                )
                Text(
                    text = "$start - $end",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF616161)),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun NoAvailableMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5)) // Subtelne, jasne tło
            .padding(vertical = 16.dp, horizontal = 12.dp), // Zwiększenie przestrzeni wewnętrznej
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Brak dostępnych terminów",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF616161), // Ciemny szary dla lepszej widoczności
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp // Drobne rozstrzelenie dla elegancji
            ),
            textAlign = TextAlign.Center
        )
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
private fun Avail() {
    RoomAvailabilityView(MainUiState(), RoomAvailabilityViewModel())
}

@Composable
fun TopDownElement(
    visible: MutableState<Boolean>,
    imageVector: ImageVector? = null,
    title: String? = "Wybierz datę",
    titleStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    titleColor: Color = Color(0xFF5D4037), // Przygaszony brąz dla naturalności
    iconTint: Color = Color(0xFF795548), // Stonowany, ciepły brąz
    containerColor: Brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFF3E5AB), Color(0xFFD7CCC8)) // Ciepły beżowy gradient
    ),
    borderColor: Color = Color(0xFFBCAAA4), // Ciepły taupe dla obramowania
    expandedContainerColor: Color = Color(0xFFEFEDE8), // Jasne tło po rozwinięciu
    expandedBorderColor: Color = Color(0xFF8D6E63), // Przygaszony brązowy dla obramowania rozwiniętego
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)) // Dodanie cienia dla głębi
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { visible.value = !visible.value },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Ikona",
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = title ?: "",
                style = titleStyle,
                color = titleColor,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (visible.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Ikona przełączania",
                tint = iconTint,
                modifier = Modifier.scale(1.5f)
            )
        }

        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp)
                    .background(expandedContainerColor, shape = RoundedCornerShape(12.dp))
                    .border(2.dp, expandedBorderColor, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}





