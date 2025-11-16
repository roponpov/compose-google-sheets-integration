package kh.roponpov.compose_google_sheets_integration.view.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kh.roponpov.compose_google_sheets_integration.models.MemberRegistrationModel

enum class MemberFilter(val label: String) {
    All("All"),
    Paid("Paid"),
    Unpaid("Unpaid"),
    Joined("Joined"),
    NotJoined("Not Joined")
}

@Composable
fun FilterRowSection(
    selectedFilter: MemberFilter,
    onFilterChange: (MemberFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        MemberFilter.entries.forEach { filter ->
            FilterChip(
                selected = filter == selectedFilter,
                onClick = { onFilterChange(filter) },
                label = { Text(filter.label) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

fun MemberFilter.matches(member: MemberRegistrationModel): Boolean = when (this) {
    MemberFilter.All -> true
    MemberFilter.Paid -> member.paymentStatus.equals("Paid", ignoreCase = true)
    MemberFilter.Unpaid -> member.paymentStatus.equals("Unpaid", ignoreCase = true)
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
