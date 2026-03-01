package com.saurabh.marathicalendar.data.provider

import android.content.Context
import com.saurabh.marathicalendar.data.model.MonthData
import kotlinx.serialization.json.Json
import java.io.IOException

class CalendarDataProvider(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val cache = mutableMapOf<Int, MonthData>()

    private val monthFileNames = mapOf(
        1 to "january_2026.json",
        2 to "february_2026.json",
        3 to "march_2026.json",
        4 to "april_2026.json",
        5 to "may_2026.json",
        6 to "june_2026.json",
        7 to "july_2026.json",
        8 to "august_2026.json",
        9 to "september_2026.json",
        10 to "october_2026.json",
        11 to "november_2026.json",
        12 to "december_2026.json"
    )

    fun getMonth(month: Int): MonthData? {
        if (month < 1 || month > 12) return null
        cache[month]?.let { return it }

        val fileName = monthFileNames[month] ?: return null
        return try {
            val jsonString = context.assets.open("calendar_data/$fileName")
                .bufferedReader()
                .use { it.readText() }
            val monthData = json.decodeFromString<MonthData>(jsonString)
            cache[month] = monthData
            monthData
        } catch (e: Exception) {
            null
        }
    }

    fun getAllMonths(): List<MonthData> =
        (1..12).mapNotNull { getMonth(it) }

    fun preloadAll() {
        (1..12).forEach { getMonth(it) }
    }
}
