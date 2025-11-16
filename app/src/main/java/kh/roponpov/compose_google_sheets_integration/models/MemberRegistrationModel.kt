package kh.roponpov.compose_google_sheets_integration.models

data class MemberRegistrationModel(
    val id: Int,
    val latinName: String,
    val khmerName: String,
    val gender: String,
    val email: String,
    val phone: String,
    val paymentStatus: String,
    val address: String,
    val dob: String,
    val registrationDate: String,
    val degree: String,
    val joinGroup: Boolean,
    val remark: String
)

