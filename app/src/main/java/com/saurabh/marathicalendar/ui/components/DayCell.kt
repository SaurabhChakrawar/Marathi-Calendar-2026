package com.saurabh.marathicalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.marathicalendar.data.model.DayData
import com.saurabh.marathicalendar.data.model.FestivalType
import com.saurabh.marathicalendar.ui.theme.*
import com.saurabh.marathicalendar.util.MarathiConstants

@Composable
fun DayCell(
    dayData: DayData,
    isToday: Boolean,
    isSelected: Boolean,
    hasNote: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSunday = dayData.dayOfWeek == 1
    val isSaturday = dayData.dayOfWeek == 7
    val isHoliday = dayData.isGovernmentHoliday
    val hasMajorFestival = dayData.festivals.any { it.type == FestivalType.MAJOR_FESTIVAL }

    val dateTextColor = when {
        isToday -> Color.White
        isSelected -> SaffronPrimary
        isHoliday || isSunday -> SundayRed
        isSaturday -> SaffronPrimary
        else -> Color(0xFF1A1A1A)
    }

    val cellBackground = when {
        hasMajorFestival -> Color(0xFFFFF3E0)
        isHoliday -> Color(0xFFFFFBF5)
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(cellBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.TopCenter
    ) {
        // Notes indicator — small triangle in top-right corner
        if (hasNote) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .size(6.dp)
                    .background(Color(0xFF0288D1), RoundedCornerShape(1.dp))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 1.dp, vertical = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Date number — circle for today, outlined circle for selected
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(28.dp)
                    .then(
                        when {
                            isToday -> Modifier
                                .clip(CircleShape)
                                .background(SaffronPrimary)
                            isSelected -> Modifier
                                .clip(CircleShape)
                                .border(1.5.dp, SaffronPrimary, CircleShape)
                            else -> Modifier
                        }
                    )
            ) {
                Text(
                    text = dayData.gregorianDate.toString(),
                    fontWeight = if (isToday || isHoliday || isSunday) FontWeight.Bold else FontWeight.Medium,
                    color = dateTextColor,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }

            // Marathi tithi: "शु.१" / "कृ.१०" / "पौर्णिमा" / "अमावस्या"
            Text(
                text = buildTithiAbbr(dayData),
                color = Color(0xFF7B5E57),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontSize = 7.5.sp
            )

            // Event dots
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(7.dp)
            ) {
                if (isHoliday) {
                    Dot(color = HolidayRed)
                }
                dayData.festivals.take(2).forEach { festival ->
                    Dot(color = festival.type.dotColor())
                }
            }
        }
    }
}

@Composable
private fun Dot(color: Color) {
    Box(
        modifier = Modifier
            .padding(horizontal = 1.dp)
            .size(5.dp)
            .clip(CircleShape)
            .background(color)
    )
}

private fun buildTithiAbbr(dayData: DayData): String {
    val paksha = if (dayData.marathiDate.paksha == "शुक्ल") "शु" else "कृ"
    val tithi = dayData.marathiDate.tithiName
    val num = dayData.marathiDate.tithiNumber
    return when {
        tithi == "पूर्णिमा" -> "पौर्णिमा"
        tithi == "अमावस्या" -> "अमावस्या"
        num > 0 -> "$paksha.${MarathiConstants.toMarathiNumber(num)}"
        else -> "$paksha.${tithi.take(3)}"
    }
}

fun FestivalType.dotColor(): Color = when (this) {
    FestivalType.MAJOR_FESTIVAL -> FestivalOrange
    FestivalType.RELIGIOUS_OBSERVANCE -> AuspiciousGold
    FestivalType.GOVERNMENT_HOLIDAY -> HolidayRed
    FestivalType.REGIONAL_FESTIVAL -> Color(0xFF7B1FA2)
    FestivalType.VRAT -> VratGreen
    FestivalType.ECLIPSE -> Color(0xFF37474F)
    FestivalType.SPECIAL_DAY -> Color(0xFF0288D1)
}
