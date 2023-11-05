package com.example.medstime.ui.add_track.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InputTextField(textValue: String, hint: String, isNumber: Boolean = false) {
    var text by remember { mutableStateOf(textValue) }
    val keyboardOptions = if (isNumber) {
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    } else {
        KeyboardOptions.Default
    }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue,
        onValueChange = {
            text = it
        },
        label = { Text(hint) },
        keyboardOptions = keyboardOptions
    )
}

//@Preview
//@Composable
//private fun InputTextFieldPreview(){
//    InputTextField()
//}