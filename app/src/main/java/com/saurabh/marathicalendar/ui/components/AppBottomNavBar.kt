package com.saurabh.marathicalendar.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        tonalElevation = 4.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Filled.CalendarToday, contentDescription = null) },
            label = { Text("दिनदर्शिका") }
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Filled.ViewModule, contentDescription = null) },
            label = { Text("वर्ष") }
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
            label = { Text("सण") }
        )
    }
}
