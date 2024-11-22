package com.example.apptemplates.presentation.main.reservation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RoomPreferences
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.presentation.main.reservation.components.AvailableRoomsList
import com.example.apptemplates.presentation.main.reservation.components.CalendarViewWithNavigation
import com.example.apptemplates.presentation.main.reservation.components.DialogRoomReservation
import com.example.apptemplates.presentation.main.reservation.components.NumberOfAttendeesView
import com.example.apptemplates.presentation.main.reservation.components.TimePickerView
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.ui.theme.getContentBackGround
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


data class ReservationState(
    val selectedDate: LocalDate? = null, // Data rozpoczecia i zakonczenia jezeli nie jest cykliczna
    val selectedTime: LocalTime? = null, // Godzina rozpoczecia
    val selectedEndTime: LocalTime? = null, // Godzina zakonczenia

    val selectedAttendees: Int = 1, // Liczba uczestnikow

    val isRecurring: Boolean = false, // Czy jest cykliczna
    val recurringFrequency: RecurrenceFrequency? = null, // Rodzaj cykliczonosci
    val endRecurrenceDate: LocalDate? = null,   // Zakonczenie cyklu

    val selectedFloor: Int = 1, // Wybrany piętro
    val selectedEquipment: List<EquipmentType> = emptyList(), // Wybrane conflictos

    val screenState: ScreenState = ScreenState.Idle, // Stan ekranu
    val availableRooms: List<Room> = emptyList()
)


@Preview(showBackground = true)
@Composable
fun ReservationView(
    state: MainUiState = MainUiState(),
    viewModel: ReservationViewModel = ReservationViewModel(),
    padding: PaddingValues = PaddingValues(0.dp),
    navigateOnSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {


    DialogRoomReservation(
        state,
        viewModel,
        navigateOnSuccess
    ) { viewModel.changeSelectedRoom(null) }


    val showTimePicker = remember { mutableStateOf(false) }
    val showAttendeesPicker = remember { mutableStateOf(false) }
    val showRecurringPicker = remember { mutableStateOf(false) }
    val showOtherFiltersPicker = remember { mutableStateOf(false) }

    // Main LazyColumn for the form elements, excluding the room list
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(getContentBackGround())
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Existing items like calendar, time picker, etc.
        item { ReservationHeader() }
        item {
            CalendarPicker(
                state,
                viewModel,
            ) { showTimePicker.value = true }
        }

        if (showTimePicker.value) {
            item { TimePicker(state, viewModel) { showAttendeesPicker.value = true } }
        }

        if (showAttendeesPicker.value) {
            item {
                NumberOfAttendeesSelector(viewModel, state) {
                    showRecurringPicker.value = true
                    showOtherFiltersPicker.value = true
                }
            }
        }

        if (showRecurringPicker.value && viewModel.canUserMakeRecurringReservation()) {
            item { CalendarEndDatePicker(viewModel = viewModel, state = state) }
        }

        if (showOtherFiltersPicker.value) {
            item { AdditionalFiltersPicker(viewModel, state) }
        }


        item { HorizontalDivider() }
        if (showTimePicker.value) {
            item {
                SelectedData(
                    viewModel,
                    state,
                    showTimePicker.value,
                    showAttendeesPicker.value,
                    showRecurringPicker.value,
                    showOtherFiltersPicker.value
                )
            }
        }

        if (showAttendeesPicker.value) {
            item {
                FindAvailableRoomsButton(viewModel)
            }
        }

        item { AvailableRoomsList(state, viewModel) }
    }

}


@Composable
fun FindAvailableRoomsButton(viewModel: ReservationViewModel = ReservationViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { viewModel.findRooms() },
            modifier = Modifier
                .width(280.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                ),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent // Use gradient as background
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Szukaj dostępnych sal",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }

}

@Composable
fun ReservationHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFF8ECD1), // Warm beige tone at the left
                        Color(0xFFEED9A7)  // Soft, golden beige at the right
                    )
                )
            )
            .border(
                BorderStroke(1.dp, Color(0xFFD8C3A5)),
                RoundedCornerShape(16.dp)
            ) // Subtle border for elegance
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Złóż rezerwację",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF5D4037), // Rich brown for text color
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            modifier = Modifier.align(Alignment.CenterStart),
        )
    }
}


