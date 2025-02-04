package com.example.apptemplates.presentation.screens.home.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.apptemplates.domain.model.EquipmentType
import com.example.apptemplates.presentation.screens.home.base.MainUiState
import com.example.apptemplates.presentation.screens.home.reservation.ReservationViewModel
import com.example.apptemplates.presentation.screens.home.reservation.getNameInPolish
import com.example.apptemplates.presentation.screens.home.room_availability.TopDownElement


@Composable
fun AdditionalFiltersPicker(viewModel: ReservationViewModel, state: MainUiState) {
    val isAdditionalFiltersVisible by remember { mutableStateOf(false) }



    TopDownElement(
        visible = remember { mutableStateOf(isAdditionalFiltersVisible) },
        imageVector = Icons.Default.FilterAlt,
        title = "Dodatkowe filtry",
        titleStyle = MaterialTheme.typography.bodyLarge
    ) {
        // Wybór piętra
        FloorPicker(state, viewModel)

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Sekcja wyposażenia
        EquipmentFilters(viewModel, state)
    }


}


@Composable
fun FloorPicker(state: MainUiState, viewModel: ReservationViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    val titleColor = if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
    val borderColor = if (isDarkTheme) Color(0xFF1ABC9C) else Color(0xFF8D6E63)
    val containerBrush = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF34495E), Color(0xFF2C3E50))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFFCE5D1), Color(0xFFFDEDD4))
        )
    }



    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(containerBrush, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Wybierz piętro",
            style = MaterialTheme.typography.titleMedium,
            color = titleColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        DropdownMenuFloorPicker(
            selectedFloor = state.selectedFloor
        ) { selectedFloor ->
            viewModel.updateSelectedFloor(selectedFloor)
            if (state.availableRooms.isNotEmpty()) {
                viewModel.clearAvailableRooms()
            }

            viewModel.saveState()
        }
    }
}

@Composable
fun EquipmentFilters(viewModel: ReservationViewModel, state: MainUiState) {
    val isDarkTheme = isSystemInDarkTheme()
    val titleColor = if (isDarkTheme) Color(0xFFAEDFF7) else Color(0xFF3E2723)
    val checkboxColor = if (isDarkTheme) Color(0xFF48C9B0) else Color(0xFF6D4C41)
    val backgroundBrush = if (isDarkTheme) {
        Brush.verticalGradient(colors = listOf(Color(0xFF2C3E50), Color(0xFF34495E)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFFDEDD4), Color(0xFFFCE5D1)))
    }
    val borderColor = if (isDarkTheme) Color(0xFF1ABC9C) else Color(0xFF8D6E63)

    val availableEquipment = listOf(
        EquipmentType.COMPUTER,
        EquipmentType.PROJECTOR,
        EquipmentType.WHITEBOARD
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(backgroundBrush, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        // Checkbox "Nie uwzględniaj wyposażenia"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable {
                    if (state.availableRooms.isNotEmpty()) {
                        viewModel.clearAvailableRooms()
                    }

                    val ignoreEquipment = !state.ignoreEquipment
                    viewModel.updateIgnoreEquipment(ignoreEquipment)
                    if (ignoreEquipment) {
                        viewModel.updateSelectedEquipment(emptyList())
                    }

                    viewModel.saveState()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.ignoreEquipment,
                onCheckedChange = { isChecked ->

                    if (state.availableRooms.isNotEmpty()) {
                        viewModel.clearAvailableRooms()
                    }


                    viewModel.updateIgnoreEquipment(isChecked)
                    if (isChecked) {
                        viewModel.updateSelectedEquipment(emptyList())
                    }

                    viewModel.saveState()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = checkboxColor,
                    uncheckedColor = checkboxColor,
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Nie uwzględniaj wyposażenia",
                style = MaterialTheme.typography.bodyMedium,
                color = titleColor
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista dostępnego wyposażenia
        availableEquipment.forEach { equipment ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {

                        if (state.availableRooms.isNotEmpty()) {
                            viewModel.clearAvailableRooms()
                        }


                        if (!state.ignoreEquipment) {
                            val updatedEquipment =
                                if (state.selectedEquipment.contains(equipment)) {
                                    state.selectedEquipment - equipment
                                } else {
                                    state.selectedEquipment + equipment
                                }
                            viewModel.updateSelectedEquipment(updatedEquipment)
                        }

                        viewModel.saveState()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.selectedEquipment.contains(equipment) && !state.ignoreEquipment,
                    onCheckedChange = { isChecked ->

                        if (state.availableRooms.isNotEmpty()) {
                            viewModel.clearAvailableRooms()
                        }

                        if (!state.ignoreEquipment) {
                            val updatedEquipment = if (isChecked) {
                                state.selectedEquipment + equipment
                            } else {
                                state.selectedEquipment - equipment
                            }
                            viewModel.updateSelectedEquipment(updatedEquipment)
                        }

                        viewModel.saveState()
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = checkboxColor,
                        uncheckedColor = checkboxColor,
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = equipment.getNameInPolish(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = titleColor
                )
            }
        }
    }
}


@Composable
fun FiltersHeader(isVisible: Boolean, onToggleVisibility: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleVisibility() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.FilterAlt,
            contentDescription = "Filtry",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Dodatkowe filtry",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = if (isVisible) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = "Rozwiń / Zwiń",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



