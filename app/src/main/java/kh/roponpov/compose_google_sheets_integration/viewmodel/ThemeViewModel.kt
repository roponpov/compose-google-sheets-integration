package kh.roponpov.compose_google_sheets_integration.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import kh.roponpov.compose_google_sheets_integration.core.prefs.AppPreferences
import kh.roponpov.compose_google_sheets_integration.models.AppThemeMode

class ThemeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _theme = mutableStateOf(
        AppPreferences.getThemeMode(application)
    )
    val theme: State<AppThemeMode> get() = _theme

    fun setTheme(mode: AppThemeMode) {
        AppPreferences.saveThemeMode(getApplication(), mode)
        _theme.value = mode
    }
}
