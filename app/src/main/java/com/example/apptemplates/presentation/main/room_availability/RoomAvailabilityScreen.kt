package com.example.apptemplates.presentation.main.room_availability

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AccessTime
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.presentation.main.reservation.components.CalendarView
import com.example.apptemplates.presentation.main.reservation.components.getDaysInMonth
import com.example.apptemplates.presentation.main.room_availability.objects.QuickReservation
import com.example.apptemplates.presentation.main.temp.DarkThemeComponentsColors
import com.example.apptemplates.presentation.main.temp.DarkThemeHeaderColors
import com.example.apptemplates.presentation.main.temp.DarkThemeTimeLineColors
import com.example.apptemplates.presentation.main.temp.LightThemeComponentsColors
import com.example.apptemplates.presentation.main.temp.LightThemeHeaderColors
import com.example.apptemplates.presentation.main.temp.LightThemeTimeLineColors
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.presentation.main.temp.ThemeComponentColors
import com.example.apptemplates.presentation.main.temp.ThemeTimeLineColors
import com.example.apptemplates.ui.theme.getContentBackGround
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar


@Composable
fun RoomAvailabilityView(
    state: MainUiState,
    viewModel: RoomAvailabilityViewModel,
    padding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    navigateToReservation: () -> Unit
) {
    viewModel.canSeeRoomAvailability()

    val isDatePickerVisible = remember { mutableStateOf(false) }
    val isFloorSelectorVisible = remember { mutableStateOf(false) }
    val isRoomSelectorVisible = remember { mutableStateOf(false) }

    val canSeeRoomAvailability = remember { mutableStateOf(viewModel.canSeeRoomAvailability()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(getContentBackGround())
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (state.times.isNullOrEmpty()) 20.dp else 10.dp)
    ) {

        if (canSeeRoomAvailability.value) {


            // Header Title
            item {
                //AvailabilityHeader()
            }


            // Date Picker using TopDownElement
            item {

                TopDownElement(
                    visible = isDatePickerVisible,
                    title = state.selectedDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                        ?.let { "Wybrana data $it" } ?: "Wybierz datę",
                    imageVector = Icons.Default.Event,
                    content = {
                        DatePicker(
                            viewModel = viewModel,
                            startFromSunday = false,
                            onDateChange = {
                                if (!state.times.isNullOrEmpty()) {
                                    viewModel.clearTimeSlots()
                                }
                                isDatePickerVisible.value = false
                                viewModel.updateShowFloorSelector(true)
                                viewModel.saveState()
                            }
                        )
                    }
                )
            }

            // Floor Selector using TopDownElement
            if (state.showFloorSelector) {
                item {

                    TopDownElement(
                        visible = isFloorSelectorVisible,
                        title = state.selectedFloorName.getFloorName(),
                        imageVector = Icons.Default.Domain,
                        content = {
                            val floors = listOf(
                                "Parter" to 1,
                                "I Piętro" to 2,
                                "II Piętro" to 3,
                                "Wszystkie" to null
                            )

                            floors.forEach { (floor, number) ->
                                val isSelected =
                                    state.selectedFloorName?.contentEquals(floor) == true
                                val isDarkTheme = isSystemInDarkTheme()

                                // Definiowanie kolorów zależnie od motywu i stanu zaznaczenia
                                val backgroundColor = if (isSelected) {
                                    if (isDarkTheme) Color(0xFF3D5A80) else Color(0xFFFCE5D1)
                                } else {
                                    Color.Transparent
                                }

                                val textColor = if (isSelected) {
                                    if (isDarkTheme) Color(0xFFFFFFFF) else Color(0xFF3E2723)
                                } else {
                                    if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
                                }

                                val iconTint =
                                    if (isDarkTheme) Color(0xFFFFFFFF) else Color(0xFF6D4C41)

                                DropdownMenuItem(
                                    onClick = {
                                        if (!state.times.isNullOrEmpty()) {
                                            viewModel.clearTimeSlots()
                                        }

                                        viewModel.changeFloor(number)
                                        if (state.selectedFloorNumber != number) {
                                            viewModel.changeRoom(null)
                                            //selectedRoom.value = null
                                        }

                                        viewModel.updateSelectedFloor(floor)
                                        //selectedFloor.value = "Wybrane piętro: $floor"
                                        viewModel.fetchRooms()
                                        isFloorSelectorVisible.value = false
                                        viewModel.updateShowRoomSelector(true)

                                        viewModel.saveState()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .then(
                                            if (isSelected) {
                                                Modifier
                                                    .background(
                                                        color = backgroundColor,
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (isDarkTheme) Color(0xFF98C1D9) else Color(
                                                            0xFF8D6E63
                                                        ),
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                            } else {
                                                Modifier
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            // Nazwa piętra
                                            Text(
                                                text = floor,
                                                color = textColor,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )

                                            // Ikona zaznaczenia
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = iconTint,
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
            if (state.showRoomSelector) {
                item {
                    TopDownElement(
                        visible = isRoomSelectorVisible,
                        title = state.selectedRoom.getRoomName(),
                        imageVector = Icons.Default.MeetingRoom,
                        content = {

                            val compColors =
                                if (isSystemInDarkTheme()) DarkThemeComponentsColors else LightThemeComponentsColors

                            if (state.selectedFloorNumber == null) {
                                // Grupowanie sal po piętrach
                                val groupedRooms = state.rooms.groupBy { it.floor }

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 350.dp)
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                    groupedRooms.onEachIndexed { index, entry ->
                                        val (floor, rooms) = entry
                                        item {
                                            Card(
                                                shape = RoundedCornerShape(12.dp),
                                                elevation = CardDefaults.cardElevation(4.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = compColors.cardBackground
                                                )
                                            ) {
                                                Column(modifier = Modifier.padding(12.dp)) {
                                                    Text(
                                                        text = floor.getFloorName(),
                                                        style = MaterialTheme.typography.titleMedium.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            color = compColors.primaryText
                                                        ),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(bottom = 8.dp)
                                                    )

                                                    rooms.forEachIndexed { roomIndex, room ->
                                                        val isSelectedRoom =
                                                            (state.selectedRoom == room)
                                                        DropdownMenuItem(
                                                            onClick = {
                                                                if (!state.times.isNullOrEmpty()) {
                                                                    viewModel.clearTimeSlots()
                                                                }

                                                                viewModel.changeRoom(room)
                                                                isRoomSelectorVisible.value = false
                                                                viewModel.updateShowButton(true)
                                                                viewModel.saveState()
                                                            },
                                                            modifier = Modifier.highlightIfSelected(
                                                                isSelectedRoom,
                                                                compColors
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
                                                                            color = if (isSelectedRoom) compColors.primaryText else compColors.secondaryText,
                                                                            fontWeight = FontWeight.SemiBold
                                                                        )
                                                                    )

                                                                    Surface(
                                                                        shape = RoundedCornerShape(8.dp),
                                                                        color = compColors.accentColor.copy(
                                                                            alpha = 0.2f
                                                                        )
                                                                    ) {
                                                                        Text(
                                                                            text = floor.getFloorName(),
                                                                            style = MaterialTheme.typography.bodySmall.copy(
                                                                                color = compColors.accentColor,
                                                                                fontWeight = FontWeight.Bold
                                                                            ),
                                                                            modifier = Modifier.padding(
                                                                                horizontal = 8.dp,
                                                                                vertical = 4.dp
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        )

                                                        if (roomIndex < rooms.size - 1) {
                                                            HorizontalDivider(
                                                                modifier = Modifier.padding(
                                                                    horizontal = 8.dp
                                                                ),
                                                                thickness = 0.5.dp,
                                                                color = compColors.dividerColor
                                                            )
                                                        }

                                                    }
                                                }
                                            }

                                            if (index < groupedRooms.size - 1) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Jeśli piętro jest wybrane, wyświetlamy prościej
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 350.dp)
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .background(
                                            color = compColors.cardBackground.copy(alpha = 0.05f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            1.dp,
                                            compColors.dividerColor,
                                            RoundedCornerShape(12.dp)
                                        )
                                ) {
                                    items(state.rooms) { room ->
                                        val isSelectedRoom = (state.selectedRoom == room)
                                        DropdownMenuItem(
                                            onClick = {
                                                if (!state.times.isNullOrEmpty()) {
                                                    viewModel.clearTimeSlots()
                                                }
                                                viewModel.changeRoom(room)
                                                isRoomSelectorVisible.value = false
                                                viewModel.updateShowButton(true)
                                                viewModel.saveState()
                                            },
                                            modifier = Modifier.highlightIfSelected(
                                                isSelectedRoom,
                                                compColors
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
                                                            color = if (isSelectedRoom) compColors.primaryText else compColors.secondaryText,
                                                            fontWeight = FontWeight.SemiBold
                                                        )
                                                    )

                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = compColors.accentColor.copy(alpha = 0.2f)
                                                    ) {
                                                        Text(
                                                            text = state.selectedFloorNumber?.getFloorName()
                                                                .orEmpty(),
                                                            style = MaterialTheme.typography.bodySmall.copy(
                                                                color = compColors.accentColor,
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            modifier = Modifier.padding(
                                                                horizontal = 8.dp,
                                                                vertical = 4.dp
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        )
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            thickness = 0.5.dp,
                                            color = compColors.dividerColor
                                        )
                                    }
                                }
                            }
                        }

                    )
                }
            }

            if (state.isButtonVisible && state.selectedRoom != null) {
                item {
                    CheckButton(viewModel)
                }
            }


            // Timeline Section of Room Reservations
            item {
                TimelineSection(state, navigateToReservation)
            }
        } else {

            item {
                /*Box(
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
                }*/
            }

            items(3) {
                Text("")
            }

            item {

                ConfirmEmailPrompt()

            }
        }


    }
}

@Composable
fun Modifier.highlightIfSelected(isSelected: Boolean, compColors: ThemeComponentColors): Modifier {
    return this
        .background(
            color = if (isSelected) compColors.accentColor.copy(alpha = 0.2f) else Color.Transparent,
            shape = RoundedCornerShape(8.dp)
        )
        .border(
            width = if (isSelected) 1.dp else 0.dp,
            color = if (isSelected) compColors.dividerColor else Color.Transparent,
            shape = RoundedCornerShape(8.dp)
        )
        .padding(horizontal = 8.dp, vertical = 8.dp)
}

private fun String?.getFloorName(): String {
    return when (this) {
        null -> "Wybierz piętro"
        "Wszystkie" -> "Wybrano Wszystkie Piętra"
        else -> "Wybrano $this"
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

private fun Room?.getRoomName(): String {
    return when (this) {
        null -> "Wybierz salę"
        else -> "Wybrano Salę " + this.name.split(" ").last()
    }
}

@Composable
fun AvailabilityHeader() {
    val isDarkTheme = isSystemInDarkTheme()
    val colors = if (isDarkTheme) DarkThemeHeaderColors else LightThemeHeaderColors

    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colors.borderColor),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = colors.backgroundGradient,
                        startX = 0f,
                        endX = 2000f
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sprawdź dostępność sali",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = colors.primaryTextColor
                ),
                textAlign = TextAlign.Center
            )
        }
    }
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
fun TimelineSection(state: MainUiState, navigateToReservation: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    val colors: ThemeTimeLineColors =
        if (isDarkTheme) DarkThemeTimeLineColors else LightThemeTimeLineColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (state.times != null) 600.dp else 280.dp)
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
                    ReservationSlotCard(state, time, navigateToReservation)
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
fun ReservationSlotCard(
    state: MainUiState,
    time: Triple<Long, Long, Boolean>,
    onAvailableSlotClick: () -> Unit
) {
    val start = formatTimeFromLong(time.first)
    val end = formatTimeFromLong(time.second)
    val isReserved = time.third


    val userSlot = state.userBookedSlots?.find {
        it.startTime < time.second && it.endTime > time.first
    }

    val lessonSlot = state.lessonBookedSlots?.find {
        overlappingLessons(it.startTime, it.endTime, time.first, time.second)
    }

    val statusColor = if (isReserved) Color(0xFFC62828) else Color(0xFF2E7D32)
    val backgroundColor = if (isReserved) Color(0xFFFDEDEC) else Color(0xFFE8F5E9)

    // Zdefiniuj styl, aby spójnie używać odstępów
    val shape = RoundedCornerShape(12.dp)

    val cardModifier = Modifier
        .fillMaxWidth()
        .clip(shape)
        .padding(horizontal = 8.dp, vertical = 6.dp)
        .shadow(4.dp, shape)
        .background(Color.White, shape) // Biały spód, a kolor stanu w pasku z lewej
        .border(width = 2.dp, color = statusColor, shape = shape)
        .then(if (!isReserved) Modifier.clickable {
            QuickReservation.copy(state, TimeSlot(time.first, time.second))
            onAvailableSlotClick()
        } else Modifier)
        .padding(16.dp) // Wewnętrzny odstęp

    Column(modifier = cardModifier) {
        // Górny wiersz z ikoną i statusem
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isReserved) Icons.Default.EventBusy else Icons.Default.EventAvailable,
                contentDescription = if (isReserved) "Zarezerwowana" else "Dostępna",
                tint = statusColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (isReserved) "Zarezerwowana" else "Dostępna",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Wiersz z godzinami slotu
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "Czas",
                tint = Color(0xFF616161),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$start - $end",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF424242))
            )
        }

        // Jeśli jest userSlot, wyświetl informację o rezerwacji
        if (userSlot != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Zarezerwowane przez: ${userSlot.username}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF616161),
                    fontWeight = FontWeight.Medium
                )
            )

            Text(
                text = "Kontakt: ${userSlot.email}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF616161),
                    fontWeight = FontWeight.Medium
                )
            )



            Text(
                text = "Czas rezerwacji: ${formatTimeFromLong(userSlot.startTime)} - ${
                    formatTimeFromLong(
                        userSlot.endTime
                    )
                }",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF616161))
            )
        }

        if (lessonSlot != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Zajęcia: ${lessonSlot.lessonName}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF616161),
                    fontWeight = FontWeight.Medium
                )
            )

            Text(
                text = "Prowadzący: ${getTeacherName(lessonSlot.email)}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF616161),
                    fontWeight = FontWeight.Medium
                )
            )



            Text(
                text = "Czas: ${formatTimeFromLong(lessonSlot.startTime)} - ${
                    formatTimeFromLong(
                        lessonSlot.endTime
                    )
                }",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF616161))
            )
        }

    }
}

