package com.saurabh.marathicalendar.ui.screens.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.saurabh.marathicalendar.data.model.MonthData
import com.saurabh.marathicalendar.data.repository.CalendarRepository
import com.saurabh.marathicalendar.data.storage.NotesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MonthViewModel(
    private val repository: CalendarRepository,
    private val notesStorage: NotesStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonthUiState())
    val uiState: StateFlow<MonthUiState> = _uiState.asStateFlow()

    private val monthCache = mutableMapOf<Int, MonthData>()

    init {
        val today = Calendar.getInstance()
        val todayMonth = today.get(Calendar.MONTH) + 1
        val todayDate = today.get(Calendar.DAY_OF_MONTH)
        val todayYear = today.get(Calendar.YEAR)

        val initialMonth = if (todayYear == 2026) todayMonth else 1

        _uiState.value = _uiState.value.copy(
            currentMonth = initialMonth,
            todayMonth = if (todayYear == 2026) todayMonth else null,
            todayDate = if (todayYear == 2026) todayDate else null
        )

        loadMonth(initialMonth)

        viewModelScope.launch(Dispatchers.IO) {
            repository.preload()
            val allMonths = repository.getAllMonths().associateBy { it.monthNumber }
            _uiState.update { it.copy(allMonthsData = allMonths) }
        }
    }

    fun loadMonth(month: Int) {
        if (month < 1 || month > 12) return
        viewModelScope.launch {
            val cached = monthCache[month]
            if (cached != null) {
                _uiState.value = _uiState.value.copy(
                    currentMonth = month,
                    monthData = cached,
                    selectedDay = null,
                    selectedDayNote = "",
                    isLoading = false
                )
                return@launch
            }
            _uiState.value = _uiState.value.copy(currentMonth = month, isLoading = true)
            val data = withContext(Dispatchers.IO) { repository.getMonth(month) }
            if (data != null) monthCache[month] = data
            _uiState.value = _uiState.value.copy(
                monthData = data,
                selectedDay = null,
                selectedDayNote = "",
                isLoading = false
            )
        }
    }

    fun selectDay(day: Int) {
        val month = _uiState.value.currentMonth
        val note = notesStorage.getNote(month, day)
        _uiState.value = _uiState.value.copy(
            selectedDay = day,
            selectedDayNote = note
        )
    }

    fun saveNote(note: String) {
        val month = _uiState.value.currentMonth
        val day = _uiState.value.selectedDay ?: return
        notesStorage.saveNote(month, day, note)
        _uiState.value = _uiState.value.copy(selectedDayNote = note.trim())
    }

    fun navigateToMonth(month: Int) {
        _uiState.update { it.copy(requestedMonth = month, currentMonth = month) }
        loadMonth(month)
    }

    fun onRequestedMonthHandled() {
        _uiState.update { it.copy(requestedMonth = null) }
    }

    fun nextMonth() {
        val next = _uiState.value.currentMonth + 1
        if (next <= 12) loadMonth(next)
    }

    fun previousMonth() {
        val prev = _uiState.value.currentMonth - 1
        if (prev >= 1) loadMonth(prev)
    }

    class Factory(
        private val repository: CalendarRepository,
        private val notesStorage: NotesStorage
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MonthViewModel(repository, notesStorage) as T
        }
    }
}
