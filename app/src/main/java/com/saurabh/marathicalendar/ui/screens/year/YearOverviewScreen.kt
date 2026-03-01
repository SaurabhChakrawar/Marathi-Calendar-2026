package com.saurabh.marathicalendar.ui.screens.year

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.marathicalendar.data.model.MonthData
import com.saurabh.marathicalendar.ui.components.AppBottomNavBar
import com.saurabh.marathicalendar.ui.screens.month.MonthViewModel
import com.saurabh.marathicalendar.ui.theme.*
import com.saurabh.marathicalendar.util.MarathiConstants
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearOverviewScreen(
    viewModel: MonthViewModel,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onMonthClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "वर्ष दर्शन",
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
        if (uiState.allMonthsData.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SaffronPrimary)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(12) { monthIndex ->
                    MonthMiniCard(
                        monthIndex = monthIndex,
                        monthData = uiState.allMonthsData[monthIndex + 1],
                        isCurrentMonth = uiState.todayMonth == monthIndex + 1,
                        onClick = { onMonthClick(monthIndex + 1) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthMiniCard(
    monthIndex: Int,
    monthData: MonthData?,
    isCurrentMonth: Boolean,
    onClick: () -> Unit
) {
    val marathiName = MarathiConstants.GREGORIAN_MONTHS_MARATHI.getOrElse(monthIndex) { "" }
    val englishName = MarathiConstants.GREGORIAN_MONTHS_ENGLISH.getOrElse(monthIndex) { "" }
    val holidayCount = monthData?.days?.count { it.isGovernmentHoliday } ?: 0
    val festivalCount = monthData?.days?.count { it.festivals.isNotEmpty() } ?: 0

    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(0.78f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentMonth)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (isCurrentMonth)
            BorderStroke(2.dp, SaffronPrimary)
        else
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Month number
            Text(
                text = MarathiConstants.toMarathiNumber(monthIndex + 1),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isCurrentMonth) SaffronPrimary else MaterialTheme.colorScheme.onSurface
            )

            // Marathi name
            Text(
                text = marathiName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            // English name
            Text(
                text = englishName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Counts row
            if (monthData != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (holidayCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(HolidayRed)
                            )
                            Text(text = holidayCount.toString(), fontSize = 9.sp, color = HolidayRed)
                        }
                    }
                    if (festivalCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(FestivalOrange)
                            )
                            Text(text = festivalCount.toString(), fontSize = 9.sp, color = FestivalOrange)
                        }
                    }
                }
            }
        }
    }
}
