package kh.roponpov.compose_google_sheets_integration.view.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBarSection(totalMember: Int) {
    Row (
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ){
        Column {
            Text("Members", style = MaterialTheme.typography.titleLarge)
            Text(
                "$totalMember records",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedProfileRing(
            imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNRRPTo_P2i0IKquqdmkZ-3KJbRw2GKHUn2w&s",
            size = 40.dp,
            ringWidth = 1.dp
        )
    }
}