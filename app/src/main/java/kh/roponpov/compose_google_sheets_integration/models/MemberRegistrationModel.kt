package kh.roponpov.compose_google_sheets_integration.models

enum class PaymentStatus(val text: String) {
    PAID("Paid"),
    UNPAID("Unpaid")
}

enum class GenderType(val text: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other")
}

enum class DegreeType(val text: String) {
    BACHELOR("Bachelor"),
    HIGH_SCHOOL("High School"),
    MASTER("Master"),
    ASSOCIATE("Associate"),
}

data class MemberRegistrationModel(
    val id: Int,
    val latinName: String,
    val khmerName: String,
    val gender: GenderType,
    val email: String,
    val phone: String,
    val paymentStatus: PaymentStatus,
    val address: String,
    val dob: String,
    val registrationDate: String,
    val degree: DegreeType,
    val joinGroup: Boolean,
    val remark: String
)

