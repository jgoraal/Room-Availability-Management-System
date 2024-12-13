package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.temp.MainUiState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*@Composable
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
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
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
}*/




@Composable
fun RecurrencePickerContent(
    isVisible: Boolean,
    viewModel: ReservationViewModel,
    state: MainUiState,
    titleColor: Color,
    iconTint: Color,
    containerBrush: Brush,
    borderColor: Color,
    expandedContainerColor: Color,
    expandedBorderColor: Color
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        // Główne opakowanie z gradientowym tłem
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(brush = containerBrush, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                RecurrenceCheckbox(
                    isChecked = state.isRecurring,
                    onCheckedChange = {
                        if (state.availableRooms.isNotEmpty()) {
                            viewModel.clearAvailableRooms()
                        }
                        viewModel.changeRecurring(it)
                        if (!it) viewModel.changeEndDate(null)
                        else if (state.endRecurrenceDate == null) viewModel.updateEndRecurrenceDate()

                        viewModel.saveState()
                    },
                    titleColor = titleColor
                )

                if (state.isRecurring) {
                    Spacer(modifier = Modifier.height(16.dp))
                    RecurrenceFrequencyOptions(
                        selectedFrequency = state.recurringFrequency ?: RecurrenceFrequency.WEEKLY,
                        onSelect = {
                            viewModel.changeFrequency(it)
                            if (state.availableRooms.isNotEmpty()) {
                                viewModel.clearAvailableRooms()
                            }

                            viewModel.saveState()
                        },
                        titleColor = titleColor,
                        iconTint = iconTint
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    EndDateSelector(
                        viewModel = viewModel,
                        state = state,
                        titleColor = titleColor,
                        iconTint = iconTint,
                        expandedContainerColor = expandedContainerColor,
                        expandedBorderColor = expandedBorderColor
                    )
                }
            }
        }
    }
}

@Composable
fun RecurrenceCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    titleColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Czy jest to rezerwacja cykliczna",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = titleColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = titleColor,
                uncheckedColor = titleColor.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
fun RecurrenceFrequencyOptions(
    selectedFrequency: RecurrenceFrequency,
    onSelect: (RecurrenceFrequency) -> Unit,
    titleColor: Color,
    iconTint: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FrequencyOption("Tygodniowo", selectedFrequency == RecurrenceFrequency.WEEKLY, titleColor) {
            onSelect(RecurrenceFrequency.WEEKLY)
        }
        FrequencyOption(
            "Co dwa tygodnie",
            selectedFrequency == RecurrenceFrequency.BIWEEKLY,
            titleColor
        ) {
            onSelect(RecurrenceFrequency.BIWEEKLY)
        }
        FrequencyOption(
            "Miesięcznie",
            selectedFrequency == RecurrenceFrequency.MONTHLY,
            titleColor
        ) {
            onSelect(RecurrenceFrequency.MONTHLY)
        }
    }
}

@Composable
fun FrequencyOption(text: String, isSelected: Boolean, titleColor: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = titleColor,
                unselectedColor = titleColor.copy(alpha = 0.6f)
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = titleColor
        )
    }
}

@Composable
fun EndDateSelector(
    viewModel: ReservationViewModel,
    state: MainUiState,
    titleColor: Color,
    iconTint: Color,
    expandedContainerColor: Color,
    expandedBorderColor: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, expandedBorderColor),
        colors = CardDefaults.cardColors(containerColor = expandedContainerColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Wybierz datę zakończenia",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = titleColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        if (state.availableRooms.isNotEmpty()) {
                            viewModel.clearAvailableRooms()
                        }

                        if (state.duration > 1) viewModel.updateDuration(-1)

                        viewModel.saveState()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Zmniejsz", tint = iconTint)
                }

                Text(
                    text = viewModel.getDurationText(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = titleColor,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                IconButton(
                    onClick = {
                        if (state.availableRooms.isNotEmpty()) {
                            viewModel.clearAvailableRooms()
                        }
                        if (viewModel.canAddDuration()) viewModel.updateDuration(1)

                        viewModel.saveState()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zwiększ", tint = iconTint)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Data zakończenia",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = titleColor,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = state.endRecurrenceDate.prepareDateFormat(),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = titleColor,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .background(
                        color = expandedContainerColor.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 6.dp, horizontal = 12.dp)
            )
        }
    }
}


fun LocalDate?.prepareDateFormat(): String {
    if (this == null) return ""

    val date = this
    val dayOfWeek = date.dayOfWeek.getDayOfWeekFormatted()

    return dayOfWeek + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

private fun DayOfWeek.getDayOfWeekFormatted(): String {
    return when (this) {
        DayOfWeek.MONDAY -> "Pon, "
        DayOfWeek.TUESDAY -> "Wto, "
        DayOfWeek.WEDNESDAY -> "Śro, "
        DayOfWeek.THURSDAY -> "Czw, "
        DayOfWeek.FRIDAY -> "Pią, "
        DayOfWeek.SATURDAY -> "Sob, "
        DayOfWeek.SUNDAY -> "Niedz, "
    }
}
