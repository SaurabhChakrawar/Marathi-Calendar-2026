package com.saurabh.marathicalendar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MarathiDateInfo(
    val marathiMonth: String,
    val marathiMonthEnglish: String,
    val paksha: String,
    val pakshaEnglish: String,
    val tithiNumber: Int,
    val tithiName: String,
    val tithiNameEnglish: String,
    val shakaSamvat: Int,
    val marathiDay: String
)
