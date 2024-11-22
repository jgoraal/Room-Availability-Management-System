package com.example.apptemplates.presentation.main.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun Date.formatToMonthString(): String = SimpleDateFormat("LLLL", Locale("pl", "PL"))
    .format(this)
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("pl", "PL")) else it.toString() }

private fun Int.getDayOfWeek3Letters(): String? {
    val day = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, this@getDayOfWeek3Letters)
    }.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale("pl", "PL"))

    return day?.replace(".", "")?.replaceFirstChar { it.uppercaseChar() }
}


private fun Date.formatToCalendarDay(): String =
    SimpleDateFormat("d", Locale.getDefault()).format(this)


@Composable
private fun CalendarCell(
    date: Date,
    isToday: Boolean,
    signal: Boolean,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = date.formatToCalendarDay()
    val calendar = Calendar.getInstance()
    val isPast = date.before(calendar.time) && !signal

    // Define background colors for different date types and states
    val pastColor = Color(0xFFE0E0E0)
    val futureColor = Color(0xFFE3F2FD)
    val selectedColor = Color(0xFFFFCDD2) // Light pink for selected state
    val todayGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784)) // Green gradient for today
    )

    // Determine background color based on state
    val backgroundColor = when {
        isSelected -> selectedColor
        isToday -> Color.Transparent // Apply gradient for today's date
        isPast -> pastColor
        else -> futureColor
    }

    // Determine text color based on state
    val textColor = when {
        isToday -> Color.White
        isSelected -> Color(0xFFC62828) // Darker red for selected text
        isPast -> Color.Gray
        else -> Color(0xFF0D47A1)
    }

    // Only allow clicking if not in the past
    val clickableModifier = if (isPast || isToday) Modifier else Modifier.clickable { onClick() }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .then(clickableModifier)
            .background(
                color = if (!isToday) backgroundColor else Color.Transparent,
                shape = RoundedCornerShape(CornerSize(8.dp))
            )
            .then(
                if (isToday) Modifier.background(
                    brush = todayGradient,
                    shape = RoundedCornerShape(CornerSize(8.dp))
                ) else Modifier
            )
            .clip(RoundedCornerShape(CornerSize(8.dp)))
            .border(
                width = if (isToday) 2.dp else 1.dp,
                color = if (isToday) Color(0xFF388E3C) else Color.LightGray,
                shape = RoundedCornerShape(CornerSize(8.dp))
            )
    ) {

        Text(
            text = text,
            color = textColor,
            style = if (isToday) MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            ) else MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun WeekdayCell(weekday: Int, modifier: Modifier = Modifier) {
    val text = weekday.getDayOfWeek3Letters()

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .background(
                color = colorScheme.primaryContainer.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50) // Circular background shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.orEmpty().uppercase(),
            color = colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                letterSpacing = 1.25.sp
            ),
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}


@Composable
private fun CalendarGrid(
    dates: List<Pair<Date, Boolean>>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    startFromSunday: Boolean,
    year: Int,
    month: Int,
    modifier: Modifier = Modifier,
) {
    val calendar = Calendar.getInstance()
    val weekdayFirstDay = getFirstWeekdayOfMonth(year, month)
    val weekdays = getWeekDays(startFromSunday)

    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    CalendarCustomLayout(modifier = modifier) {
        weekdays.forEach {
            WeekdayCell(weekday = it)
        }

        // Adds spacers to align the first day of the month
        repeat(weekdayFirstDay - 1) {
            Spacer(modifier = Modifier)
        }

        dates.forEach { (date, signal) ->
            val dateCalendar = Calendar.getInstance().apply { time = date }
            val isToday = dateCalendar.get(Calendar.YEAR) == currentYear &&
                    dateCalendar.get(Calendar.MONTH) == currentMonth &&
                    dateCalendar.get(Calendar.DAY_OF_MONTH) == currentDay
            val isSelected =
                selectedDate == date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            CalendarCell(
                date = date,
                isToday = isToday,
                isSelected = isSelected,
                signal = signal,
                onClick = {
                    val clickedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(clickedDate)
                }
            )
        }
    }
}

