# मराठी दिनदर्शिका २०२६ — Marathi Calendar 2026

A beautiful, offline-first Marathi calendar Android app for the year 2026, built with Jetpack Compose and Material 3.

[![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.1.0-blue?logo=kotlin)](https://kotlinlang.org)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-API%2024%20(Android%207.0)-orange)](https://developer.android.com/studio/releases/platforms)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## Screenshots

| Calendar | Day Detail | Festivals |
|----------|------------|-----------|
| Monthly calendar with Marathi tithi | Full panchang bottom sheet | Grouped festival list |

---

## Features

- 📅 **12-month swipeable calendar** with smooth 3D page-turn animation
- 🗓️ **Bilingual cells** — English date + Marathi tithi in every cell
- 🎉 **100+ festivals** — all Maharashtra government holidays included
- 🔵 **Colour-coded dots** — saffron (festival), red (holiday), green (vrat), gold (religious)
- ⭕ **Today highlight** — saffron circle with quick-jump button
- 📋 **Full panchang** — tithi, nakshatra, yoga, karana, sunrise/sunset on day tap
- 📝 **Daily notes** — write a short note (50 chars) on any day
- 📤 **Share** — share any day's panchang via any app
- 🗓️ **Year overview** — 3-column grid of all 12 months at a glance
- 🎊 **Festivals screen** — filterable, grouped list with sticky headers
- 🌙 **Dark mode** toggle
- 🔤 **Font size** — Small / Medium / Large
- 📴 **Offline-first** — zero internet required, all data bundled (~550 KB JSON)

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin 2.1.0 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM, single-module |
| Async | Kotlin Coroutines + Flow |
| JSON | kotlinx.serialization |
| Persistence | SharedPreferences |
| Min SDK | API 24 (Android 7.0) |
| Target SDK | API 35 (Android 15) |
| Build | AGP 8.7.3 |

---

## Project Structure

```
app/src/main/
├── assets/calendar_data/       # 12 JSON files — all 365 days
├── java/com/saurabh/marathicalendar/
│   ├── data/
│   │   ├── model/              # DayData, MonthData, PanchangData, etc.
│   │   ├── provider/           # CalendarDataProvider (JSON reader + cache)
│   │   ├── repository/         # CalendarRepository
│   │   └── storage/            # NotesStorage, SettingsStorage, FontSizeOption
│   ├── ui/
│   │   ├── components/         # CalendarGrid, DayCell, PanchangInfoCard, etc.
│   │   ├── screens/
│   │   │   ├── month/          # MonthScreen, MonthViewModel, MonthUiState
│   │   │   ├── year/           # YearOverviewScreen
│   │   │   ├── festivals/      # FestivalsScreen
│   │   │   └── daydetail/      # DayDetailContent (bottom sheet)
│   │   └── theme/              # Color, Type, Shape, Theme (saffron/orange)
│   └── util/                   # MarathiConstants, DateUtils
└── res/
    ├── mipmap-*/               # Launcher icons (mdpi → xxxhdpi)
    ├── drawable-*/             # Splash logo icons
    └── values/                 # colors, strings, themes
```

---

## Calendar Data

- **Location reference:** Pune, Maharashtra (18.5°N, 73.8°E) for sunrise/sunset
- **Samvat:** Shaka Samvat 1947 → 1948 from Gudi Padwa (March 19, 2026)
- **Key festivals:**
  - Gudi Padwa — March 19
  - Ganesh Chaturthi — September 14
  - Diwali — November 8
- **Data source:** Python-generated JSON with Marathi tithi, nakshatra, panchang

---

## Building the App

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK with API 35

### Debug Build (Open in Android Studio)
```bash
# Clone the repo
git clone https://github.com/SaurabhChakrawar/Marathi-Calendar-2026.git
cd Marathi-Calendar-2026

# Open in Android Studio → File → Open → select the folder
# Let Gradle sync, then press ▶ Run
```

### Release Build (Signed AAB)

1. Copy the keystore template and fill in your credentials:
```bash
cp keystore.properties.template keystore.properties
# Edit keystore.properties with your keystore path and passwords
```

2. Generate a signed release bundle:
```bash
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab
```

---

## Signing

Release signing is configured via `keystore.properties` (not committed to git).

```properties
# keystore.properties (DO NOT COMMIT)
storeFile=../your_keystore.jks
storePassword=your_store_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

See `keystore.properties.template` for the template.

---

## Privacy

This app:
- ✅ Collects **no personal data**
- ✅ Requires **no internet permission**
- ✅ Has **no analytics or tracking**
- ✅ Stores only your notes and preferences locally

Full privacy policy: [privacy_policy.html](privacy_policy.html)
Hosted at: https://saurabhchakrawar.github.io/Marathi-Calendar-2026/privacy_policy.html

---

## License

Copyright (c) 2026 Saurabh Chakrawar. See [LICENSE](LICENSE) for details.
