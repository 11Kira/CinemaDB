package com.kira.android.cinemadb.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kira.android.cinemadb.R
import kotlin.math.roundToInt

@Composable
fun ReviewSection(
    rating: Double,
    reviewCount: Int,
    onReviewClick: () -> Unit
) {
    Surface(
        onClick = onReviewClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.DarkGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "User Reviews",
                    fontSize = 17.sp,
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    DynamicStarRating(rating = rating)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${rating.times(10.0).roundToInt() / 10.0}/10",
                        fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                        color = Color.White
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$reviewCount reviews",
                    fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}