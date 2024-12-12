package com.example.apptemplates.presentation.main.reservation.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.data.user.Role
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.LocalDate
import java.time.LocalTime


/*@Composable
fun TimePickerOption(
    label: String,
    label2: String,
    initialTime: LocalTime,
    initialEndTime: LocalTime,
    state: MainUiState,
    onStartTimeSelected: (LocalTime) -> Unit,
    onEndTimeSelected: (LocalTime) -> Unit
) {
    var currentPicker by remember { mutableStateOf<String?>(null) }
    var selectedStartTime by remember { mutableStateOf(initialTime) }
    var selectedEndTime by remember { mutableStateOf(initialEndTime) }
    var allDay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        // Wybór godziny rozpoczęcia
        TimePickerBox(
            label = label,
            selectedTime = selectedStartTime,
            isActive = currentPicker == "start",
            onClick = {
                allDay = false
                currentPicker = "start"
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Wybór godziny zakończenia
        TimePickerBox(
            label = label2,
            selectedTime = selectedEndTime,
            isActive = currentPicker == "end",
            onClick = {
                allDay = false
                currentPicker = "end"
            }
        )

        if (ActiveUser.getUser()?.role != Role.STUDENT && ActiveUser.getUser()?.role != Role.GUEST) {
            AllDaySwitchOption(
                checked = allDay,
            ) {
                allDay = it

                if (allDay) {
                    selectedStartTime = LocalTime.of(0, 0)
                    selectedEndTime = LocalTime.of(23, 59)
                    onStartTimeSelected(selectedStartTime)
                    onEndTimeSelected(selectedEndTime)
                } else {
                    selectedStartTime = initialTime
                    selectedEndTime = initialEndTime
                    onStartTimeSelected(initialTime)
                    onEndTimeSelected(initialEndTime)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Panel wyboru godziny rozpoczęcia
        if (currentPicker == "start" && !allDay) {
            TimePickerPanel(
                initialTime = selectedStartTime,
                predefTimes = generatePreferredStartTimes(selectedStartTime, state),
                onTimeSelected = { newTime ->
                    selectedStartTime = newTime
                    onStartTimeSelected(newTime)
                    currentPicker = null
                },
                onClose = { currentPicker = null }
            )
        }

        // Panel wyboru godziny zakończenia
        if (currentPicker == "end" && !allDay) {
            TimePickerPanel(
                initialTime = selectedEndTime,
                predefTimes = generatePreferredEndTimes(selectedStartTime, state),
                onTimeSelected = { newTime ->
                    selectedEndTime = newTime
                    onEndTimeSelected(newTime)
                    currentPicker = null
                },
                onClose = { currentPicker = null }
            )
        }
    }
}*/

@Composable
fun AllDaySwitchOption(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    // Całodobowe przełącznik
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(
                color = if (checked) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (checked) 2.dp else 0.dp,
                color = if (checked) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Całodobowe",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = { isChecked ->
                    onCheckedChange(isChecked)
                }
            )
        }
    }
}


@Composable
fun TimePickerBox(
    label: String,
    selectedTime: LocalTime,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(
                color = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (isActive) 2.dp else 0.dp,
                color = if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = selectedTime.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


/*@Composable
fun TimePickerPanel(
    initialTime: LocalTime,
    predefTimes: List<LocalTime>,
    onTimeSelected: (LocalTime) -> Unit,
    onClose: () -> Unit
) {
    var selectedHour by remember { mutableIntStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(initialTime.minute) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        // Ręczny wybór godzin i minut
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                ArrowButton(
                    onClick = {
                        if (selectedHour < 21) selectedHour += 1
                    },
                    isUp = true
                )


                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", selectedHour),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                ArrowButton(
                    onClick = {
                        if (selectedHour > 8) selectedHour -= 1
                    },
                    isUp = false
                )


            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ArrowButton(
                    onClick = {
                        selectedMinute = (selectedMinute + 5) % 60
                    },
                    isUp = true
                )
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", selectedMinute),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                ArrowButton(
                    onClick = {
                        selectedMinute = (selectedMinute - 5 + 60) % 60
                    },
                    isUp = false
                )
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        // Preferowane godziny
        Text(
            text = "Sugerowane godziny",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyHorizontalGrid(
            rows = GridCells.Fixed(if (predefTimes.size > 4) 2 else 1),
            modifier = Modifier
                .height(if (predefTimes.size > 4) 80.dp else 40.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(predefTimes) { time ->
                Card(
                    modifier = Modifier
                        .width(70.dp)
                        .height(40.dp)
                        .clickable { onTimeSelected(time) },
                    shape = RoundedCornerShape(8.dp),
                    //elevation = 2.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = time.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Przycisk zamknięcia i zatwierdzenia
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, // Rozstawione równomiernie
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = { onClose() },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                //elevation = ButtonDefaults.elevation(4.dp)
            ) {
                Text("Zamknij", style = MaterialTheme.typography.bodyMedium)
            }
            Button(
                onClick = {
                    onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                //elevation = ButtonDefaults.elevation(4.dp)
            ) {
                Text("Zatwierdź", style = MaterialTheme.typography.bodyMedium)
            }
        }

    }
}*/

@Composable
fun ArrowButton(onClick: () -> Unit, isUp: Boolean) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isUp) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isUp) "Increment" else "Decrement"
        )
    }
}

