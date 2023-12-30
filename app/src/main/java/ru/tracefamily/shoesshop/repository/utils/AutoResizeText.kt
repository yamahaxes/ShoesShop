package ru.tracefamily.shoesshop.repository.utils

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoSizeableText(
    text: String,
    maxTextSize: TextUnit,
    minTextSize: TextUnit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {

    var textSize by remember(text) { mutableStateOf(maxTextSize) }

    Text(
        text = text,
        fontSize = textSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        onTextLayout = {
            if (it.hasVisualOverflow && textSize > minTextSize) {
                textSize = (textSize.value - 1.0F).sp
            }
        },
        style = style
    )
}