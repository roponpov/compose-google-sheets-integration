package kh.roponpov.compose_google_sheets_integration.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchSection(
    value: String,
    onTrailingTap: () -> Unit,
    onValueChange: (String) -> Unit
) {
    val placeholderStyle = MaterialTheme.typography.bodyLarge.copy(
        color = Color.Gray,
        fontSize = 13.sp
    )

    val textStyle = LocalTextStyle.current.copy(
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onSurface
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = textStyle,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .height(45.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // prefix icon
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(20.dp)
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Search by name, email, phone...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = placeholderStyle
                        )
                    }
                    innerTextField()
                }

                // trailing icon
                if(value.isNotEmpty()) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.size(20.dp).clickable {
                            onTrailingTap()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Icon",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            modifier = Modifier
                                .padding(3.dp)
                                .size(20.dp)
                        )
                    }
                }
            }
        }
    )
}
