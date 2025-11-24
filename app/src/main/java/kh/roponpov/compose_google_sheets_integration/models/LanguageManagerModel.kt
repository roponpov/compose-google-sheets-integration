package kh.roponpov.compose_google_sheets_integration.models

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

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
        prefs(context).edit()
            .putString(KEY_LANGUAGE_CODE, language.code)
            .apply()
    }

    /**
     * Apply locale to current Activity and recreate it so all UI uses new language.
     */
    fun updateAppLocale(activity: Activity, language: AppLanguage) {
        saveLanguage(activity, language)

        val locale = language.locale
        Locale.setDefault(locale)

        val res = activity.resources
        val config = res.configuration
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)

        activity.recreate()
    }

    /**
     * Wrap context with the selected locale.
     * Used in MainActivity.attachBaseContext().
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
