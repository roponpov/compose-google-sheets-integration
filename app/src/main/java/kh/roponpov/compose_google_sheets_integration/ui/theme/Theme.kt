package kh.roponpov.compose_google_sheets_integration.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kh.roponpov.compose_google_sheets_integration.models.AppThemeMode

@Composable
fun AppTheme(
    themeMode: AppThemeMode,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Detect system dark mode
    val systemDark = isSystemInDarkTheme()

    // Resolve app mode
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> systemDark
    }

    val context = LocalContext.current

    // Pick color scheme
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (isDark) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            }

            isDark -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