@Composable
fun AdditionalFiltersPicker(viewModel: ReservationViewModel, state: MainUiState) {
    var isAdditionalFiltersVisible by remember { mutableStateOf(false) }
    val availableEquipment = listOf(
        EquipmentType.COMPUTER, EquipmentType.PROJECTOR, EquipmentType.WHITEBOARD
    )  // Available equipment options

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Filter header with clickable behavior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isAdditionalFiltersVisible = !isAdditionalFiltersVisible },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterAlt, contentDescription = "FilterAlt Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Wybierz dodatkowe wymagania", style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isAdditionalFiltersVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Toggle Filters"
            )
        }

        // Animated visibility for filters
        AnimatedVisibility(
            visible = isAdditionalFiltersVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                // Floor picker
                Text(text = "Wybierz piętro", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))

                // Dropdown for selecting the floor
                DropdownMenuFloorPicker(selectedFloor = state.selectedFloor) { selectedFloor ->
                    viewModel.updateSelectedFloor(selectedFloor)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Equipment picker
                Text(text = "Wybierz wyposażenie:", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))

                // Equipment selection using checkboxes
                availableEquipment.forEach { equipment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val updatedEquipment =
                                    if (state.selectedEquipment.contains(equipment)) {
                                        state.selectedEquipment.minus(equipment)
                                    } else {
                                        state.selectedEquipment.plus(equipment)
                                    }
                                viewModel.updateSelectedEquipment(updatedEquipment)
                            }, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = state.selectedEquipment.contains(equipment),
                            onCheckedChange = { isChecked ->
                                val updatedEquipment = if (isChecked) {
                                    state.selectedEquipment.plus(equipment)
                                } else {
                                    state.selectedEquipment.minus(equipment)
                                }
                                viewModel.updateSelectedEquipment(updatedEquipment)
                            })
                        Text(text = equipment.getNameInPolish())
                    }
                }
            }
        }
    }
}

fun EquipmentType.getNameInPolish(): String {
    return when (this) {
        EquipmentType.COMPUTER -> "Komputery"
        EquipmentType.PROJECTOR -> "Projektor"
        EquipmentType.WHITEBOARD -> "Tablica"
    }
}


// Helper function for floor picker dropdown menu
@Composable
fun DropdownMenuFloorPicker(selectedFloor: Int?, onFloorSelected: (Int?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val floors = mapOf(
        "Wszystkie" to null,
        "Parter" to 1,
        "1 piętro" to 2,
        "2 piętro" to 3,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
            .clickable { expanded = !expanded }, contentAlignment = Alignment.Center
    ) {
        Text(text = selectedFloor.getFloorName(), modifier = Modifier.padding(8.dp))

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            floors.forEach { (floor, floorNumber) ->
                DropdownMenuItem(onClick = {
                    onFloorSelected(floorNumber)
                    expanded = false
                }, text = { Text(text = floor.toString()) })
            }
        }
    }
}


