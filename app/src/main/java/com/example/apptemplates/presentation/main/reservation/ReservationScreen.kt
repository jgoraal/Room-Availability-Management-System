package com.example.apptemplates.presentation.main.reservation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.presentation.main.reservation.components.AvailableRoomsList
import com.example.apptemplates.presentation.main.reservation.components.CalendarViewWithNavigation
import com.example.apptemplates.presentation.main.reservation.components.NumberOfAttendeesView
import com.example.apptemplates.presentation.main.reservation.components.TimePickerView
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


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

@Composable
fun ReservationScreen(
    viewModel: ReservationViewModel = ReservationViewModel(),
    navController: NavController,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val state by viewModel.state.collectAsState()


    /*Scaffold(topBar = {
        TopBarPreview() // Your existing top bar
    }, bottomBar = {
        BottomBar(navController) // Your existing bottom bar
    }, content = { padding ->
        ReservationView(state, viewModel, modifier.padding(padding))
    })*/
}

@Composable
fun ReservationView(
    state: MainUiState, viewModel: ReservationViewModel, modifier: Modifier = Modifier
) {
    // Main LazyColumn for the form elements, excluding the room list
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Existing items like calendar, time picker, etc.
        item { Text(text = "Złóż rezerwację", style = MaterialTheme.typography.headlineMedium) }
        item { CalendarPicker(state, viewModel) }
        item { TimePicker(state, viewModel, label = "Start Time") }
        item { NumberOfAttendeesSelector(viewModel, state) }
        item { CalendarEndDatePicker(viewModel = viewModel, state = state) }
        item { AdditionalFiltersPicker(viewModel, state) }
        item { HorizontalDivider() }
        item { SelectedData(state) }
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { viewModel.findRooms() },
                    modifier = Modifier
                        .width(260.dp)
                        .height(50.dp)
                ) {
                    Text(text = "Szukaj dostępnych sal")
                }
            }
        }
        item { AvailableRoomsList(state) }
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
                Text(text = "Wybierz piętro:", style = MaterialTheme.typography.bodyMedium)
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
        EquipmentType.COMPUTER -> "Komputer"
        EquipmentType.PROJECTOR -> "Projektor"
        EquipmentType.WHITEBOARD -> "Tablica"
    }
}


// Helper function for floor picker dropdown menu
@Composable
fun DropdownMenuFloorPicker(selectedFloor: Int, onFloorSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
            .clickable { expanded = !expanded }, contentAlignment = Alignment.Center
    ) {
        Text(text = "Piętro: $selectedFloor", modifier = Modifier.padding(8.dp))

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            (1..3).forEach { floor ->
                DropdownMenuItem(onClick = {
                    onFloorSelected(floor)
                    expanded = false
                }, text = { Text(text = floor.toString()) })
            }
        }
    }
}


@Composable
fun SelectedData(state: MainUiState) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        // Date Row
        if (state.selectedDate != null) DataRow(
            icon = Icons.Default.CalendarToday,
            label = "Wybrana data",
            value = state.selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) ?: "Brak"
        )

        // Time Row
        if (state.selectedTime != null) DataRow(
            icon = Icons.Default.Schedule,
            label = "Wybrana godzina",
            value = state.selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        )

        if (state.selectedEndTime != null) DataRow(
            icon = Icons.Default.Schedule,
            label = "Wybrana godzina zakończenia",
            value = state.selectedEndTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        )

        // Attendees Row
        if (state.selectedAttendees > 0) DataRow(
            icon = Icons.Default.People,
            label = "Liczba uczestników",
            value = state.selectedAttendees.toString()
        )

        // Recurring Row
        DataRow(
            icon = Icons.Default.EventRepeat,
            label = "Cykliczność",
            value = if (state.isRecurring) "Tak" else "Nie"
        )

        // Recurring Frequency Row
        if (state.isRecurring) {
            DataRow(
                icon = Icons.Default.EventRepeat,
                label = "Częstotliwość",
                value = translateRecurringFrequency(state.recurringFrequency),
            )

            // End Date Row
            DataRow(
                icon = Icons.Default.CalendarToday,
                label = "Data zakończenia",
                value = state.endRecurrenceDate?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    ?: "Brak"
            )
        }
    }
}


fun translateRecurringFrequency(frequency: RecurrenceFrequency?): String {
    return when (frequency) {
        RecurrenceFrequency.WEEKLY -> "Tygodniowo"
        RecurrenceFrequency.BIWEEKLY -> "Co dwa tygodnie"
        RecurrenceFrequency.MONTHLY -> "Miesięcznie"
        else -> "Brak"
    }
}

@Composable
fun DataRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
fun TimePicker(
    state: MainUiState, viewModel: ReservationViewModel, label: String
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
                    TimePickerView(viewModel, "Wybierz godzinę rozpoczęcia")
                    TimePickerView(viewModel, "Wybierz godzinę zakończenia")
                }
            }
        }
    }
}


@Composable
fun NumberOfAttendeesSelector(viewModel: ReservationViewModel, state: MainUiState) {
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
            NumberOfAttendeesView(viewModel, state)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(state: MainUiState, viewModel: ReservationViewModel) {
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
                    CalendarViewWithNavigation(viewModel, startFromSunday = false)
                }

            }

        }
    }


}


@Composable
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
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        )
                        .border(
                            2.dp,
                            Color(0xFF9B5DE5),
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
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
}



