package kh.roponpov.compose_google_sheets_integration.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val primary = Color(0xFF122133)
val primaryDark = Color(0xFF0D1826)
val primaryLight = Color(0xFF7FBBFF)

val secondary = Color(0xFFFFB300)
val secondaryDark = Color(0xFFC68400)
val secondaryLight = Color(0xFFFFD65C)
val error = Color(0xFFB00020)

val Neutral0 = Color(0xFFFFFFFF)
val Neutral10 = Color(0xFFF5F7FA)
val Neutral20 = Color(0xFFE6E9EE)
val Neutral30 = Color(0xFFCCD1D9)
val Neutral40 = Color(0xFF9EA5B1)
val Neutral80 = Color(0xFF2C313A)
val Neutral90 = Color(0xFF1A1D22)
val Black = Color(0xFF000000)

val LightColorScheme = lightColorScheme(
    primary = primary,
    onPrimary = Color.White,
    primaryContainer = primaryLight,
    onPrimaryContainer = Color(0xFFDCE4EF),

    secondary = secondary,
    onSecondary = Color.Black,
    secondaryContainer = secondaryLight,
    onSecondaryContainer = Color(0xFF2B1F00),

    tertiary = Color(0xFF4E3BC9),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE0DFFF),
    onTertiaryContainer = Color(0xFF080065),

    background = Neutral0,
    onBackground = Neutral90,

    surface = Neutral0,
    onSurface = Neutral90,
    surfaceVariant = Neutral10,
    onSurfaceVariant = Neutral80,

    error = error,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410002),

    outline = Neutral30,
    outlineVariant = Neutral20,

    scrim = Black,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral10,
    inversePrimary = primaryLight,
)


val DarkColorScheme = darkColorScheme(
    primary = primaryLight,         // Lighter tone on dark bg
    onPrimary = Color.Black,
    primaryContainer = primaryDark,
    onPrimaryContainer = Color(0xFFD7E2FF),

    secondary = secondaryLight,
    onSecondary = Color.Black,
    secondaryContainer = secondaryDark,
    onSecondaryContainer = Color(0xFFFFF0C2),

    tertiary = Color(0xFFC3BFFF),
    onTertiary = Color(0xFF1C1163),
    tertiaryContainer = Color(0xFF34257F),
    onTertiaryContainer = Color(0xFFE2DEFF),

    background = Neutral90,
    onBackground = Neutral10,

    surface = Neutral90,
    onSurface = Neutral10,
    surfaceVariant = Neutral80,
    onSurfaceVariant = Neutral20,

    error = error,
    onError = Color.White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD4),

    outline = Neutral40,
    outlineVariant = Neutral80,

    scrim = Black,
    inverseSurface = Neutral10,
    inverseOnSurface = Neutral90,
    inversePrimary = primary,
)
