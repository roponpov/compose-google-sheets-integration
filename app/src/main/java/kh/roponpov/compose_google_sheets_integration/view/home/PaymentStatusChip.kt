package kh.roponpov.compose_google_sheets_integration.view.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PaymentStatusChip(status: String) {
    val isPaid = status.equals("Paid", ignoreCase = true)
    val bg = if (isPaid) Color(0xFFDCFCE7) else Color(0xFFFFE4E6)
    val textColor = if (isPaid) Color(0xFF166534) else Color(0xFFB91C1C)

    Surface(
        color = bg,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun JoinGroupChip(joined: Boolean) {
    val label = if (joined) "Joined group" else "Not joined"
    val bg = if (joined) Color(0xFFE0F2FE) else Color(0xFFF3F4F6)
    val textColor = if (joined) Color(0xFF075985) else Color(0xFF4B5563)

    Surface(
        color = bg,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