fun generatePreferredStartTimes(startTime: LocalTime, state: MainUiState): List<LocalTime> {
    val isFutureDate = state.selectedDate != null && state.selectedDate != LocalDate.now()

    return if (isFutureDate) {
        generateStartTimeSlots()
    } else {
        generateStartTimeSlots().filter { it.isAfter(startTime) }
    }
}

fun generateStartTimeSlots(): List<LocalTime> {
    return listOf(
        LocalTime.of(8, 0),
        LocalTime.of(9, 45),
        LocalTime.of(11, 45),
        LocalTime.of(13, 30),
        LocalTime.of(15, 10),
        LocalTime.of(16, 50),
        LocalTime.of(18, 30),
        LocalTime.of(20, 10)
    )
}

fun generatePreferredEndTimes(
    startTime: LocalTime,
    state: MainUiState
): List<LocalTime> {
    val isFutureDate = state.selectedDate != null && state.selectedDate != LocalDate.now()

    // Filtruj godziny zakończenia w zależności od godziny rozpoczęcia
    return if (isFutureDate) {
        generateEndTimeSlots().filter { it.isAfter(startTime.plusMinutes(60)) }
    } else {
        generateEndTimeSlots().filter { it.isAfter(startTime.plusMinutes(90)) }
    }
}

fun generateEndTimeSlots(): List<LocalTime> {
    return listOf(
        LocalTime.of(9, 30),
        LocalTime.of(11, 15),
        LocalTime.of(13, 15),
        LocalTime.of(15, 0),
        LocalTime.of(16, 40),
        LocalTime.of(18, 20),
        LocalTime.of(21, 40)
    )
}

