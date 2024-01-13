package com.example.medstime.ui.add_track.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    textValue: String = "",
    hint: String = "",
    isNumber: Boolean = false,
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false,
) {
    val keyboardOptions = if (isNumber) {
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    } else {
        KeyboardOptions.Default
    }
    OutlinedTextField(
        modifier = modifier,
        value = textValue,
        onValueChange = onValueChange,
        label = { Text(hint) },
        keyboardOptions = keyboardOptions,
        readOnly = readOnly,
    )
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun InputTextFieldPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        InputTextField()
    }
}