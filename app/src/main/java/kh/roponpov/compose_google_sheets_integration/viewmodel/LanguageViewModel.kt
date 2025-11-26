package kh.roponpov.compose_google_sheets_integration.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import kh.roponpov.compose_google_sheets_integration.core.language.AppLanguage
import kh.roponpov.compose_google_sheets_integration.core.prefs.AppPreferences

class LanguageViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val appContext = getApplication<Application>().applicationContext
    var currentLanguage = mutableStateOf(AppLanguage.KHMER)

    init {
        currentLanguage.value = AppPreferences.getSavedLanguage(appContext)
    }

    fun setLanguage(language: AppLanguage) {
        if (language == currentLanguage.value) return
        currentLanguage.value = language
        AppPreferences.saveLanguage(appContext, language)
    }
}
