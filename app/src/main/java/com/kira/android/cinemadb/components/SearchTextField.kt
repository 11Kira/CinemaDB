package com.kira.android.cinemadb.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kira.android.cinemadb.R

@Composable
fun SearchTextField(
    searchText: String = "",
    onSearchTextChange: (String) -> Unit = {},
    onClearClicked: () -> Unit = {},
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .onFocusChanged { isFocused = it.isFocused },
        singleLine = true,
        cursorBrush = SolidColor(Color.White),
        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .height(38.dp)
                    .background(color = Color.Transparent)
                    .border(
                        width = 1.5.dp,
                        color = if (isFocused) Color.White else Color.DarkGray,
                        shape = RoundedCornerShape(24)
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = if (isFocused) Color.White else Color.Gray,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (searchText.isEmpty()) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text = "Search Movie...",
                            style = TextStyle(
                                color = Color.Gray,
                                fontFamily = Font(R.font.roboto_medium).toFontFamily(),
                                fontSize = 14.sp,
                            ),
                        )
                    }
                    innerTextField()
                }
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onClearClicked()
                        },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    )
}