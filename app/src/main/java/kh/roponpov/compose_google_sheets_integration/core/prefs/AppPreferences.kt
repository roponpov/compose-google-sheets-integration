package kh.roponpov.compose_google_sheets_integration.core.prefs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import kh.roponpov.compose_google_sheets_integration.core.language.AppLanguage
import kh.roponpov.compose_google_sheets_integration.models.AppThemeMode
import kh.roponpov.compose_google_sheets_integration.models.GoogleUserProfileModel
import java.util.Locale

object AppPreferences {

    private const val PREF_NAME = "app_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
    private const val KEY_LANGUAGE_CODE = "language_code"
    private const val KEY_USER_PROFILE = "user_profile"
    private const val KEY_THEME_MODE = "theme_mode"

    private val moshi by lazy { Moshi.Builder().build() }
    private val profileAdapter by lazy {
        moshi.adapter(GoogleUserProfileModel::class.java)
    }

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun isFirstLaunch(context: Context): Boolean =
        prefs(context).getBoolean(KEY_IS_FIRST_LAUNCH, true)

    fun setFirstLaunch(context: Context, value: Boolean) {
        prefs(context).edit {
            putBoolean(KEY_IS_FIRST_LAUNCH, value)
        }
    }

    fun getSavedLanguage(context: Context): AppLanguage {
        val code = prefs(context).getString(KEY_LANGUAGE_CODE, null)
        return AppLanguage.fromCode(code)
    }

    fun saveLanguage(context: Context, language: AppLanguage) {
        prefs(context).edit {
            putString(KEY_LANGUAGE_CODE, language.code)
        }
    }

    fun applyLocaleToActivity(activity: Activity, language: AppLanguage) {
        // Save language
        saveLanguage(activity, language)

        val locale = language.locale
        Locale.setDefault(locale)

        val res = activity.resources
        val config = res.configuration
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)
    }

    fun restartAppFromRoot(activity: Activity, language: AppLanguage) {
        applyLocaleToActivity(activity, language)
        restartAppFromRoot(activity)
    }

    fun getUserProfile(context: Context): GoogleUserProfileModel? {
        val json = prefs(context).getString(KEY_USER_PROFILE, null) ?: return null
        return profileAdapter.fromJson(json)
    }

    fun saveUserProfile(context: Context, profile: GoogleUserProfileModel) {
        val json = profileAdapter.toJson(profile)
        prefs(context).edit {
            putString(KEY_USER_PROFILE, json)
        }
    }

    fun clearUserProfile(context: Context,activity: Activity) {
        prefs(context).edit {
            remove(KEY_USER_PROFILE)
        }
        restartAppFromRoot(activity)
    }

    private fun restartAppFromRoot(activity: Activity) {
        val intent = activity.packageManager
            .getLaunchIntentForPackage(activity.packageName)
            ?: return

        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        )

        activity.startActivity(intent)
        activity.finish()
    }

    fun saveThemeMode(context: Context, mode: AppThemeMode) {
        prefs(context).edit {
            putString(KEY_THEME_MODE, mode.value)
        }
    }

    fun getThemeMode(context: Context): AppThemeMode {
        val stored = prefs(context).getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.value)
        return AppThemeMode.fromValue(stored)
    }

    fun wrapContext(base: Context): Context {
        val language = this.getSavedLanguage(base)
        val locale = language.locale
        Locale.setDefault(locale)

        val config = base.resources.configuration
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }
}