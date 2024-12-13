package com.example.apptemplates.presentation.main.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.presentation.main.reservation.components.AdditionalFiltersPicker
import com.example.apptemplates.presentation.main.reservation.components.AvailableRoomsList
import com.example.apptemplates.presentation.main.reservation.components.CalendarViewWithNavigation
import com.example.apptemplates.presentation.main.reservation.components.DialogRoomReservation
import com.example.apptemplates.presentation.main.reservation.components.FindAvailableRoomsButton
import com.example.apptemplates.presentation.main.reservation.components.NumberOfAttendeesSelector
import com.example.apptemplates.presentation.main.reservation.components.RecurrencePickerContent
import com.example.apptemplates.presentation.main.reservation.components.SelectedData
import com.example.apptemplates.presentation.main.reservation.components.TimePickerOption
import com.example.apptemplates.presentation.main.room_availability.TopDownElement
import com.example.apptemplates.presentation.main.room_availability.objects.QuickReservation
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
        state, viewModel, navigateOnSuccess
    ) { viewModel.changeSelectedRoom(null) }


    viewModel.checkIfQuickReservationIsReady()

    if (QuickReservation.getStartTime() != null) {
        viewModel.updateVisibility(true)
    }


    // Main LazyColumn for the form elements, excluding the room list
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(getContentBackGround())
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (state.availableRooms.isEmpty()) 16.dp else 8.dp)
    ) {
        // Existing items like calendar, time picker, etc.
        item { /*ReservationHeader()*/ }
        item {
            CalendarPickerInTopDown(
                state,
                viewModel,
            ) {
                if (state.endRecurrenceDate != null) {
                    viewModel.updateEndRecurrenceDate()
                }

                viewModel.updateShowTimePicker(true)

                if (state.availableRooms.isNotEmpty()) {
                    viewModel.clearAvailableRooms()
                }

            }
        }

        if (state.showTimePicker) {
            item {
                TimePickerInTopDown(state, viewModel) {
                    viewModel.updateShowRecurringPicker(true)
                    viewModel.updateShowAttendeesPicker(true)
                    viewModel.updateShowOtherFiltersPicker(true)

                    if (state.availableRooms.isNotEmpty()) {
                        viewModel.clearAvailableRooms()
                    }
                }
            }
        }

        if (state.showRecurringPicker && viewModel.canUserMakeRecurringReservation()) {
            item { CalendarEndDatePicker(viewModel = viewModel, state = state) }
        }

        if (state.showAttendeesPicker) {
            item {
                NumberOfAttendeesSelector(viewModel, state) {
                    if (state.availableRooms.isNotEmpty()) {
                        viewModel.clearAvailableRooms()
                    }

                    viewModel.updateShowRecurringPicker(true)
                    viewModel.updateShowOtherFiltersPicker(true)
                }
            }
        }

        if (state.showOtherFiltersPicker) {
            item { AdditionalFiltersPicker(viewModel, state) }
        }


        item { HorizontalDivider() }
        if (state.showTimePicker) {
            item {
                SelectedData(
                    viewModel,
                    state,
                    state.showTimePicker,
                    state.showAttendeesPicker,
                    state.showRecurringPicker,
                    state.showOtherFiltersPicker
                )
            }
        }

        if (state.showAttendeesPicker) {
            item {
                FindAvailableRoomsButton(viewModel, isSystemInDarkTheme())
            }
        }

        item { AvailableRoomsList(state, viewModel) }
    }

}


/*@Composable
fun TimePicker(
    state: MainUiState, viewModel: ReservationViewModel, onSuccess: () -> Unit
) {
    var isTimePickerVisible by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(state.selectedTime ?: getPreferredStartTime(state)) }
    var endTime by remember {
        mutableStateOf(
            state.selectedEndTime ?: getPreferredEndTime(
                startTime, state
            )
        )
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
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
            Text(
                text = state.selectedTime.getTimesText(state.selectedEndTime),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isTimePickerVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Toggle Calendar"
            )
        }



        AnimatedVisibility(
            visible = isTimePickerVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                // Walidacja przy otwarciu
                if (isTimePickerVisible && state.selectedTime == null && state.selectedEndTime == null) {
                    if (!validateTimeDifference(startTime, endTime)) {
                        errorMessage = "Różnica między godzinami musi wynosić co najmniej 90 minut."
                    } else {
                        errorMessage = null
                        viewModel.changeReservationTimes(startTime, endTime)
                        onSuccess()
                    }
                }

                // Wyświetlanie komunikatu błędu
                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                TimePickerOption(label = "Godzina rozpoczęcia",
                    label2 = "Godzina zakończenia",
                    initialTime = startTime,
                    initialEndTime = endTime,
                    state = state,
                    onStartTimeSelected = { newTime ->
                        val newEndTime = getPreferredEndTime(newTime, state)
                        if (validateTimeDifference(newTime, endTime)) {
                            startTime = newTime
                            endTime = newEndTime
                            viewModel.changeTime(newTime)
                            viewModel.changeEndTime(newEndTime)
                            errorMessage = null
                            onSuccess()
                        } else {
                            errorMessage = "Niepoprawna godzina rozpoczęcia!"
                        }
                    },
                    onEndTimeSelected = { newTime ->
                        if (validateTimeDifference(startTime, newTime)) {
                            endTime = newTime
                            viewModel.changeEndTime(newTime)
                            errorMessage = null
                        } else {
                            errorMessage = "Niepoprawna godzina zakończenia!"
                        }
                    })


            }
        }
    }
}*/


