package com.saurabh.marathicalendar.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class FestivalType {
    MAJOR_FESTIVAL,
    RELIGIOUS_OBSERVANCE,
    GOVERNMENT_HOLIDAY,
    REGIONAL_FESTIVAL,
    VRAT,
    ECLIPSE,
    SPECIAL_DAY
}

@Serializable
data class FestivalData(
    val name: String,
    val nameEnglish: String,
    val type: FestivalType,
    val description: String = "",
    val descriptionEnglish: String = ""
)
