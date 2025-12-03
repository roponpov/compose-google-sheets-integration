package kh.roponpov.compose_google_sheets_integration.view.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kh.roponpov.compose_google_sheets_integration.R
import kh.roponpov.compose_google_sheets_integration.core.language.AppLanguage
import kh.roponpov.compose_google_sheets_integration.models.MemberFilter
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel
import kh.roponpov.compose_google_sheets_integration.models.PaymentStatus
import kh.roponpov.compose_google_sheets_integration.models.getDisplayName

@Composable
fun FilterRowSection(
    selectedFilter: MemberFilter,
    onFilterChange: (MemberFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 8.dp, top = 5.dp, bottom = 5.dp)
    ) {
        MemberFilter.entries.forEach { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterChange(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    // UNSELECTED
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,

                    // SELECTED
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = filter == selectedFilter,
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    borderWidth = 1.dp,
                ),
                label = { Text(filter.getDisplayName()) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

fun MemberFilter.matches(member: MemberRegistrationModel): Boolean = when (this) {
    MemberFilter.All -> true
    MemberFilter.Paid -> member.paymentStatus == PaymentStatus.PAID
    MemberFilter.Unpaid -> member.paymentStatus == PaymentStatus.UNPAID
    MemberFilter.Joined -> member.joinGroup
    MemberFilter.NotJoined -> !member.joinGroup

}

fun MemberRegistrationModel.matchesSearch(query: String): Boolean {
    if (query.isBlank()) return true
    val q = query.lowercase()
    return latinName.lowercase().contains(q) ||
            khmerName.lowercase().contains(q) ||
            email.lowercase().contains(q) ||
            phone.lowercase().contains(q) ||
            address.lowercase().contains(q)
}
