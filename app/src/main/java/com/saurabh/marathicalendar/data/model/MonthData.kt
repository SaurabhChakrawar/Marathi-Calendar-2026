package com.saurabh.marathicalendar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MonthData(
    val monthNumber: Int,
    val monthNameEnglish: String,
    val monthNameMarathi: String,
    val year: Int,
    val totalDays: Int,
    val firstDayOfWeek: Int, // 1=Sunday, 7=Saturday
    val marathiMonthsActive: List<String>,
    val days: List<DayData>
)
