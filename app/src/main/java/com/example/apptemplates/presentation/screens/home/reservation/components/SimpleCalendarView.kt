package com.example.apptemplates.presentation.screens.home.reservation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.presentation.screens.home.reservation.ReservationViewModel
import com.example.apptemplates.utils.DarkThemeComponentsColors
import com.example.apptemplates.utils.LightThemeComponentsColors
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale


// ------------------ Extensions ------------------ //

fun Date.formatToMonthString(): String {
    val formatted = SimpleDateFormat("LLLL", Locale("pl", "PL")).format(this)
    return formatted.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale(
                "pl",
                "PL"
            )
        ) else it.toString()
    }
}

private fun Int.getDayOfWeek3Letters(): String? {
    val dayName = Calendar.getInstance().run {
        set(Calendar.DAY_OF_WEEK, this@getDayOfWeek3Letters)
        getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale("pl", "PL"))
    }

    return dayName
        ?.replace(".", "")
        ?.replaceFirstChar { it.uppercaseChar() }
}

private fun Date.formatToCalendarDay(): String =
    SimpleDateFormat("d", Locale.getDefault()).format(this)

fun getFirstWeekdayOfMonth(year: Int, month: Int): Int {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    return if (dayOfWeek == Calendar.SUNDAY) 7 else dayOfWeek - 1
}

fun getDaysInMonth(year: Int, month: Int): List<Pair<Date, Boolean>> {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val currentDate = Calendar.getInstance()

    return (1..daysInMonth).map { day ->
        calendar.set(Calendar.DAY_OF_MONTH, day)
        val isToday = calendar.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)
        Pair(calendar.time, isToday)
    }
}

// ------------------ UI Components ------------------ //

@Composable
private fun CalendarCell(
    date: Date,
    isToday: Boolean,
    signal: Boolean,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val themeColors = if (isDarkTheme) DarkThemeComponentsColors else LightThemeComponentsColors

    val text = date.formatToCalendarDay()
    val calendar = Calendar.getInstance()
    val isPast = date.before(calendar.time) && !signal
    val actuallyToday =
        isToday && !isSelected


    val (backgroundModifier, borderColor, borderWidth) = when {
        isSelected -> {

            Triple(
                Modifier.background(
                    brush = Brush.linearGradient(themeColors.selectedBackground),
                    shape = RoundedCornerShape(8.dp)
                ),
                themeColors.accentColor,
                2.dp
            )
        }

        actuallyToday -> {

            Triple(
                Modifier.background(
                    color = themeColors.cardBackground.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ),
                themeColors.accentColor,
                2.dp
            )
        }

        isPast -> {

            Triple(
                Modifier.background(
                    color = themeColors.cardBackground.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                ),
                themeColors.dividerColor.copy(alpha = 0.5f),
                1.dp
            )
        }

        else -> {

            Triple(
                Modifier.background(
                    color = themeColors.cardBackground,
                    shape = RoundedCornerShape(8.dp)
                ),
                themeColors.dividerColor,
                1.dp
            )
        }
    }


    val textColor = when {
        isSelected -> themeColors.primaryText
        actuallyToday -> themeColors.primaryText
        isPast -> themeColors.secondaryText.copy(alpha = 0.4f)
        else -> themeColors.secondaryText
    }


    val textStyle = if (isSelected || actuallyToday) {
        MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )
    } else {
        MaterialTheme.typography.bodyMedium
    }


    val clickableModifier = if (!isPast) Modifier.clickable { onClick() } else Modifier

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .then(clickableModifier)
            .then(backgroundModifier)
            .clip(RoundedCornerShape(8.dp))
            .border(width = borderWidth, color = borderColor, shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun WeekdayCell(weekday: Int, modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val themeColors = if (isDarkTheme) DarkThemeComponentsColors else LightThemeComponentsColors

    val text = weekday.getDayOfWeek3Letters()?.uppercase().orEmpty()

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .background(
                color = themeColors.cardBackground.copy(alpha = 0.2f),
                shape = RoundedCornerShape(50)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = themeColors.primaryText,
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
        weekdays.forEach { WeekdayCell(it) }

        repeat(weekdayFirstDay - 1) { Spacer(modifier = Modifier) }

        dates.forEach { (date, signal) ->
            val dateCalendar = Calendar.getInstance().apply { time = date }
            val isToday = (dateCalendar.get(Calendar.YEAR) == currentYear &&
                    dateCalendar.get(Calendar.MONTH) == currentMonth &&
                    dateCalendar.get(Calendar.DAY_OF_MONTH) == currentDay)
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
    val horizontalGap = with(LocalDensity.current) { horizontalGapDp.roundToPx() }
    val verticalGap = with(LocalDensity.current) { verticalGapDp.roundToPx() }

    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val totalWidthWithoutGap = constraints.maxWidth - (horizontalGap * 6)
        val singleWidth = totalWidthWithoutGap / 7

        val xPositions = mutableListOf<Int>()
        val yPositions = mutableListOf<Int>()
        var currentX = 0
        var currentY = 0

        measurables.forEach { _ ->
            xPositions.add(currentX)
            yPositions.add(currentY)
            if (currentX + singleWidth + horizontalGap > totalWidthWithoutGap) {
                currentX = 0
                currentY += singleWidth + verticalGap
            } else {
                currentX += singleWidth + horizontalGap
            }
        }

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(maxWidth = singleWidth, maxHeight = singleWidth))
        }

        layout(width = constraints.maxWidth, height = currentY + singleWidth + verticalGap) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(x = xPositions[index], y = yPositions[index])
            }
        }
    }
}

// ------------------ Public API ------------------ //

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
    val isDarkTheme = isSystemInDarkTheme()
    val themeColors = if (isDarkTheme) DarkThemeComponentsColors else LightThemeComponentsColors

    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (displayPrev) {
                IconButton(
                    onClick = onClickPrev,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Poprzedni miesiąc",
                        modifier = Modifier.wrapContentSize(),
                        tint = themeColors.iconColor
                    )
                }
            }

            if (displayNext) {
                IconButton(
                    onClick = onClickNext,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Następny miesiąc",
                        modifier = Modifier.wrapContentSize(),
                        tint = themeColors.iconColor
                    )
                }
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                        brush = Brush.linearGradient(themeColors.cardGradient),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = month.formatToMonthString(),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = themeColors.primaryText
                )
                Text(
                    text = year.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    ),
                    color = themeColors.secondaryText
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
        mutableStateOf(getDaysInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)))
    }
    val selectedDate = viewModel.state.collectAsState().value.selectedDate

    val canGoBack = calendar.get(Calendar.YEAR) > currentCalendar.get(Calendar.YEAR) ||
            (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) > currentCalendar.get(Calendar.MONTH))

    fun updateMonth(offset: Int) {
        calendar.add(Calendar.MONTH, offset)
        dates = getDaysInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
    }

    CalendarView(
        selectedDate = selectedDate,
        month = calendar.time,
        date = dates,
        displayNext = true,
        displayPrev = canGoBack,
        onClickNext = { updateMonth(1) },
        onClickPrev = { if (canGoBack) updateMonth(-1) },
        onClick = {
            viewModel.changeDate(it)
            onDateSelected()
        },
        startFromSunday = startFromSunday,
        modifier = modifier,
        year = calendar.get(Calendar.YEAR),
        monthh = calendar.get(Calendar.MONTH)
    )
}

@Preview(showBackground = true)
@Composable
private fun calendar() {
    CalendarViewWithNavigation(viewModel = ReservationViewModel(), startFromSunday = false)
}





