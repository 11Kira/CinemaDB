package com.kira.android.cinemadb.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DynamicStarRating(
    rating: Double,
    starColor: Color = Color(0xFFFFD700)
) {
    Row {
        for (i in 1..5) {
            val fullStarThreshold = i * 2.0
            val halfStarThreshold = fullStarThreshold - 1.0

            val icon = when {
                rating >= fullStarThreshold -> Icons.Filled.Star
                rating >= halfStarThreshold -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = starColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}