package com.saurabh.marathicalendar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronLight,
    onPrimaryContainer = SaffronDark,
    secondary = MaroonAccent,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFCDD2),
    onSecondaryContainer = Color(0xFF7B0000),
    tertiary = VratGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFC8E6C9),
    onTertiaryContainer = Color(0xFF1B5E20),
    error = HolidayRed,
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    background = SurfaceWhite,
    onBackground = OnSurface,
    surface = SurfaceWhite,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline = DividerColor,
    outlineVariant = Color(0xFFEFEBE9),
    surfaceVariant = SaffronSurface,
    inverseSurface = Color(0xFF3E2723),
    inverseOnSurface = Color(0xFFFBE9E7),
    inversePrimary = SaffronLight
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB74D),
    onPrimary = Color(0xFF3E1F00),
    primaryContainer = Color(0xFFE65100),
    onPrimaryContainer = Color(0xFFFFE0B2),
    secondary = Color(0xFFEF9A9A),
    onSecondary = Color(0xFF4A0000),
    secondaryContainer = Color(0xFF7B0000),
    onSecondaryContainer = Color(0xFFFFCDD2),
    tertiary = Color(0xFF81C784),
    onTertiary = Color(0xFF1B3A1C),
    tertiaryContainer = Color(0xFF1B5E20),
    onTertiaryContainer = Color(0xFFC8E6C9),
    error = Color(0xFFFF8A80),
    onError = Color(0xFF3B0000),
    errorContainer = Color(0xFF7B0000),
    onErrorContainer = Color(0xFFFFCDD2),
    background = Color(0xFF1C1208),
    surface = Color(0xFF1C1208),
    onBackground = Color(0xFFEDE0D4),
    onSurface = Color(0xFFEDE0D4),
    onSurfaceVariant = Color(0xFFCDBBA7),
    outline = Color(0xFF5D4037),
    outlineVariant = Color(0xFF3E2723),
    surfaceVariant = Color(0xFF2D1F12),
    inverseSurface = Color(0xFFEDE0D4),
    inverseOnSurface = Color(0xFF1C1208),
    inversePrimary = SaffronDark
)

@Composable
fun MarathiCalendarTheme(
    isDarkMode: Boolean = false,
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme
    val currentDensity = LocalDensity.current

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = currentDensity.density,
            fontScale = currentDensity.fontScale * fontScale
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MarathiTypography,
            shapes = CalendarShapes,
            content = content
        )
    }
}
