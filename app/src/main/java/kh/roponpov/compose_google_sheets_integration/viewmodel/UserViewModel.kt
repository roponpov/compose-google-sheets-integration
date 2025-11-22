package kh.roponpov.compose_google_sheets_integration.viewmodel

import androidx.lifecycle.ViewModel
import kh.roponpov.compose_google_sheets_integration.models.GoogleUserProfileModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<GoogleUserProfileModel?>(null)
    val user = _user.asStateFlow()

    fun setFromGoogleAccount(account: GoogleSignInAccount) {
        _user.value = GoogleUserProfileModel(
            name = account.displayName.orElse(),
            email = account.email.orElse(),
            photoUrl = account.photoUrl?.toString(),
            givenName = account.givenName,
            familyName = account.familyName
        )
    }

    fun clear() {
        _user.value = null
    }

    private fun String?.orElse(default: String = "") = this ?: default
}
