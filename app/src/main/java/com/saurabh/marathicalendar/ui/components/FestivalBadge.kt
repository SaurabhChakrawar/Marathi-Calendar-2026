package com.saurabh.marathicalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.marathicalendar.data.model.FestivalData
import com.saurabh.marathicalendar.data.model.FestivalType
import com.saurabh.marathicalendar.ui.theme.HolidayRed

@Composable
fun FestivalList(
    festivals: List<FestivalData>,
    isGovernmentHoliday: Boolean,
    holidayNameMarathi: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (isGovernmentHoliday && holidayNameMarathi.isNotEmpty()) {
            FestivalChip(
                name = holidayNameMarathi,
                color = HolidayRed,
                label = "सरकारी सुट्टी"
            )
        }
        festivals.forEach { festival ->
            FestivalChip(
                name = festival.name,
                color = festival.type.dotColor(),
                label = festival.type.labelMarathi(),
                description = festival.description
            )
        }
    }
}

@Composable
private fun FestivalChip(
    name: String,
    color: Color,
    label: String,
    description: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

fun FestivalType.labelMarathi(): String = when (this) {
    FestivalType.MAJOR_FESTIVAL -> "सण"
    FestivalType.RELIGIOUS_OBSERVANCE -> "धार्मिक"
    FestivalType.GOVERNMENT_HOLIDAY -> "सुट्टी"
    FestivalType.REGIONAL_FESTIVAL -> "उत्सव"
    FestivalType.VRAT -> "व्रत"
    FestivalType.ECLIPSE -> "ग्रहण"
    FestivalType.SPECIAL_DAY -> "विशेष"
}
