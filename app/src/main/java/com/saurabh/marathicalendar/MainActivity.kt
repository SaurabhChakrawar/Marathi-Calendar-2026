package com.saurabh.marathicalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saurabh.marathicalendar.data.storage.FontSizeOption
import com.saurabh.marathicalendar.ui.screens.festivals.FestivalsScreen
import com.saurabh.marathicalendar.ui.screens.month.MonthScreen
import com.saurabh.marathicalendar.ui.screens.month.MonthViewModel
import com.saurabh.marathicalendar.ui.screens.year.YearOverviewScreen
import com.saurabh.marathicalendar.ui.theme.MarathiCalendarTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as MarathiCalendarApp

        setContent {
            var isDarkMode by remember { mutableStateOf(app.settingsStorage.isDarkMode()) }
            var fontSizeOption by remember { mutableStateOf(app.settingsStorage.getFontSize()) }
            var selectedTab by remember { mutableIntStateOf(0) }
            var showSettingsDialog by remember { mutableStateOf(false) }

            // Splash: visible for 1.5 s then fades out over 0.5 s
            var showSplash by remember { mutableStateOf(true) }
            val splashAlpha by animateFloatAsState(
                targetValue = if (showSplash) 1f else 0f,
                animationSpec = tween(durationMillis = 500),
                label = "splashFade"
            )

            LaunchedEffect(Unit) {
                delay(1500L)
                showSplash = false
            }

            MarathiCalendarTheme(isDarkMode = isDarkMode, fontScale = fontSizeOption.scale) {

                Box(modifier = Modifier.fillMaxSize()) {

                    val viewModel: MonthViewModel = viewModel(
                        factory = MonthViewModel.Factory(app.repository, app.notesStorage)
                    )

                    when (selectedTab) {
                        0 -> MonthScreen(
                            viewModel = viewModel,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it },
                            onSettingsClick = { showSettingsDialog = true }
                        )
                        1 -> YearOverviewScreen(
                            viewModel = viewModel,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it },
                            onMonthClick = { month ->
                                viewModel.navigateToMonth(month)
                                selectedTab = 0
                            },
                            onSettingsClick = { showSettingsDialog = true }
                        )
                        2 -> FestivalsScreen(
                            viewModel = viewModel,
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it },
                            onSettingsClick = { showSettingsDialog = true }
                        )
                    }

                    if (showSettingsDialog) {
                        SettingsDialog(
                            isDarkMode = isDarkMode,
                            fontSizeOption = fontSizeOption,
                            onDarkModeChange = { value ->
                                isDarkMode = value
                                app.settingsStorage.setDarkMode(value)
                            },
                            onFontSizeChange = { option ->
                                fontSizeOption = option
                                app.settingsStorage.setFontSize(option)
                            },
                            onDismiss = { showSettingsDialog = false }
                        )
                    }

                    // Compose splash overlay — matches Android 12 system splash size/shape
                    // so the transition between system splash and this is seamless.
                    if (splashAlpha > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(splashAlpha)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_splash_logo),
                                contentDescription = "मराठी दिनदर्शिका २०२६",
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(percent = 20))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsDialog(
    isDarkMode: Boolean,
    fontSizeOption: FontSizeOption,
    onDarkModeChange: (Boolean) -> Unit,
    onFontSizeChange: (FontSizeOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("सेटिंग्ज") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("डार्क मोड", style = MaterialTheme.typography.bodyLarge)
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onDarkModeChange
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("अक्षर आकार", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FontSizeOption.entries.forEach { option ->
                            FilterChip(
                                selected = fontSizeOption == option,
                                onClick = { onFontSizeChange(option) },
                                label = { Text(option.labelMarathi) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("बंद करा") }
        }
    )
}
