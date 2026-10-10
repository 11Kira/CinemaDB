package com.kira.android.cinemadb.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kira.android.cinemadb.R

@Composable
fun HeaderText(text: String) {
    Text(
        textAlign = TextAlign.Start,
        fontSize = 17.sp,
        fontFamily = Font(R.font.roboto_medium).toFontFamily(),
        modifier = Modifier
            .wrapContentWidth()
            .padding(top = 20.dp),
        text = text,
        color = Color.White
    )
}