/*@Composable
fun TimeField(
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = "",
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        //modifier = Modifier.weight(1f)
    )
}


fun ReservationError.showMessage(): String {
    return when (this) {
        is ReservationError.TimeConflict -> message
        is ReservationError.AttendeesConflict -> message
        is ReservationError.RecurrenceConflict -> message
    }
}


// Helper function to check for end-time specific errors
@OptIn(ExperimentalMaterial3Api::class)
fun checkEndTimeError(
    viewModel: ReservationViewModel,
    state: MainUiState,
    timePickerState: TimePickerState
): ReservationError? {
    val selectedTime = state.selectedTime
        ?: return ReservationError.TimeConflict("Nie wybrano godziny rozpoczęcia")

    return when {
        selectedTime.hour > timePickerState.hour -> ReservationError.TimeConflict("Niepoprawna godzina zakończenia")
        selectedTime.hour == timePickerState.hour && selectedTime.minute == timePickerState.minute -> ReservationError.TimeConflict(
            "Niepoprawna godzina zakończenia"
        )

        !isMinimallyCorrectTime(
            viewModel,
            selectedTime,
            timePickerState
        ) -> ReservationError.TimeConflict("Niepoprawna godzina zakończenia")

        else -> null // No errors
    }
}

// Checks if selected time is within the allowable range
@OptIn(ExperimentalMaterial3Api::class)
fun isMinimallyCorrectTime(
    viewModel: ReservationViewModel,
    selectedTime: LocalTime,
    timePickerState: TimePickerState
): Boolean {
    if (selectedTime.hour < 8 || selectedTime.hour > 21) return false
    if (selectedTime.hour == 21 && selectedTime.minute > 30) return false

    val minutesBetween = selectedTime.until(
        LocalTime.of(timePickerState.hour, timePickerState.minute),
        ChronoUnit.MINUTES
    )

    return viewModel.checkMaxReservationTime(minutesBetween)
}

@Composable
fun ErrorText(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(color = Color(0xFFFFCDD2), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error Icon",
            tint = Color(0xFFD32F2F),
            modifier = Modifier
                .size(20.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = message,
            color = Color(0xFFD32F2F),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LabelWithIcon(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color(0xFF9B5DE5).copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = Color(0xFF9B5DE5),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color(0xFF9B5DE5),
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


fun generateTimeSlots(startHour: Int, endHour: Int, intervalMinutes: Int): List<LocalTime> {
    val slots = mutableListOf<LocalTime>()
    for (hour in startHour..endHour) {
        for (minute in 0 until 60 step intervalMinutes) {
            slots.add(LocalTime.of(hour, minute))
        }
    }
    return slots
}*/

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerView(
    viewModel: ReservationViewModel,
    state: MainUiState,
    label: String,
    onTimeSelected: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
            .background(color = Color(0xFFF4F2FF), shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color(0xFF9B5DE5), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        LabelWithIcon(text = label)


        // Show error message if there is one
        if (state.reservationError != null) {
            ErrorText(state.reservationError.showMessage())
        }


        TimePicker(
            state = timePickerState,
            modifier = Modifier.scale(0.9f)
        )


        if (label.contains("Wybierz godzinę zakończenia")) {
            val error = checkEndTimeError(viewModel, state, timePickerState)

            if (error != null) {
                viewModel.errorState(error) // Set error state in ViewModel
            } else {
                viewModel.changeEndTime(timePickerState)
                viewModel.errorState(null) // Clear error state in ViewModel
                onTimeSelected()
            }

        } else {
            viewModel.changeTime(timePickerState)
        }

    }
}*/


@Composable
fun TimePickerOption(
    label: String,
    label2: String,
    initialTime: LocalTime,
    initialEndTime: LocalTime,
    state: MainUiState,
    onStartTimeSelected: (LocalTime) -> Unit,
    onEndTimeSelected: (LocalTime) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Definicja kolorów harmonizujących z tłem
    val titleColor = if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
    val boxBackgroundColor = if (isDarkTheme) Color(0xFF2C3E50) else Color(0xFFFFF8E1)
    val borderColor = if (isDarkTheme) Color(0xFF48C9B0) else Color(0xFF6D4C41)

    var currentPicker by remember { mutableStateOf<String?>(null) }
    var selectedStartTime by remember { mutableStateOf(initialTime) }
    var selectedEndTime by remember { mutableStateOf(initialEndTime) }
    var allDay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        // Wybór godziny rozpoczęcia
        TimePickerBox(
            label = label,
            selectedTime = selectedStartTime,
            isActive = currentPicker == "start",
            titleColor = titleColor,
            boxBackgroundColor = boxBackgroundColor,
            borderColor = borderColor,
            onClick = {
                allDay = false
                currentPicker = "start"
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Wybór godziny zakończenia
        TimePickerBox(
            label = label2,
            selectedTime = selectedEndTime,
            isActive = currentPicker == "end",
            titleColor = titleColor,
            boxBackgroundColor = boxBackgroundColor,
            borderColor = borderColor,
            onClick = {
                allDay = false
                currentPicker = "end"
            }
        )

        if (ActiveUser.getUser()?.role != Role.STUDENT && ActiveUser.getUser()?.role != Role.GUEST) {
            AllDaySwitchOption(
                checked = allDay,
                titleColor = titleColor,
                boxBackgroundColor = boxBackgroundColor,
                borderColor = borderColor,
            ) {
                allDay = it
                if (allDay) {
                    selectedStartTime = LocalTime.of(0, 0)
                    selectedEndTime = LocalTime.of(23, 59)
                    onStartTimeSelected(selectedStartTime)
                    onEndTimeSelected(selectedEndTime)
                } else {
                    selectedStartTime = initialTime
                    selectedEndTime = initialEndTime
                    onStartTimeSelected(initialTime)
                    onEndTimeSelected(initialEndTime)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Panel wyboru godziny rozpoczęcia
        if (currentPicker == "start" && !allDay) {
            TimePickerPanel(
                initialTime = selectedStartTime,
                predefTimes = generatePreferredStartTimes(selectedStartTime, state),
                onTimeSelected = { newTime ->
                    selectedStartTime = newTime
                    onStartTimeSelected(newTime)
                    currentPicker = null
                },
                onClose = { currentPicker = null }
            )
        }

        // Panel wyboru godziny zakończenia
        if (currentPicker == "end" && !allDay) {
            TimePickerPanel(
                initialTime = selectedEndTime,
                predefTimes = generatePreferredEndTimes(selectedStartTime, state),
                onTimeSelected = { newTime ->
                    selectedEndTime = newTime
                    onEndTimeSelected(newTime)
                    currentPicker = null
                },
                onClose = { currentPicker = null }
            )
        }
    }
}

@Composable
fun TimePickerBox(
    label: String,
    selectedTime: LocalTime,
    isActive: Boolean,
    titleColor: Color,
    boxBackgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val timeBox = if (isDarkTheme) Color(0xFF1B262C) else Color(0xFFF1F8E9)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(
                color = if (isActive) borderColor.copy(alpha = 0.1f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (isActive) 2.dp else 0.dp,
                color = if (isActive) borderColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(timeBox)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = selectedTime.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = titleColor
                )
            }
        }
    }
}

@Composable
fun AllDaySwitchOption(
    checked: Boolean,
    titleColor: Color,
    boxBackgroundColor: Color,
    borderColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(
                color = if (checked) borderColor.copy(alpha = 0.1f)
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (checked) 2.dp else 0.dp,
                color = if (checked) borderColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Całodobowe",
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = { isChecked -> onCheckedChange(isChecked) }
            )
        }
    }
}


