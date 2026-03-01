package com.saurabh.marathicalendar.util

import java.util.Calendar

object DateUtils {

    fun getTodayMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    fun getTodayDate(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    fun getTodayYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

    fun isToday(month: Int, day: Int, year: Int): Boolean {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR) == year &&
               today.get(Calendar.MONTH) + 1 == month &&
               today.get(Calendar.DAY_OF_MONTH) == day
    }

    fun dayOfWeekLabel(dayOfWeek: Int): String =
        MarathiConstants.DAYS_FULL_MARATHI.getOrElse(dayOfWeek - 1) { "" }
}