fun getWeekDays(startFromSunday: Boolean): List<Int> {
    val list = (1..7).toList()
    return if (startFromSunday) list else list.drop(1) + list.take(1)
}


@Composable
private fun CalendarCustomLayout(
    modifier: Modifier = Modifier,
    horizontalGapDp: Dp = 2.dp,
    verticalGapDp: Dp = 2.dp,
    content: @Composable () -> Unit,
) {
    val horizontalGap = with(LocalDensity.current) {
        horizontalGapDp.roundToPx()
    }
    val verticalGap = with(LocalDensity.current) {
        verticalGapDp.roundToPx()
    }
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val totalWidthWithoutGap = constraints.maxWidth - (horizontalGap * 6)
        val singleWidth = totalWidthWithoutGap / 7

        val xPos: MutableList<Int> = mutableListOf()
        val yPos: MutableList<Int> = mutableListOf()
        var currentX = 0
        var currentY = 0
        measurables.forEach { _ ->
            xPos.add(currentX)
            yPos.add(currentY)
            if (currentX + singleWidth + horizontalGap > totalWidthWithoutGap) {
                currentX = 0
                currentY += singleWidth + verticalGap
            } else {
                currentX += singleWidth + horizontalGap
            }
        }

        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(constraints.copy(maxHeight = singleWidth, maxWidth = singleWidth))
        }

        layout(
            width = constraints.maxWidth,
            height = currentY + singleWidth + verticalGap,
        ) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = xPos[index],
                    y = yPos[index],
                )
            }
        }
    }
}


@Composable
fun CalendarView(
    selectedDate: LocalDate?,
    month: Date,
    date: List<Pair<Date, Boolean>>?,
    displayNext: Boolean,
    displayPrev: Boolean,
    onClickNext: () -> Unit,
    onClickPrev: () -> Unit,
    onClick: (LocalDate) -> Unit,
    startFromSunday: Boolean,
    modifier: Modifier = Modifier,
    year: Int,
    monthh: Int
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (displayPrev)
                IconButton(
                    onClick = onClickPrev,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "navigate to previous month",
                        modifier.wrapContentSize()
                    )
                }
            if (displayNext)
                IconButton(
                    onClick = onClickNext,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "navigate to next month",
                        modifier.wrapContentSize()
                    )
                }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                // Displaying the month
                Text(
                    text = month.formatToMonthString(),
                    style = typography.headlineMedium,
                    color = colorScheme.onPrimaryContainer,
                )
                // Displaying the year
                Text(
                    text = year.toString(),
                    style = typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    ),
                    color = colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        if (!date.isNullOrEmpty()) {
            CalendarGrid(
                selectedDate = selectedDate,
                dates = date,
                onDateSelected = onClick,
                startFromSunday = startFromSunday,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                year = year,
                month = monthh
            )
        }
    }
}


fun getDaysInMonth(year: Int, month: Int): List<Pair<Date, Boolean>> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val currentDate = Calendar.getInstance() // Get the current date

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    return (1..daysInMonth).map { day ->
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val isToday = calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)

        Pair(calendar.time, isToday)
    }
}

fun getFirstWeekdayOfMonth(year: Int, month: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    // Adjust to make Monday as the first day of the week
    return if (dayOfWeek == Calendar.SUNDAY) 7 else dayOfWeek - 1
}


@Composable
fun CalendarViewWithNavigation(
    viewModel: ReservationViewModel,
    startFromSunday: Boolean,
    onDateSelected: () -> Unit = {},
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
        onClick = { viewModel.changeDate(it)
            onDateSelected()
                  },
        startFromSunday = startFromSunday,
        modifier = Modifier,
        year = calendar.get(Calendar.YEAR),
        monthh = calendar.get(Calendar.MONTH)
    )


}

@Preview(showBackground = true)
@Composable
private fun calendar() {
    CalendarViewWithNavigation(viewModel = ReservationViewModel(), startFromSunday = false)
}





