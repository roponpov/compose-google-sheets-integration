package kh.roponpov.compose_google_sheets_integration.core.prefs

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import kh.roponpov.compose_google_sheets_integration.core.language.AppLanguage
import kh.roponpov.compose_google_sheets_integration.models.GoogleUserProfileModel
import java.util.Locale

object AppPreferences {

    private const val PREF_NAME = "app_prefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
    private const val KEY_LANGUAGE_CODE = "language_code"
    private const val KEY_USER_PROFILE = "user_profile"

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

//    fun saveAccessToken(context: Context, token: String) {
//        prefs(context).edit {
//            putString(KEY_ACCESS_TOKEN, token)
//        }
//    }
//
//    fun getAccessToken(context: Context): String? =
//        prefs(context).getString(KEY_ACCESS_TOKEN, null)

    fun getSavedLanguage(context: Context): AppLanguage {
        val code = prefs(context).getString(KEY_LANGUAGE_CODE, null)
        return AppLanguage.Companion.fromCode(code)
    }

    fun saveLanguage(context: Context, language: AppLanguage) {
        prefs(context).edit {
            putString(KEY_LANGUAGE_CODE, language.code)
        }
    }

    fun updateAppLocale(activity: Activity, language: AppLanguage) {
        this.saveLanguage(activity, language)

        val locale = language.locale
        Locale.setDefault(locale)

        val res = activity.resources
        val config = res.configuration
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)

        activity.recreate()
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

    fun clearUserProfile(context: Context) {
        prefs(context).edit {
            remove(KEY_USER_PROFILE)
        }
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