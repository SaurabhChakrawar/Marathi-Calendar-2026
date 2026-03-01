package com.saurabh.marathicalendar.util

object MarathiConstants {

    val GREGORIAN_MONTHS_MARATHI = listOf(
        "जानेवारी", "फेब्रुवारी", "मार्च", "एप्रिल",
        "मे", "जून", "जुलै", "ऑगस्ट",
        "सप्टेंबर", "ऑक्टोबर", "नोव्हेंबर", "डिसेंबर"
    )

    val GREGORIAN_MONTHS_ENGLISH = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    val HINDU_MONTHS = listOf(
        "चैत्र", "वैशाख", "ज्येष्ठ", "आषाढ",
        "श्रावण", "भाद्रपद", "आश्विन", "कार्तिक",
        "मार्गशीर्ष", "पौष", "माघ", "फाल्गुन"
    )

    val DAYS_SHORT_MARATHI = listOf(
        "रवि", "सोम", "मंगळ", "बुध", "गुरु", "शुक्र", "शनि"
    )

    val DAYS_FULL_MARATHI = listOf(
        "रविवार", "सोमवार", "मंगळवार", "बुधवार",
        "गुरुवार", "शुक्रवार", "शनिवार"
    )

    val DAYS_FULL_ENGLISH = listOf(
        "Sunday", "Monday", "Tuesday", "Wednesday",
        "Thursday", "Friday", "Saturday"
    )

    val TITHI_NAMES_MARATHI = listOf(
        "प्रतिपदा", "द्वितीया", "तृतीया", "चतुर्थी",
        "पंचमी", "षष्ठी", "सप्तमी", "अष्टमी",
        "नवमी", "दशमी", "एकादशी", "द्वादशी",
        "त्रयोदशी", "चतुर्दशी", "पूर्णिमा/अमावस्या"
    )

    val TITHI_NAMES_ENGLISH = listOf(
        "Pratipada", "Dwitiya", "Tritiya", "Chaturthi",
        "Panchami", "Shashthi", "Saptami", "Ashtami",
        "Navami", "Dashami", "Ekadashi", "Dwadashi",
        "Trayodashi", "Chaturdashi", "Purnima/Amavasya"
    )

    val NAKSHATRAS_MARATHI = listOf(
        "अश्विनी", "भरणी", "कृत्तिका", "रोहिणी",
        "मृगशीर्ष", "आर्द्रा", "पुनर्वसु", "पुष्य",
        "आश्लेषा", "मघा", "पूर्वा फाल्गुनी", "उत्तरा फाल्गुनी",
        "हस्त", "चित्रा", "स्वाती", "विशाखा",
        "अनुराधा", "ज्येष्ठा", "मूळ", "पूर्वाषाढा",
        "उत्तराषाढा", "श्रवण", "धनिष्ठा", "शतभिषा",
        "पूर्वाभाद्रपदा", "उत्तराभाद्रपदा", "रेवती"
    )

    val YOGAS_MARATHI = listOf(
        "विष्कंभ", "प्रीती", "आयुष्मान", "सौभाग्य",
        "शोभन", "अतिगंड", "सुकर्मा", "धृती",
        "शूल", "गंड", "वृद्धी", "ध्रुव",
        "व्याघात", "हर्षण", "वज्र", "सिद्धी",
        "व्यतीपात", "वरीयान", "परिघ", "शिव",
        "सिद्ध", "साध्य", "शुभ", "शुक्ल",
        "ब्रह्म", "इंद्र", "वैधृती"
    )

    val KARANAS_MARATHI = listOf(
        "बव", "बालव", "कौलव", "तैतिल",
        "गर", "वणिज", "विष्टी",
        "शकुनी", "चतुष्पाद", "नाग", "किंस्तुघ्न"
    )

    const val SHUKLA_PAKSHA = "शुक्ल"
    const val KRISHNA_PAKSHA = "कृष्ण"
    const val PURNIMA = "पूर्णिमा"
    const val AMAVASYA = "अमावस्या"

    const val APP_TITLE = "मराठी दिनदर्शिका २०२६"
    const val PANCHANG_LABEL = "पंचांग"
    const val TITHI_LABEL = "तिथी"
    const val NAKSHATRA_LABEL = "नक्षत्र"
    const val YOGA_LABEL = "योग"
    const val KARANA_LABEL = "करण"
    const val SUNRISE_LABEL = "सूर्योदय"
    const val SUNSET_LABEL = "सूर्यास्त"
    const val FESTIVAL_LABEL = "सण / उत्सव"
    const val HOLIDAY_LABEL = "सरकारी सुट्टी"
    const val RAHU_KALAM_LABEL = "राहूकाळ"
    const val GULIKA_LABEL = "गुळिककाळ"
    const val SHAKA_SAMVAT_LABEL = "शके"
    const val PAKSHA_LABEL = "पक्ष"

    private val MARATHI_DIGITS = charArrayOf(
        '०', '१', '२', '३', '४', '५', '६', '७', '८', '९'
    )

    fun toMarathiNumber(number: Int): String =
        number.toString().map { ch ->
            if (ch.isDigit()) MARATHI_DIGITS[ch - '0'] else ch
        }.joinToString("")

    fun dayOfWeekMarathi(dayOfWeek: Int): String =
        DAYS_FULL_MARATHI.getOrElse(dayOfWeek - 1) { "" }

    fun dayOfWeekShortMarathi(dayOfWeek: Int): String =
        DAYS_SHORT_MARATHI.getOrElse(dayOfWeek - 1) { "" }
}