@Composable
fun TimePickerPanel(
    initialTime: LocalTime,
    predefTimes: List<LocalTime>,
    onTimeSelected: (LocalTime) -> Unit,
    onClose: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    // Definicja kolorów harmonizujących z tłem
    val titleColor = if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
    val boxBackgroundColor = if (isDarkTheme) Color(0xFF1B262C) else Color(0xFFF1F8E9)
    val borderColor = if (isDarkTheme) Color(0xFF48C9B0) else Color(0xFF6D4C41)

    var selectedHour by remember { mutableIntStateOf(initialTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(initialTime.minute) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(boxBackgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Ręczny wybór godzin i minut
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ArrowButton(
                    onClick = {
                        if (selectedHour < 21) selectedHour += 1
                    },
                    isUp = true
                )

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(boxBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", selectedHour),
                        style = MaterialTheme.typography.titleLarge,
                        color = titleColor
                    )
                }

                ArrowButton(
                    onClick = {
                        if (selectedHour > 8) selectedHour -= 1
                    },
                    isUp = false
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ArrowButton(
                    onClick = {
                        selectedMinute = (selectedMinute + 5) % 60
                    },
                    isUp = true
                )
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                        .background(boxBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", selectedMinute),
                        style = MaterialTheme.typography.titleLarge,
                        color = titleColor
                    )
                }
                ArrowButton(
                    onClick = {
                        selectedMinute = (selectedMinute - 5 + 60) % 60
                    },
                    isUp = false
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Preferowane godziny
        Text(
            text = "Sugerowane godziny",
            style = MaterialTheme.typography.titleMedium,
            color = titleColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyHorizontalGrid(
            rows = GridCells.Fixed(if (predefTimes.size > 4) 2 else 1),
            modifier = Modifier
                .height(if (predefTimes.size > 4) 80.dp else 40.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(predefTimes) { time ->
                Card(
                    modifier = Modifier
                        .width(70.dp)
                        .height(40.dp)
                        .clickable { onTimeSelected(time) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = time.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = titleColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Przycisk zamknięcia i zatwierdzenia
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp), // Odpowiedni odstęp między przyciskami
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = { onClose() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp) // Ustawienie stałej wysokości dla spójności
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp) // Więcej miejsca dla tekstu
            ) {
                Text(
                    "Zamknij",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1, // Zapewnienie jednej linii
                    overflow = TextOverflow.Ellipsis // Obsługa przepełnienia (gdyby tekst był za długi)
                )
            }
            Button(
                onClick = {
                    onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp) // Ustawienie stałej wysokości dla spójności
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp) // Więcej miejsca dla tekstu
            ) {
                Text(
                    "Zatwierdź",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1, // Zapewnienie jednej linii
                    overflow = TextOverflow.Ellipsis // Obsługa przepełnienia (gdyby tekst był za długi)
                )
            }
        }

    }
}


