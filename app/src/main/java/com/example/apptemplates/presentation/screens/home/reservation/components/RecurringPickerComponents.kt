package com.example.apptemplates.presentation.screens.home.reservation.components

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
import com.example.apptemplates.domain.model.RecurrenceFrequency
import com.example.apptemplates.presentation.screens.home.base.MainUiState
import com.example.apptemplates.presentation.screens.home.reservation.ReservationViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter



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
