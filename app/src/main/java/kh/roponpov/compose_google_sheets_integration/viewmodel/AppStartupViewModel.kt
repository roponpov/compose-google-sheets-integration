package kh.roponpov.compose_google_sheets_integration.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kh.roponpov.compose_google_sheets_integration.core.prefs.AppPreferences

class AppStartupViewModel(
    app: Application
) : AndroidViewModel(app) {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow("language")
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1200)

            val appContext = getApplication<Application>()

            val isFirstLaunch = AppPreferences.isFirstLaunch(appContext)

            val token = AppPreferences.getUserProfile(appContext)

            _startDestination.value = when {
                isFirstLaunch -> "language"
                token != null -> "home"
                else -> "login"
            }

            _isLoading.value = false
        }
    }
}
