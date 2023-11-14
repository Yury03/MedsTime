package com.example.medstime.ui.add_track.components


import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    textValue: String,
    hint: String,
    isNumber: Boolean = false,
    onValueChange: (String) -> Unit,
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
        keyboardOptions = keyboardOptions
    )

}

//@Preview
//@Composable
//private fun InputTextFieldPreview(){
//    InputTextField()
//}