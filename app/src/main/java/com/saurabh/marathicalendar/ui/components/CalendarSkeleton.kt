package com.saurabh.marathicalendar.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.saurabh.marathicalendar.ui.theme.SaffronPrimary

/**
 * Animated skeleton placeholder that mimics the CalendarGrid layout.
 * Shown while calendar data is loading from assets.
 */
@Composable
fun CalendarSkeleton(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.10f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    Column(modifier = modifier) {
        repeat(6) { row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                repeat(7) { col ->
                    val isHighlighted = row == 1 && col == 3   // simulate "today" cell
                    val isSunday = col == 0

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isHighlighted) {
                            // Saffron circle — looks like the today indicator
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(SaffronPrimary.copy(alpha = shimmerAlpha * 2.5f))
                            )
                        } else {
                            // Regular date cell shimmer
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .fillMaxHeight(0.55f)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        if (isSunday)
                                            SaffronPrimary.copy(alpha = shimmerAlpha * 1.4f)
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = shimmerAlpha)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}