@Composable
fun SelectedData(viewModel: ReservationViewModel, state: MainUiState, vararg show: Boolean) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Date Row (Start and End Date)
        if (state.selectedDate != null) {
            DataGroup(
                items = listOf(
                    DataRowItem(
                        icon = Icons.Default.EventAvailable,
                        label = "Data rozpoczęcia",
                        value = state.selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    ),
                    DataRowItem(
                        icon = Icons.Default.EventBusy,
                        label = "Data zakończenia",
                        value = (state.endRecurrenceDate ?: state.selectedDate)
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    )
                )
            )
        }

        // Time Rows (Start and End Time in one row)
        if ((state.selectedTime != null || state.selectedEndTime != null) && show[0]) {
            DataGroup(
                items = listOf(
                    DataRowItem(
                        icon = Icons.Default.Schedule,
                        label = "Czas rozpoczęcia",
                        value = state.selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                            ?: ""
                    ),
                    DataRowItem(
                        icon = Icons.Default.Schedule,
                        label = "Czas zakończenia",
                        value = state.selectedEndTime?.format(DateTimeFormatter.ofPattern("HH:mm"))
                            ?: ""
                    )
                )
            )
        }

        // Recurring and Frequency Rows
        if (show[2] && state.isRecurring && viewModel.canUserMakeRecurringReservation()) {


            DataGroup(
                items = listOf(
                    DataRowItem(
                        icon = Icons.Default.EventRepeat,
                        label = "Cykliczność",
                        value = if (state.isRecurring) "Tak" else "Nie"
                    ),
                    DataRowItem(
                        icon = Icons.Default.EventRepeat,
                        label = "Częstotliwość",
                        value = translateRecurringFrequency(
                            state.recurringFrequency ?: RecurrenceFrequency.WEEKLY
                        )
                    )
                )
            )
        }

        // Attendees Row
        if (state.selectedAttendees > 0 && show[1]) {
            DataRow(
                icon = Icons.Default.People,
                label = "Liczba uczestników",
                value = state.selectedAttendees.toString()
            )
        }


        // Floor and Equipment Rows
        if (show[3]) {
            DataRow(
                icon = Icons.Default.Business,
                label = "Piętro",
                value = state.selectedFloor.getFloorName()
            )

            DataRow(
                icon = Icons.Default.RoomPreferences,
                label = "Udogodnienia",
                value = state.selectedEquipment.joinToString(", ") { it.getNameInPolish() }
                    .ifEmpty { "Nie wybrano" }
            )
        }
    }
}

private fun Int?.getFloorName(): String {
    return when (this) {
        1 -> "Parter"
        2 -> "1. Piętro"
        3 -> "2. Piętro"
        null -> "Wszystkie"
        else -> "Nie wybrano"
    }
}

@Composable
fun DataGroup(items: List<DataRowItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = "${item.label} Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun DataRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.05f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

data class DataRowItem(val icon: ImageVector, val label: String, val value: String)


fun translateRecurringFrequency(frequency: RecurrenceFrequency?): String {
    return when (frequency) {
        RecurrenceFrequency.WEEKLY -> "Tygodniowo"
        RecurrenceFrequency.BIWEEKLY -> "Co dwa tygodnie"
        RecurrenceFrequency.MONTHLY -> "Miesięcznie"
        else -> "Brak"
    }
}


@Composable
fun TimePicker(
    state: MainUiState, viewModel: ReservationViewModel, onSuccess: () -> Unit
) {
    var isTimePickerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isTimePickerVisible = !isTimePickerVisible },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime, contentDescription = "Calendar Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Wybierz godzinę", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))

            // Dropdown icon based on the state of the calendar visibility
            Icon(
                imageVector = if (isTimePickerVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Toggle Calendar"
            )
        }

        // Animated visibility for the calendar with background and rounded corners
        AnimatedVisibility(
            visible = isTimePickerVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            LazyRow {
                item {
                    TimePickerView(viewModel, state, "Wybierz godzinę rozpoczęcia")
                    TimePickerView(viewModel, state, "Wybierz godzinę zakończenia", onSuccess)
                }
            }
        }
    }
}


@Composable
fun NumberOfAttendeesSelector(
    viewModel: ReservationViewModel,
    state: MainUiState,
    onSuccess: () -> Unit
) {
    var isNumberOfAttendeesVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isNumberOfAttendeesVisible = !isNumberOfAttendeesVisible },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Groups, contentDescription = "Calendar Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Wybierz liczbę uczestników", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f))

            // Dropdown icon based on the state of the calendar visibility
            Icon(
                imageVector = if (isNumberOfAttendeesVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Toggle Calendar"
            )
        }

        // Animated visibility for the calendar with background and rounded corners
        AnimatedVisibility(
            visible = isNumberOfAttendeesVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            NumberOfAttendeesView(viewModel, state, onSuccess)
        }
    }
}


