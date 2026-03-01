package com.saurabh.marathicalendar.ui.screens.daydetail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saurabh.marathicalendar.data.model.DayData
import com.saurabh.marathicalendar.ui.components.FestivalList
import com.saurabh.marathicalendar.ui.components.PanchangInfoCard
import com.saurabh.marathicalendar.ui.theme.*
import com.saurabh.marathicalendar.util.MarathiConstants

private const val MAX_NOTE_LENGTH = 50

@Composable
fun DayDetailContent(
    dayData: DayData,
    currentNote: String,
    onSaveNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val md = dayData.marathiDate
    val englishMonthName = MarathiConstants.GREGORIAN_MONTHS_ENGLISH.getOrElse(dayData.gregorianMonth - 1) { "" }
    val marathiMonthName = MarathiConstants.GREGORIAN_MONTHS_MARATHI.getOrElse(dayData.gregorianMonth - 1) { "" }
    val dayFullMarathi = MarathiConstants.DAYS_FULL_MARATHI.getOrElse(dayData.dayOfWeek - 1) { "" }

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Date header card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SaffronPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            val shareText = buildShareText(dayData, englishMonthName, dayFullMarathi, currentNote)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "शेअर करा"))
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "शेअर करा",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    text = "${dayData.gregorianDate} $englishMonthName 2026",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    text = "$dayFullMarathi, ${MarathiConstants.toMarathiNumber(dayData.gregorianDate)} $marathiMonthName",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                val hinduDateStr = buildString {
                    append(md.marathiMonth)
                    append(" ")
                    append(md.paksha)
                    append(" ")
                    append(md.tithiName)
                    append(" | ")
                    append(MarathiConstants.SHAKA_SAMVAT_LABEL)
                    append(" ")
                    append(MarathiConstants.toMarathiNumber(md.shakaSamvat))
                }
                Text(
                    text = hinduDateStr,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                if (dayData.isGovernmentHoliday && dayData.governmentHolidayNameMarathi.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🏛 ${dayData.governmentHolidayNameMarathi}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Panchang card
        PanchangInfoCard(
            panchang = dayData.panchang,
            sunriseTime = dayData.sunriseTime,
            sunsetTime = dayData.sunsetTime
        )

        // Festivals / observances
        if (dayData.festivals.isNotEmpty() || (dayData.isGovernmentHoliday && dayData.governmentHolidayNameMarathi.isNotEmpty())) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = MarathiConstants.FESTIVAL_LABEL,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FestivalList(
                        festivals = dayData.festivals,
                        isGovernmentHoliday = dayData.isGovernmentHoliday,
                        holidayNameMarathi = dayData.governmentHolidayNameMarathi
                    )
                }
            }
        }

        // Notes card
        NoteCard(
            currentNote = currentNote,
            onSaveNote = onSaveNote
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun NoteCard(
    currentNote: String,
    onSaveNote: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var draftNote by remember(currentNote) { mutableStateOf(currentNote) }
    val focusRequester = remember { FocusRequester() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✏️ माझी नोंद",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037)
                )
                if (!isEditing && currentNote.isNotEmpty()) {
                    IconButton(
                        onClick = { isEditing = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "नोंद बदला",
                            tint = SaffronPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = draftNote,
                    onValueChange = { if (it.length <= MAX_NOTE_LENGTH) draftNote = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = {
                        Text(
                            text = "इथे नोंद लिहा...",
                            color = Color(0xFFBCAAA4),
                            fontSize = 13.sp
                        )
                    },
                    maxLines = 3,
                    supportingText = {
                        Text(
                            text = "${draftNote.length}/$MAX_NOTE_LENGTH",
                            color = if (draftNote.length >= MAX_NOTE_LENGTH) SundayRed
                            else Color(0xFF9E9E9E)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SaffronPrimary,
                        unfocusedBorderColor = Color(0xFFD7CCC8)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        draftNote = currentNote
                        isEditing = false
                    }) {
                        Text(
                            text = "रद्द करा",
                            color = Color(0xFF9E9E9E)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSaveNote(draftNote)
                            isEditing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "जतन करा",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                if (currentNote.isEmpty()) {
                    TextButton(
                        onClick = { isEditing = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "+ नोंद जोडा",
                            color = SaffronPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Text(
                        text = currentNote,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4E342E),
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

private fun buildShareText(
    dayData: DayData,
    englishMonthName: String,
    dayFullMarathi: String,
    note: String
): String = buildString {
    val md = dayData.marathiDate
    append("📅 ${dayData.gregorianDate} $englishMonthName 2026\n")
    append("$dayFullMarathi\n\n")
    append("🗓️ ${md.marathiMonth} ${md.paksha} ${md.tithiName}\n")
    append("⛅ सूर्योदय: ${dayData.sunriseTime} | सूर्यास्त: ${dayData.sunsetTime}\n")
    if (dayData.festivals.isNotEmpty()) {
        append("\n🎉 ")
        append(dayData.festivals.joinToString(", ") { it.name })
    }
    if (dayData.isGovernmentHoliday && dayData.governmentHolidayNameMarathi.isNotEmpty()) {
        append("\n🏛 ${dayData.governmentHolidayNameMarathi}")
    }
    if (note.isNotEmpty()) {
        append("\n📝 $note")
    }
    append("\n\n— मराठी दिनदर्शिका २०२६")
}
