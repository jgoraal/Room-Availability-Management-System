package com.example.apptemplates.presentation.screens.home.reservation.components

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
import com.example.apptemplates.data.model.model.user.Role
import com.example.apptemplates.domain.usecase.ActiveUser
import com.example.apptemplates.presentation.screens.home.base.MainUiState
import java.time.LocalDate
import java.time.LocalTime



@Composable
fun AllDaySwitchOption(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {

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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = { onClose() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text(
                    "Zamknij",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Button(
                onClick = {
                    onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text(
                    "Zatwierdź",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}


