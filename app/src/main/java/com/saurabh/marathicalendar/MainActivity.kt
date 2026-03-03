package com.saurabh.marathicalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
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
    val uriHandler = LocalUriHandler.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("सेटिंग्ज") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                HorizontalDivider()

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "माहिती व स्रोत",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        "या अॅपमधील शासकीय सुट्ट्यांची माहिती महाराष्ट्र शासनाच्या अधिकृत राजपत्रावर आधारित आहे. पंचांग माहिती दृक् पंचांगावर आधारित आहे.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    SourceLink(
                        label = "महाराष्ट्र शासन राजपत्र (सार्वजनिक सुट्ट्या)",
                        url = "https://gr.maharashtra.gov.in",
                        onClick = { uriHandler.openUri(it) }
                    )
                    SourceLink(
                        label = "महाराष्ट्र शासन — सामान्य प्रशासन विभाग",
                        url = "https://gad.maharashtra.gov.in",
                        onClick = { uriHandler.openUri(it) }
                    )
                    SourceLink(
                        label = "दृक् पंचांग (पंचांग गणना)",
                        url = "https://www.drikpanchang.com",
                        onClick = { uriHandler.openUri(it) }
                    )
                    Text(
                        "हे अॅप महाराष्ट्र शासनाशी संलग्न नाही. सुट्ट्यांची यादी शासकीय राजपत्र (GR) वरून संकलित केली आहे.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("बंद करा") }
        }
    )
}

@Composable
private fun SourceLink(
    label: String,
    url: String,
    onClick: (String) -> Unit
) {
    Text(
        text = "🔗 $label",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(url) }
            .padding(vertical = 4.dp)
    )
}
