package kh.roponpov.compose_google_sheets_integration.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kh.roponpov.compose_google_sheets_integration.models.GoogleUserProfileModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kh.roponpov.compose_google_sheets_integration.core.prefs.AppPreferences

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<GoogleUserProfileModel?>(null)
    val user = _user.asStateFlow()

    fun setFromGoogleAccount(
        account: GoogleSignInAccount,
        context: Context
    ) {

        val profile = GoogleUserProfileModel(
            name = account.displayName.orElse(),
            email = account.email.orElse(),
            photoUrl = account.photoUrl?.toString(),
            givenName = account.givenName,
            familyName = account.familyName
        )

        _user.value = profile
        AppPreferences.saveUserProfile(context, profile)
    }

    fun loadSavedUser(context: Context) {
        _user.value = AppPreferences.getUserProfile(context)
    }

    fun clear(context: Context) {
        _user.value = null
        AppPreferences.clearUserProfile(context)
    }

    private fun String?.orElse(default: String = "") = this ?: default
}