@Composable
fun TimePickerInTopDown(
    state: MainUiState,
    viewModel: ReservationViewModel,
    onSuccess: () -> Unit
) {
    val isTimePickerVisible = remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(state.selectedTime ?: getPreferredStartTime(state)) }
    var endTime by remember {
        mutableStateOf(
            state.selectedEndTime ?: getPreferredEndTime(
                startTime, state
            )
        )
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    TopDownElement(
        visible = isTimePickerVisible,
        imageVector = Icons.Default.AccessTime,
        title = state.selectedTime.getTimesText(state.selectedEndTime)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            // Walidacja przy otwarciu
            if (state.selectedTime == null && state.selectedEndTime == null) {
                if (!validateTimeDifference(startTime, endTime)) {
                    errorMessage = "Różnica między godzinami musi wynosić co najmniej 90 minut."
                } else {
                    errorMessage = null
                    viewModel.changeReservationTimes(startTime, endTime)
                    onSuccess()
                }
            }

            // Wyświetlanie komunikatu błędu
            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            TimePickerOption(
                label = "Godzina rozpoczęcia",
                label2 = "Godzina zakończenia",
                initialTime = startTime,
                initialEndTime = endTime,
                state = state,
                onStartTimeSelected = { newTime ->
                    val newEndTime = getPreferredEndTime(newTime, state)
                    if (validateTimeDifference(newTime, endTime)) {
                        startTime = newTime
                        endTime = newEndTime
                        viewModel.changeTime(newTime)
                        viewModel.changeEndTime(newEndTime)
                        errorMessage = null
                        onSuccess()
                    } else {
                        errorMessage = "Niepoprawna godzina rozpoczęcia!"
                    }
                },
                onEndTimeSelected = { newTime ->
                    if (validateTimeDifference(startTime, newTime)) {
                        endTime = newTime
                        viewModel.changeEndTime(newTime)
                        errorMessage = null
                    } else {
                        errorMessage = "Niepoprawna godzina zakończenia!"
                    }
                }
            )
        }
    }
}


fun validateTimeDifference(startTime: LocalTime, endTime: LocalTime): Boolean {
    val difference = java.time.Duration.between(startTime, endTime).toMinutes()
    return difference >= 90 && startTime.isBefore(endTime)
}


@Composable
fun CalendarPickerInTopDown(
    state: MainUiState,
    viewModel: ReservationViewModel,
    onSuccess: () -> Unit
) {
    val isCalendarVisible = remember { mutableStateOf(false) }

    TopDownElement(
        visible = isCalendarVisible,
        imageVector = Icons.Default.CalendarMonth,
        title = state.selectedDate.getTitleText()
    ) {
        // Zawartość CalendarPicker
        //CalendarPickerContent(state, viewModel, onSuccess)
        CalendarViewWithNavigation(viewModel, startFromSunday = false, onSuccess)
    }
}


