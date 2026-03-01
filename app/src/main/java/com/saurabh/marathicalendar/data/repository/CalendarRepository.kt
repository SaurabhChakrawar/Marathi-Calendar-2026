package com.saurabh.marathicalendar.data.repository

import com.saurabh.marathicalendar.data.model.DayData
import com.saurabh.marathicalendar.data.model.MonthData
import com.saurabh.marathicalendar.data.provider.CalendarDataProvider

class CalendarRepository(private val provider: CalendarDataProvider) {

    fun getMonth(month: Int): MonthData? = provider.getMonth(month)

    fun getDay(month: Int, day: Int): DayData? =
        provider.getMonth(month)?.days?.find { it.gregorianDate == day }

    fun getAllMonths(): List<MonthData> = provider.getAllMonths()

    fun preload() = provider.preloadAll()
}
