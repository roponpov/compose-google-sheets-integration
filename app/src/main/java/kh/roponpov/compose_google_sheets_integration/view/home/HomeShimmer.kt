package kh.roponpov.compose_google_sheets_integration.view.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

@Composable
fun MemberListShimmer() {
    val listState = rememberLazyListState()
    val overscroll = rememberOverscrollEffect()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        overscrollEffect = overscroll,            // <- important

    ) {
        items(6) {
            MemberCardShimmer()
        }
    }
}

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.8f),
            Color.LightGray.copy(alpha = 0.3f),
        ),
        start = Offset(shimmerTranslate, 0f),
        end = Offset(shimmerTranslate + 300f, 0f)
    )
}

@Composable
fun MemberCardShimmer() {
    val shimmerBrush = rememberShimmerBrush()

    androidx.compose.material3.Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // mimic your real MemberCard structure
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Name
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ){
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(shimmerBrush, CircleShape)
                )

                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(16.dp)
                            .background(shimmerBrush, MaterialTheme.shapes.small)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Khmer name
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .height(12.dp)
                            .background(shimmerBrush, MaterialTheme.shapes.small)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .height(12.dp)
                            .background(shimmerBrush, MaterialTheme.shapes.small)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // gender + phone row
            Row(
                horizontalArrangement = Arrangement.spacedBy(alignment = Alignment.Start, space = 5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(16.dp)
                        .background(shimmerBrush, MaterialTheme.shapes.small)
                )

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(16.dp)
                        .background(shimmerBrush, MaterialTheme.shapes.small)
                )
            }
        }
    }
}

