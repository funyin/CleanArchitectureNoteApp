package com.plcoding.cleanarchitecturenoteapp.features.notes.presentation.add_edit_notes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun TransparentHintTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean = text.isEmpty(),
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onFocusChanged: (FocusState) -> Unit,
    onValueChanged: (String) -> Unit,
) {
    Box(modifier = modifier) {
        BasicTextField(value = text, onValueChange = onValueChanged, singleLine = singleLine, textStyle = textStyle,
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged(onFocusChanged))
        if (isHintVisible)
            Text(text = hint, style = textStyle, color = Color.DarkGray)
    }
}