@Composable
fun CalendarPicker(state: MainUiState, viewModel: ReservationViewModel, onSuccess: () -> Unit) {
    var isCalendarVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Date picker header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isCalendarVisible = !isCalendarVisible },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendar Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Wybierz datę", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isCalendarVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Toggle Calendar"
            )
        }

        // Animated visibility for the calendar
        AnimatedVisibility(
            visible = isCalendarVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF4F2FF), shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            2.dp, Color(0xFF9B5DE5), shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .padding(16.dp)
                ) {
                    CalendarViewWithNavigation(viewModel, startFromSunday = false, onSuccess)
                }

            }

        }
    }


}


/*@Composable
fun CalendarEndDatePicker(
    modifier: Modifier = Modifier, viewModel: ReservationViewModel, state: MainUiState
) {
    var isCalendarVisible by remember { mutableStateOf(false) }
    val showEndDatePicker by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Date picker header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isCalendarVisible = !isCalendarVisible },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EventRepeat, contentDescription = "Calendar Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Wybierz cykliczność", style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isCalendarVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Toggle Calendar"
            )
        }

        // Animated visibility for the calendar
        AnimatedVisibility(
            visible = isCalendarVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFF4F2FF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            2.dp,
                            Color(0xFF9B5DE5),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        // Recurrence checkbox
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Czy jest to rezerwacja cykliczna",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Checkbox(checked = state.isRecurring, onCheckedChange = { isChecked ->
                                viewModel.changeRecurring(isChecked)
                            })
                        }

                        if (state.isRecurring) {
                            // Recurrence frequency selection
                            Column(
                                modifier = Modifier.wrapContentHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        RadioButton(selected = state.recurringFrequency == RecurrenceFrequency.WEEKLY,
                                            onClick = {
                                                viewModel.changeFrequency(
                                                    RecurrenceFrequency.WEEKLY
                                                )
                                            })
                                        Text(
                                            text = "Tygodniowo",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        RadioButton(selected = state.recurringFrequency == RecurrenceFrequency.BIWEEKLY,
                                            onClick = {
                                                viewModel.changeFrequency(
                                                    RecurrenceFrequency.BIWEEKLY
                                                )
                                            })
                                        Text(
                                            text = "Co dwa tygodnie",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        RadioButton(selected = state.recurringFrequency == RecurrenceFrequency.MONTHLY,
                                            onClick = {
                                                viewModel.changeFrequency(
                                                    RecurrenceFrequency.MONTHLY
                                                )
                                            })
                                        Text(
                                            text = "Miesięcznie",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // End date selector
                            if (showEndDatePicker) {
                                Text(
                                    text = "Wybierz datę zakończenia",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Button(onClick = {}) {
                                    val endDate = null
                                    Text(text = endDate?.toString() ?: "Wybierz datę zakończenia")
                                }
                            }
                        }
                    }
                }
            }


        }
    }
}*/


@Composable
fun CalendarEndDatePicker(
    viewModel: ReservationViewModel,
    state: MainUiState,
    modifier: Modifier = Modifier
) {
    var isCalendarVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        DatePickerHeader(isCalendarVisible) { isCalendarVisible = !isCalendarVisible }
        RecurrencePickerContent(
            isVisible = isCalendarVisible,
            viewModel = viewModel,
            state = state
        )
    }
}

@Composable
fun DatePickerHeader(isVisible: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.EventRepeat,
            contentDescription = "Calendar Icon",
            tint = Color(0xFF9B5DE5)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Wybierz cykliczność",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = if (isVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = "Toggle Calendar"
        )
    }
}

@Composable
fun RecurrencePickerContent(
    isVisible: Boolean,
    viewModel: ReservationViewModel,
    state: MainUiState
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(color = Color(0xFFF4F2FF), shape = RoundedCornerShape(12.dp))
                .border(2.dp, Color(0xFF9B5DE5), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            RecurrenceCheckbox(state.isRecurring) {
                viewModel.changeRecurring(it)
            }

            if (state.isRecurring) {
                RecurrenceFrequencyOptions(state.recurringFrequency ?: RecurrenceFrequency.WEEKLY) {
                    viewModel.changeFrequency(it)
                }

                Spacer(modifier = Modifier.height(16.dp))
                EndDateSelector(viewModel, state)
            }
        }
    }
}

