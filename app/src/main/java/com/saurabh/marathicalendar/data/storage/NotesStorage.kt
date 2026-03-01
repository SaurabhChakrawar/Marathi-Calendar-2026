package com.saurabh.marathicalendar.data.storage

import android.content.Context

class NotesStorage(context: Context) {

    private val prefs = context.getSharedPreferences("marathi_calendar_notes", Context.MODE_PRIVATE)

    fun getNote(month: Int, day: Int): String =
        prefs.getString(key(month, day), "") ?: ""

    fun saveNote(month: Int, day: Int, note: String) {
        if (note.isBlank()) {
            prefs.edit().remove(key(month, day)).apply()
        } else {
            prefs.edit().putString(key(month, day), note.trim()).apply()
        }
    }

    private fun key(month: Int, day: Int) = "note_2026_${month}_${day}"
}
