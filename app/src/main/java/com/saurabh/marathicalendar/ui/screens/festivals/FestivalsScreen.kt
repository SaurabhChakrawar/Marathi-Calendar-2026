package com.saurabh.marathicalendar.ui.screens.festivals

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.marathicalendar.data.model.DayData
import com.saurabh.marathicalendar.data.model.FestivalType
import com.saurabh.marathicalendar.ui.components.AppBottomNavBar
import com.saurabh.marathicalendar.ui.components.dotColor
import com.saurabh.marathicalendar.ui.components.labelMarathi
import com.saurabh.marathicalendar.ui.screens.month.MonthViewModel
import com.saurabh.marathicalendar.ui.theme.*
import com.saurabh.marathicalendar.util.MarathiConstants

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FestivalsScreen(
    viewModel: MonthViewModel,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf<FestivalType?>(null) }
    var showOnlyHolidays by remember { mutableStateOf(false) }

    // All days that have festivals or are holidays, sorted by month+day
    val allEventDays = remember(uiState.allMonthsData) {
        uiState.allMonthsData.values
            .sortedBy { it.monthNumber }
            .flatMap { monthData ->
                monthData.days
                    .filter { it.festivals.isNotEmpty() || it.isGovernmentHoliday }
                    .map { monthData.monthNumber to it }
            }
    }

    val filteredDays = remember(allEventDays, selectedFilter, showOnlyHolidays) {
        allEventDays.filter { (_, day) ->
            when {
                showOnlyHolidays -> day.isGovernmentHoliday
                selectedFilter != null -> day.festivals.any { it.type == selectedFilter }
                else -> true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "सण / सुट्ट्या",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "२०२६",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Settings, contentDescription = "सेटिंग्ज", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SaffronPrimary)
            )
        },
        bottomBar = {
            AppBottomNavBar(selectedTab = selectedTab, onTabSelected = onTabSelected)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == null && !showOnlyHolidays,
                        onClick = { selectedFilter = null; showOnlyHolidays = false },
                        label = { Text("सर्व") }
                    )
                }
                item {
                    FilterChip(
                        selected = showOnlyHolidays,
                        onClick = { showOnlyHolidays = !showOnlyHolidays; selectedFilter = null },
                        label = { Text("सरकारी सुट्टी") }
                    )
                }
                items(listOf(FestivalType.MAJOR_FESTIVAL, FestivalType.VRAT, FestivalType.RELIGIOUS_OBSERVANCE)) { type ->
                    FilterChip(
                        selected = selectedFilter == type,
                        onClick = {
                            selectedFilter = if (selectedFilter == type) null else type
                            showOnlyHolidays = false
                        },
                        label = { Text(type.labelMarathi()) }
                    )
                }
            }

            HorizontalDivider()

            if (allEventDays.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = SaffronPrimary)
                }
            } else if (filteredDays.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("कोणतेही सण सापडले नाहीत", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                val grouped = filteredDays.groupBy { it.first }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    grouped.forEach { (month, days) ->
                        val marathiMonthName = MarathiConstants.GREGORIAN_MONTHS_MARATHI.getOrElse(month - 1) { "" }
                        val englishMonthName = MarathiConstants.GREGORIAN_MONTHS_ENGLISH.getOrElse(month - 1) { "" }
                        stickyHeader {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SaffronPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = MarathiConstants.toMarathiNumber(month),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = "$marathiMonthName  ·  $englishMonthName",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = "${days.size} दिवस",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        items(days) { (_, dayData) ->
                            FestivalDayRow(dayData = dayData)
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FestivalDayRow(dayData: DayData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Date column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(42.dp)
        ) {
            Text(
                text = dayData.gregorianDate.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = if (dayData.dayOfWeek == 1 || dayData.isGovernmentHoliday)
                    SundayRed
                else
                    MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = MarathiConstants.DAYS_SHORT_MARATHI.getOrElse(dayData.dayOfWeek - 1) { "" },
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Events column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (dayData.isGovernmentHoliday && dayData.governmentHolidayNameMarathi.isNotEmpty()) {
                EventRow(
                    name = dayData.governmentHolidayNameMarathi,
                    color = HolidayRed,
                    typeLabel = "सरकारी सुट्टी"
                )
            }
            dayData.festivals.forEach { festival ->
                EventRow(
                    name = festival.name,
                    color = festival.type.dotColor(),
                    typeLabel = festival.type.labelMarathi()
                )
            }
        }
    }
}

@Composable
private fun EventRow(name: String, color: Color, typeLabel: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = typeLabel,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}
