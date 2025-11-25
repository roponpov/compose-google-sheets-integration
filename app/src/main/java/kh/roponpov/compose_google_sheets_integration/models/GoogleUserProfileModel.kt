package kh.roponpov.compose_google_sheets_integration.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleUserProfileModel(
    val name: String,
    val email: String,
    val photoUrl: String?,
    val givenName: String?,
    val familyName: String?
)

