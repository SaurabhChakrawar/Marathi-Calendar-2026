package com.saurabh.marathicalendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.saurabh.marathicalendar.data.model.MonthData

@Composable
fun CalendarGrid(
    monthData: MonthData,
    todayDate: Int?,
    selectedDay: Int?,
    onDayClick: (Int) -> Unit,
    daysWithNotes: Set<Int> = emptySet(),
    modifier: Modifier = Modifier
) {
    val totalCells = monthData.firstDayOfWeek - 1 + monthData.totalDays
    val rows = (totalCells + 6) / 7

    Column(modifier = modifier) {
        for (row in 0 until rows) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val dayIndex = cellIndex - (monthData.firstDayOfWeek - 1)

                    if (dayIndex < 0 || dayIndex >= monthData.totalDays) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    } else {
                        val dayData = monthData.days[dayIndex]
                        DayCell(
                            dayData = dayData,
                            isToday = dayData.gregorianDate == todayDate,
                            isSelected = dayData.gregorianDate == selectedDay && dayData.gregorianDate != todayDate,
                            hasNote = dayData.gregorianDate in daysWithNotes,
                            onClick = { onDayClick(dayData.gregorianDate) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    }
}
