# Changelog

All notable changes to **मराठी दिनदर्शिका २०२६** are documented here.

## [1.0.0] — 2026-03-01 — Initial Release

### Added
- 12-month swipeable calendar with HorizontalPager and 3D page-turn animation
- Bilingual day cells: English date + Marathi tithi + colour-coded festival dots
- Today highlighted with saffron circle; quick-jump button in top bar
- Modal bottom sheet on day tap: full panchang, sunrise/sunset, festivals, notes, share
- Year Overview screen: 3-column grid of all 12 months
- Festivals screen: filterable, grouped list with sticky month headers
- Bottom navigation: Calendar | Year | Festivals
- Dark mode toggle in settings dialog
- Font size preference: Small / Medium / Large
- Daily notes (50 characters per day) via SharedPreferences
- Share button: share any day's panchang via WhatsApp, SMS, email, etc.
- 100+ festivals including all Maharashtra government holidays
- Shaka Samvat tithi for all 365 days of 2026
- Offline-first: all data bundled as JSON (~550 KB), no internet required
- Supports Android 7.0 (API 24) and above

### Calendar Data
- Pune-based sunrise/sunset times (18.5°N, 73.8°E)
- Shaka Samvat 1947 → 1948 transition on Gudi Padwa (March 19, 2026)
- Key festivals: Gudi Padwa (Mar 19), Ganesh Chaturthi (Sep 14), Diwali (Nov 8)