private fun overlappingLessons(
    lessonStart: Long,
    lessonEnd: Long,
    slotStart: Long,
    slotEnd: Long
): Boolean {

    val selectedDate = LocalDate.now()

    // Konwersja milisekund na LocalTime
    val lessonStartTime = Instant.ofEpochMilli(lessonStart).atZone(ZoneOffset.UTC).toLocalTime()
    val lessonEndTime = Instant.ofEpochMilli(lessonEnd).atZone(ZoneOffset.UTC).toLocalTime()
    val slotStartTime = Instant.ofEpochMilli(slotStart).atZone(ZoneOffset.UTC).toLocalTime()
    val slotEndTime = Instant.ofEpochMilli(slotEnd).atZone(ZoneOffset.UTC).toLocalTime()

    // Połączenie z wybraną datą
    val lessonStartAdjusted = LocalDateTime.of(selectedDate, lessonStartTime).toEpochMilli()
    val lessonEndAdjusted = LocalDateTime.of(selectedDate, lessonEndTime).toEpochMilli()
    val slotStartAdjusted = LocalDateTime.of(selectedDate, slotStartTime).toEpochMilli()
    val slotEndAdjusted = LocalDateTime.of(selectedDate, slotEndTime).toEpochMilli()

    // Teraz sprawdzamy nachodzenie się znormalizowanych czasów
    return lessonStartAdjusted < slotEndAdjusted && lessonEndAdjusted > slotStartAdjusted
}