@Composable
fun RecurrenceCheckbox(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Czy jest to rezerwacja cykliczna",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun RecurrenceFrequencyOptions(
    selectedFrequency: RecurrenceFrequency,
    onSelect: (RecurrenceFrequency) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FrequencyOption("Tygodniowo", selectedFrequency == RecurrenceFrequency.WEEKLY) {
            onSelect(RecurrenceFrequency.WEEKLY)
        }
        FrequencyOption("Co dwa tygodnie", selectedFrequency == RecurrenceFrequency.BIWEEKLY) {
            onSelect(RecurrenceFrequency.BIWEEKLY)
        }
        FrequencyOption("Miesięcznie", selectedFrequency == RecurrenceFrequency.MONTHLY) {
            onSelect(RecurrenceFrequency.MONTHLY)
        }
    }
}

@Composable
fun FrequencyOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RadioButton(selected = isSelected, onClick = onClick)
        Text(text = text, style = MaterialTheme.typography.labelMedium)
    }
}


@Composable
fun EndDateSelector(viewModel: ReservationViewModel, state: MainUiState) {
    var selectedDuration by remember { mutableIntStateOf(1) }
    val step = 1
    val endDate = remember(selectedDuration, state.recurringFrequency) {
        calculateFormattedEndDate(state, viewModel, selectedDuration)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wybierz datę zakończenia",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { if (selectedDuration > step) selectedDuration -= step }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease duration")
            }

            Text(
                text = "${
                    getDuration(
                        state.recurringFrequency ?: RecurrenceFrequency.WEEKLY,
                        selectedDuration
                    )
                } ${getDurationLabel(state.recurringFrequency ?: RecurrenceFrequency.WEEKLY)}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )

            IconButton(onClick = { selectedDuration += step }) {
                Icon(Icons.Default.Add, contentDescription = "Increase duration")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Data zakończenia",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = endDate,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 6.dp, horizontal = 12.dp)
        )
    }
}

// Adjust the duration increment based on recurrence frequency
fun getStepValue(frequency: RecurrenceFrequency): Int {
    return when (frequency) {
        RecurrenceFrequency.WEEKLY -> 1
        RecurrenceFrequency.BIWEEKLY -> 1
        RecurrenceFrequency.MONTHLY -> 1
    }
}

fun getDuration(frequency: RecurrenceFrequency, duration: Int): String {
    return when (frequency) {
        RecurrenceFrequency.WEEKLY -> duration.toString()
        RecurrenceFrequency.BIWEEKLY -> (duration * 2).toString()
        RecurrenceFrequency.MONTHLY -> duration.toString()
    }
}

@Composable
fun getDurationLabel(frequency: RecurrenceFrequency): String {
    return when (frequency) {
        RecurrenceFrequency.WEEKLY -> "Tygodnie"
        RecurrenceFrequency.BIWEEKLY -> "Tygodnie"
        RecurrenceFrequency.MONTHLY -> "Miesiące"
    }
}

// Format end date as dd.MM.yyyy
fun calculateFormattedEndDate(
    state: MainUiState,
    viewModel: ReservationViewModel,
    duration: Int
): String {
    val frequency = state.recurringFrequency ?: RecurrenceFrequency.WEEKLY
    val startDate = state.selectedDate?.atTime(LocalTime.MAX)?.toLocalDate() ?: LocalDate.now()
    val endDate = when (frequency) {
        RecurrenceFrequency.WEEKLY -> startDate.plusWeeks(duration.toLong())
        RecurrenceFrequency.BIWEEKLY -> startDate.plusWeeks(duration.toLong() * 2)
        RecurrenceFrequency.MONTHLY -> startDate.plusMonths(duration.toLong())
    }

    viewModel.changeEndDate(endDate)

    return formatEndDateWithDay(endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
}


@Composable
fun EndDateDisplay(endDate: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "End Date Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Data zakończenia",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = formatEndDateWithDay(endDate),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            )
        }
    }
}

fun formatEndDateWithDay(endDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("pl", "PL"))
    val date = LocalDate.parse(endDate, formatter)
    val displayFormatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale("pl", "PL"))
    return date.format(displayFormatter)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}




