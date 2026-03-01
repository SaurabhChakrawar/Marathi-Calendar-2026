package com.saurabh.marathicalendar.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saurabh.marathicalendar.data.model.PanchangData
import com.saurabh.marathicalendar.ui.theme.SaffronSurface
import com.saurabh.marathicalendar.util.MarathiConstants

@Composable
fun PanchangInfoCard(
    panchang: PanchangData,
    sunriseTime: String,
    sunsetTime: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SaffronSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = MarathiConstants.PANCHANG_LABEL,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            PanchangRow(
                label = MarathiConstants.TITHI_LABEL,
                value = panchang.tithi.name,
                subValue = if (panchang.tithi.endTime.isNotEmpty()) "पर्यंत ${panchang.tithi.endTime}" else ""
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            PanchangRow(
                label = MarathiConstants.NAKSHATRA_LABEL,
                value = panchang.nakshatra.name,
                subValue = if (panchang.nakshatra.endTime.isNotEmpty()) "पर्यंत ${panchang.nakshatra.endTime}" else ""
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            PanchangRow(
                label = MarathiConstants.YOGA_LABEL,
                value = panchang.yoga.name,
                subValue = if (panchang.yoga.endTime.isNotEmpty()) "पर्यंत ${panchang.yoga.endTime}" else ""
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            PanchangRow(
                label = MarathiConstants.KARANA_LABEL,
                value = "${panchang.karana.first} / ${panchang.karana.second}",
                subValue = ""
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            PanchangRow(
                label = MarathiConstants.RAHU_KALAM_LABEL,
                value = panchang.rahuKalam,
                subValue = ""
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SunTimeItem(
                    icon = Icons.Default.WbSunny,
                    label = MarathiConstants.SUNRISE_LABEL,
                    time = sunriseTime
                )
                SunTimeItem(
                    icon = Icons.Default.NightsStay,
                    label = MarathiConstants.SUNSET_LABEL,
                    time = sunsetTime
                )
            }
        }
    }
}

@Composable
private fun PanchangRow(label: String, value: String, subValue: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(80.dp)
        )
        Text(text = " : ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subValue.isNotEmpty()) {
                Text(
                    text = subValue,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SunTimeItem(icon: ImageVector, label: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
