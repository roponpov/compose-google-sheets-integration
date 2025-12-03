package kh.roponpov.compose_google_sheets_integration.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kh.roponpov.compose_google_sheets_integration.R

enum class MemberFilter(val label: String,val nameRes: Int) {
    All(
        label = "All",
        nameRes = R.string.all,
    ),
    Paid(
        label = "Paid",
        nameRes = R.string.paid,
    ),
    Unpaid(
        label = "Unpaid",
        nameRes = R.string.unpaid,
    ),
    Joined(
        label = "Joined",
        nameRes = R.string.joined,
    ),
    NotJoined(
        label = "Not Joined",
        nameRes = R.string.not_joined,
    )
}

@Composable
fun MemberFilter.getDisplayName(): String {
    return stringResource(id = nameRes)
}