package com.saurabh.marathicalendar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DayData(
    val gregorianDate: Int,
    val gregorianMonth: Int,
    val gregorianYear: Int,
    val dayOfWeek: Int, // 1=Sunday, 2=Monday ... 7=Saturday
    val marathiDate: MarathiDateInfo,
    val panchang: PanchangData,
    val festivals: List<FestivalData> = emptyList(),
    val isGovernmentHoliday: Boolean = false,
    val governmentHolidayName: String = "",
    val governmentHolidayNameMarathi: String = "",
    val sunriseTime: String = "06:30",
    val sunsetTime: String = "18:30"
)
