package com.saurabh.marathicalendar.data.storage

import android.content.Context

enum class FontSizeOption(val scale: Float, val labelMarathi: String) {
    SMALL(0.85f, "लहान"),
    MEDIUM(1.0f, "मध्यम"),
    LARGE(1.15f, "मोठे")
}

class SettingsStorage(context: Context) {

    private val prefs = context.getSharedPreferences("marathi_calendar_settings", Context.MODE_PRIVATE)

    fun isDarkMode(): Boolean = prefs.getBoolean("dark_mode", false)

    fun setDarkMode(value: Boolean) {
        prefs.edit().putBoolean("dark_mode", value).apply()
    }

    fun getFontSize(): FontSizeOption =
        FontSizeOption.entries.getOrElse(prefs.getInt("font_size", 1)) { FontSizeOption.MEDIUM }

    fun setFontSize(option: FontSizeOption) {
        prefs.edit().putInt("font_size", option.ordinal).apply()
    }
}