private fun LocalDate?.getTitleText(): String {
    return when (this) {
        null -> "Wybierz datę"
        else -> "Wybrana data " + this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}

private fun LocalTime?.getTimesText(endTime: LocalTime?): String {
    return when (this) {
        null -> "Wybierz godziny"
        else -> "Wybrane godziny " + this.format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + endTime?.format(
            DateTimeFormatter.ofPattern("HH:mm")
        )
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


/*@Composable
fun CalendarEndDatePicker(
    viewModel: ReservationViewModel, state: MainUiState, modifier: Modifier = Modifier
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
        DatePickerHeader(state, isCalendarVisible) { isCalendarVisible = !isCalendarVisible }
        RecurrencePickerContent(
            isVisible = isCalendarVisible, viewModel = viewModel, state = state
        )
    }
}*/

@Composable
fun CalendarEndDatePicker(
    viewModel: ReservationViewModel, state: MainUiState, modifier: Modifier = Modifier
) {
    val isVisible = remember { mutableStateOf(false) }

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


    TopDownElement(
        visible = isVisible,
        imageVector = Icons.Default.EventRepeat,
        title = state.endRecurrenceDate.getRecurrenceText(state.recurringFrequency),
        titleStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        content = {
            RecurrencePickerContent(
                isVisible = isVisible.value,
                viewModel = viewModel,
                state = state,
                titleColor = titleColor,
                iconTint = iconTint,
                containerBrush = containerBrush,
                borderColor = borderColor,
                expandedContainerColor = expandedContainerColor,
                expandedBorderColor = expandedBorderColor
            )
        }
    )
}

/*
@Composable
fun DatePickerHeader(state: MainUiState, isVisible: Boolean, onClick: () -> Unit) {
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
            text = state.endRecurrenceDate.getRecurrenceText(state.recurringFrequency),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = if (isVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = "Toggle Calendar"
        )
    }
}*/


private fun LocalDate?.getRecurrenceText(freq: RecurrenceFrequency?): String {
    if (freq == null) return "Wybierz powtarzalność"

    return when (this) {
        null -> "Wybierz powtarzalność"
        else -> "${freq.getFrequencyName()} do ${this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}"
    }
}

private fun RecurrenceFrequency.getFrequencyName(): String {
    return when (this) {
        RecurrenceFrequency.WEEKLY -> "Tygodniowo"
        RecurrenceFrequency.BIWEEKLY -> "Co 2 tygodnie"
        RecurrenceFrequency.MONTHLY -> "Miesięcznie"
    }
}

/*@Composable
fun FrequencyOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RadioButton(selected = isSelected, onClick = onClick)
        Text(text = text, style = MaterialTheme.typography.labelMedium)
    }
}*/


/*// Adjust the duration increment based on recurrence frequency
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
}*/

// Format end date as dd.MM.yyyy
fun calculateFormattedEndDate(
    state: MainUiState, viewModel: ReservationViewModel, duration: Int
): String {
    val frequency = state.recurringFrequency ?: RecurrenceFrequency.WEEKLY
    val startDate = state.selectedDate?.atTime(LocalTime.MAX)?.toLocalDate() ?: LocalDate.now()
    val endDate = when (frequency) {
        RecurrenceFrequency.WEEKLY -> startDate.plusWeeks(duration.toLong())
        RecurrenceFrequency.BIWEEKLY -> startDate.plusWeeks(duration.toLong() * 2)
        RecurrenceFrequency.MONTHLY -> startDate.plusWeeks(duration.toLong() * 4)
    }

    viewModel.changeEndDate(endDate)

    return formatEndDateWithDay(endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
}


fun formatEndDateWithDay(endDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("pl", "PL"))
    val date = LocalDate.parse(endDate, formatter)
    val displayFormatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale("pl", "PL"))
    return date.format(displayFormatter)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}


fun EquipmentType.getNameInPolish(): String {
    return when (this) {
        EquipmentType.COMPUTER -> "Komputery"
        EquipmentType.PROJECTOR -> "Projektor"
        EquipmentType.WHITEBOARD -> "Tablica"
    }
}


fun Int?.getFloorName(): String {
    return when (this) {
        1 -> "Parter"
        2 -> "1. Piętro"
        3 -> "2. Piętro"
        null -> "Wszystkie"
        else -> "Nie wybrano"
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


private fun getPreferredStartTime(state: MainUiState): LocalTime {
    if (state.selectedTime != null) return state.selectedTime

    val now = LocalTime.now()

    // Jeśli godzina jest poza zakresem pracy (przed 8:00 lub po 21:00)
    if (now.hour < 8 || now.hour >= 21) {
        return LocalTime.of(8, 0)
    }

    // Jeśli godzina jest w trakcie pracy
    return when (now.minute) {
        in 0..19 -> LocalTime.of(now.hour + 1, 0)
        in 20..40 -> LocalTime.of(now.hour + 1, 30)
        else -> LocalTime.of(now.hour + 1, 45)
    }
}


private fun getPreferredEndTime(startTime: LocalTime, state: MainUiState): LocalTime {
    if (state.selectedEndTime != null) return state.selectedEndTime

    // Dodaj jedną godzinę i zaokrąglij do najbliższych 30 minut
    val endHour = startTime.hour + 1
    val roundedMinute = when (startTime.minute) {
        in 0..29 -> 30
        in 30..44 -> 0
        else -> 15
    }

    val end = startTime.plusMinutes(90)

    // Jeśli godzina końcowa wykracza poza zakres pracy, zwróć maksymalny czas
    return if (endHour >= 21) LocalTime.of(21, 0)
    else end
}
