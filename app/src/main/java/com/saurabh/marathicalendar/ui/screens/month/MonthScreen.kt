package com.saurabh.marathicalendar.ui.screens.month

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.marathicalendar.data.model.DayData
import com.saurabh.marathicalendar.ui.components.*
import com.saurabh.marathicalendar.ui.screens.daydetail.DayDetailContent
import com.saurabh.marathicalendar.ui.theme.*
import com.saurabh.marathicalendar.util.MarathiConstants
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthScreen(
    viewModel: MonthViewModel,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = (uiState.currentMonth - 1).coerceIn(0, 11),
        pageCount = { 12 }
    )
    var selectedDayData by remember { mutableStateOf<DayData?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Sync pager → viewmodel when user swipes
    LaunchedEffect(pagerState.currentPage) {
        viewModel.loadMonth(pagerState.currentPage + 1)
    }

    // Handle programmatic navigation (today button / year-view tap)
    LaunchedEffect(uiState.requestedMonth) {
        uiState.requestedMonth?.let { month ->
            pagerState.animateScrollToPage(month - 1)
            viewModel.onRequestedMonthHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "मराठी दिनदर्शिका",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "२०२६",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    // Today button — only visible when NOT on today's month
                    val todayMonth = uiState.todayMonth
                    if (todayMonth != null && pagerState.currentPage + 1 != todayMonth) {
                        IconButton(onClick = { viewModel.navigateToMonth(todayMonth) }) {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = "आजचा दिवस",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "सेटिंग्ज",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SaffronPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            AppBottomNavBar(selectedTab = selectedTab, onTabSelected = onTabSelected)
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { pageIndex ->
                val isCurrentPage = pagerState.currentPage == pageIndex
                val pageOffset = (pagerState.currentPage - pageIndex).toFloat() +
                    pagerState.currentPageOffsetFraction

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            cameraDistance = 8f * density
                            if (pageOffset < 0f) {
                                transformOrigin = TransformOrigin(1f, 0.5f)
                                rotationY = 90f * abs(pageOffset).coerceIn(0f, 1f)
                            } else if (pageOffset > 0f) {
                                transformOrigin = TransformOrigin(0f, 0.5f)
                                rotationY = -90f * pageOffset.coerceIn(0f, 1f)
                            }
                            alpha = if (abs(pageOffset) >= 1f) 0f else 1f
                        }
                ) {
                    MonthPage(
                        monthIndex = pageIndex,
                        viewModel = viewModel,
                        isCurrentPage = isCurrentPage,
                        onDayClick = { dayData ->
                            selectedDayData = dayData
                            coroutineScope.launch { sheetState.show() }
                        },
                        onPreviousMonth = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage((pageIndex - 1).coerceAtLeast(0))
                            }
                        },
                        onNextMonth = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage((pageIndex + 1).coerceAtMost(11))
                            }
                        }
                    )
                }
            }
        }

        // Day Detail Bottom Sheet
        if (selectedDayData != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch { sheetState.hide() }
                    selectedDayData = null
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                DayDetailContent(
                    dayData = selectedDayData!!,
                    currentNote = uiState.selectedDayNote,
                    onSaveNote = { note -> viewModel.saveNote(note) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun MonthPage(
    monthIndex: Int,
    viewModel: MonthViewModel,
    isCurrentPage: Boolean,
    onDayClick: (DayData) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val monthData = if (isCurrentPage) uiState.monthData else null
    val todayDate = if (uiState.todayMonth == monthIndex + 1) uiState.todayDate else null
    val selectedDay = if (uiState.currentMonth == monthIndex + 1) uiState.selectedDay else null

    // Festivals to show in the strip for this month
    val festivalDays = remember(monthData) {
        monthData?.days?.filter {
            it.festivals.isNotEmpty() || it.isGovernmentHoliday
        }?.take(10) ?: emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        MonthHeader(
            monthNumber = monthIndex + 1,
            marathiMonthsActive = monthData?.marathiMonthsActive ?: emptyList(),
            onPreviousClick = onPreviousMonth,
            onNextClick = onNextMonth,
            canGoPrevious = monthIndex > 0,
            canGoNext = monthIndex < 11
        )

        // Upcoming / month festivals strip
        if (festivalDays.isNotEmpty()) {
            UpcomingFestivalsStrip(days = festivalDays, onDayClick = onDayClick)
        }

        WeekDayHeader()

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

        if (uiState.isLoading && isCurrentPage) {
            CalendarSkeleton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else if (monthData != null) {
            CalendarGrid(
                monthData = monthData,
                todayDate = todayDate,
                selectedDay = selectedDay,
                onDayClick = { day ->
                    viewModel.selectDay(day)
                    monthData.days.find { it.gregorianDate == day }?.let { onDayClick(it) }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            FestivalLegend(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun UpcomingFestivalsStrip(
    days: List<DayData>,
    onDayClick: (DayData) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { dayData ->
            val festival = dayData.festivals.firstOrNull()
            val color = festival?.type?.dotColor() ?: HolidayRed
            val name = festival?.name ?: dayData.governmentHolidayNameMarathi

            SuggestionChip(
                onClick = { onDayClick(dayData) },
                label = {
                    Text(
                        text = "${MarathiConstants.toMarathiNumber(dayData.gregorianDate)} – $name",
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = color.copy(alpha = 0.12f)
                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = true,
                    borderColor = color.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun FestivalLegend(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = SundayRed, label = "सुट्टी/रवि")
        LegendItem(color = FestivalOrange, label = "सण")
        LegendItem(color = VratGreen, label = "व्रत")
        LegendItem(color = AuspiciousGold, label = "धार्मिक")
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