// Funkcja pomocnicza do konwersji LocalDateTime na millis:
private fun LocalDateTime.toEpochMilli(): Long {
    return this.toInstant(ZoneOffset.UTC).toEpochMilli()
}

private fun getTeacherName(email: String): String {
    if (email.substringAfter("@") == "gmail.com") return "Admin"


    val emailTeacherNamePart = email.substringBefore("@")
    val teacherName = emailTeacherNamePart.split(".")


    return teacherName[0].replaceFirstChar { it.uppercase() } +
            " " +
            teacherName[1].replaceFirstChar { it.uppercase() }
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
        .atZone(ZoneOffset.UTC)
        .format(formatter)
}


@Preview(showBackground = true)
@Composable
private fun Avail() {
    RoomAvailabilityView(MainUiState(), RoomAvailabilityViewModel(), navigateToReservation = {})
}

@Composable
fun TopDownElement(
    visible: MutableState<Boolean>,
    imageVector: ImageVector? = null,
    title: String? = "Wybierz datę",
    titleStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Definicja kolorów harmonizujących z tłem
    val titleColor = if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
    val iconTint = if (isDarkTheme) Color(0xFF48C9B0) else Color(0xFF6D4C41)
    val containerBrush = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF34495E), Color(0xFF2C3E50))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFFCE5D1), Color(0xFFFDEDD4))
        )
    }
    val borderColor = if (isDarkTheme) Color(0xFF1ABC9C) else Color(0xFF8D6E63)
    val expandedContainerColor = if (isDarkTheme) Color(0xFF2C3E50) else Color(0xFFFFF8E1)
    val expandedBorderColor = if (isDarkTheme) Color(0xFF48C9B0) else Color(0xFF6D4C41)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(containerBrush)
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







