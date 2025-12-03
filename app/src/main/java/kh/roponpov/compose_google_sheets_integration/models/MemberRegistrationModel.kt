package kh.roponpov.compose_google_sheets_integration.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kh.roponpov.compose_google_sheets_integration.R

enum class PaymentStatus(val text: String,val resName: Int) {
    PAID(
        text = "Paid",
        resName = R.string.paid,
    ),
    UNPAID(
        text = "Unpaid",
        resName = R.string.unpaid
    )
}
@Composable
fun PaymentStatus.getDisplayName(): String {
    return stringResource(id = resName)
}

enum class GenderType(val text: String,val resName: Int) {
    MALE(
        text = "Male",
        resName = R.string.male,
    ),
    FEMALE(
        text = "Female",
        resName = R.string.female,
    ),
    OTHER(
        text = "Other",
        resName = R.string.other
    )
}
@Composable
fun GenderType.getDisplayName(): String {
    return stringResource(id = resName)
}

enum class DegreeType(val text: String,val resName: Int) {
    BACHELOR(
        text = "Bachelor",
        resName = R.string.bachelor,
    ),
    HIGH_SCHOOL(
        text = "High School",
        resName = R.string.high_school
    ),
    MASTER(
        text = "Master",
        resName = R.string.master
    ),
    ASSOCIATE(
        text = "Associate",
        resName = R.string.associate
    ),
    UNKNOWN(
        text = "Unknown",
        resName = R.string.unknown
    )
}
@Composable
fun DegreeType.getDisplayName(): String {
    return stringResource(id = resName)
}

data class MemberRegistrationModel(
    val indexRange: Int? = null,
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

