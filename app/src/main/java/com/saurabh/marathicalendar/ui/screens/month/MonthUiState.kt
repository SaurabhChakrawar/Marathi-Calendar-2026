package com.saurabh.marathicalendar.ui.screens.month

import com.saurabh.marathicalendar.data.model.MonthData

data class MonthUiState(
    val currentMonth: Int = 1,
    val monthData: MonthData? = null,
    val selectedDay: Int? = null,
    val todayMonth: Int? = null,
    val todayDate: Int? = null,
    val isLoading: Boolean = true,
    val selectedDayNote: String = "",
    val allMonthsData: Map<Int, MonthData> = emptyMap(),
    val requestedMonth: Int? = null,
    val daysWithNotes: Set<Int> = emptySet()
)
