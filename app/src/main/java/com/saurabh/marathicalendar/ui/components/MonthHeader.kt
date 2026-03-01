package com.saurabh.marathicalendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saurabh.marathicalendar.util.MarathiConstants

@Composable
fun MonthHeader(
    monthNumber: Int,
    marathiMonthsActive: List<String>,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    modifier: Modifier = Modifier
) {
    val marathiMonthName = MarathiConstants.GREGORIAN_MONTHS_MARATHI.getOrElse(monthNumber - 1) { "" }
    val englishMonthName = MarathiConstants.GREGORIAN_MONTHS_ENGLISH.getOrElse(monthNumber - 1) { "" }
    val hinduMonths = marathiMonthsActive.joinToString(" / ")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onPreviousClick,
                enabled = canGoPrevious
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "मागील महिना",
                    tint = if (canGoPrevious)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outlineVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = marathiMonthName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = englishMonthName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                if (hinduMonths.isNotEmpty()) {
                    Text(
                        text = hinduMonths,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            IconButton(
                onClick = onNextClick,
                enabled = canGoNext
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "पुढील महिना",
                    tint = if (canGoNext)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}
