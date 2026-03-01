package com.saurabh.marathicalendar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TithiInfo(
    val name: String,
    val nameEnglish: String,
    val endTime: String,
    val nextTithi: String = ""
)

@Serializable
data class NakshatraInfo(
    val name: String,
    val nameEnglish: String,
    val endTime: String
)

@Serializable
data class YogaInfo(
    val name: String,
    val nameEnglish: String,
    val endTime: String
)

@Serializable
data class KaranaInfo(
    val first: String,
    val firstEnglish: String,
    val second: String,
    val secondEnglish: String
)

@Serializable
data class PanchangData(
    val tithi: TithiInfo,
    val nakshatra: NakshatraInfo,
    val yoga: YogaInfo,
    val karana: KaranaInfo,
    val rahuKalam: String,
    val gulikaKalam: String
)
