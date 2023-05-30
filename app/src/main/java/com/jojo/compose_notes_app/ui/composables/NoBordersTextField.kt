package com.jojo.compose_notes_app.ui.composables

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun NoBordersTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    singleLine: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        textStyle = textStyle,
        placeholder = placeholder,
        modifier = Modifier.then(modifier)
    )
}