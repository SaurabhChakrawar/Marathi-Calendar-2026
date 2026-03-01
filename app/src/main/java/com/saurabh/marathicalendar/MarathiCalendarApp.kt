package com.saurabh.marathicalendar

import android.app.Application
import com.saurabh.marathicalendar.data.provider.CalendarDataProvider
import com.saurabh.marathicalendar.data.repository.CalendarRepository
import com.saurabh.marathicalendar.data.storage.NotesStorage
import com.saurabh.marathicalendar.data.storage.SettingsStorage

class MarathiCalendarApp : Application() {
    lateinit var repository: CalendarRepository
        private set

    lateinit var notesStorage: NotesStorage
        private set

    lateinit var settingsStorage: SettingsStorage
        private set

    override fun onCreate() {
        super.onCreate()
        val provider = CalendarDataProvider(applicationContext)
        repository = CalendarRepository(provider)
        notesStorage = NotesStorage(applicationContext)
        settingsStorage = SettingsStorage(applicationContext)
    }
}
