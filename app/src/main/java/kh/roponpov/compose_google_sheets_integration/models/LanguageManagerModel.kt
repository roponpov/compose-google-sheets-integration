package kh.roponpov.compose_google_sheets_integration.models

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.Locale
import androidx.core.content.edit

enum class AppLanguage(
    val code: String,
    val locale: Locale,
    val displayName: String
) {
    KHMER("km", Locale("km"), "KHMER"),
    ENGLISH("en", Locale.ENGLISH, "ENGLISH"),
    CHINESE("zh", Locale.SIMPLIFIED_CHINESE, "CHINESE"),
    LAO("lo", Locale("lo"), "LAO");

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}

object LanguageManager {

    private const val PREF_NAME = "app_language_prefs"
    private const val KEY_LANGUAGE_CODE = "language_code"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getSavedLanguage(context: Context): AppLanguage {
        val code = prefs(context).getString(KEY_LANGUAGE_CODE, null)
        return AppLanguage.fromCode(code)
    }

    fun saveLanguage(context: Context, language: AppLanguage) {
        prefs(context).edit {
            putString(KEY_LANGUAGE_CODE, language.code)
        }
    }

    fun updateAppLocale(activity: Activity, language: AppLanguage) {
        saveLanguage(activity, language)

        val locale = language.locale
        Locale.setDefault(locale)

        val res = activity.resources
        val config = res.configuration
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)

        // recreate so all Composables use new locale
        activity.recreate()
    }

    /**
     * Used in MainActivity.attachBaseContext to apply saved language early.
     */
    fun wrapContext(base: Context): Context {
        val language = getSavedLanguage(base)
        val locale = language.locale
        Locale.setDefault(locale)

        val config = base.resources.configuration
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }
}
