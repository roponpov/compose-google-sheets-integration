package kh.roponpov.compose_google_sheets_integration.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun FontSizeSheet(
    fontScale: Float,
    onFontScaleChange: (Float) -> Unit,
    onClose: () -> Unit
) {
    val steps = listOf(0.9f, 1.0f, 1.25f)

    fun snapToStep(value: Float): Float {
        return steps.minBy { abs(it - value) }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Font Size",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Choose how big the text looks in the app.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = fontScale,
            onValueChange = { raw ->
                val snapped = snapToStep(raw)
                onFontScaleChange(snapped)
            },
            valueRange = steps.first()..steps.last(),
            steps = steps.size - 2
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Small\n90%",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = if (fontScale == 0.9f)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Normal\n100%",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = if (fontScale == 1.0f)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "Bigger\n125%",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = if (fontScale == 1.25f)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Preview text (កខគ ABC 123)",
            fontSize = (14.sp * fontScale),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(text = "Done")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}