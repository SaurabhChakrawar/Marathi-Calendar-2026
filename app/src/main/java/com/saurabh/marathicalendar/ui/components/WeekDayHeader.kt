package com.saurabh.marathicalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.marathicalendar.ui.theme.SaffronPrimary
import com.saurabh.marathicalendar.ui.theme.SundayRed
import com.saurabh.marathicalendar.util.MarathiConstants

@Composable
fun WeekDayHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF8F0))
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MarathiConstants.DAYS_SHORT_MARATHI.forEachIndexed { index, day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = when (index) {
                    0 -> SundayRed
                    6 -> SaffronPrimary
                    else -> Color(0xFF5D4037)
                }
            )
        }
    }
}
