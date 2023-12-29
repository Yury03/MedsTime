package com.example.medstime.ui.add_track.components

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medstime.R
import com.example.medstime.ui.add_track.AddMedTrackEvent
import com.example.medstime.ui.add_track.AddMedTrackState
import com.example.medstime.ui.common_components.AddMedButton
import com.example.medstime.ui.common_components.PackageList
import com.example.medstime.ui.utils.toDisplayString
import java.util.Date

@ExperimentalMaterial3Api
@Composable
fun AddMedTrack(
    uiState: AddMedTrackState,
    onBackButtonClick: (String) -> Unit = {},
    sendEvent: (AddMedTrackEvent) -> Unit = {},
) {
    var textDosageUnit by remember(uiState.dosageUnit) { mutableStateOf(uiState.dosageUnit) }
    val expirationDate by remember(uiState.expirationDate) { mutableLongStateOf(uiState.expirationDate) }
    var textQuantityInPackage by remember(uiState.quantityInPackage) { mutableStateOf(uiState.quantityInPackage) }
    var textMedName by remember(uiState.medName) { mutableStateOf(uiState.medName) }
    val packageList by remember(uiState.actualPackageList) { mutableStateOf(uiState.actualPackageList) }
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    sendEvent(AddMedTrackEvent.BackButtonClicked)
                    onBackButtonClick(uiState.addMedStateJson)
                }) {
                Icon(
                    modifier = Modifier
                        .padding(12.dp),
                    painter = painterResource(id = R.drawable.button_icon_arrow_to_left),
                    contentDescription = null,
                )
            }
            Text(
                text = stringResource(id = R.string.add_package),
                fontSize = dimensionResource(id = R.dimen.text_4_level).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
            )
        }
        InputTextField(
            modifier = Modifier.fillMaxWidth(),
            textValue = textMedName,
            hint = stringResource(id = R.string.med_name_hint),
            onValueChange = { textMedName = it }
        )
        BottomInputFields(
            textDosageUnits = textDosageUnit,
            expirationDate = expirationDate,
            textQuantityInPackage = textQuantityInPackage,
            onValuesChangedDosageUnits = { newTextDosageUnit ->
                Log.d("Tag", "dosageUnit: old - $textDosageUnit | new - $newTextDosageUnit")
                if (newTextDosageUnit.isNotEmpty()) textDosageUnit = newTextDosageUnit
                sendEvent(AddMedTrackEvent.UpdateState(uiState.copy(dosageUnit = textDosageUnit)))
            },
            onValueChangedQuantityInPackage = { newTextQuantityInPackage ->
                textQuantityInPackage = newTextQuantityInPackage
                sendEvent(AddMedTrackEvent.UpdateState(uiState.copy(quantityInPackage = newTextQuantityInPackage)))
            },
            onExpirationDateChanged = { newExpirationDate ->
                sendEvent(AddMedTrackEvent.UpdateState(uiState.copy(expirationDate = newExpirationDate)))
            },
        )
        AddMedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            onClick = {
                sendEvent(AddMedTrackEvent.AddNewPackageButtonClicked)
            },
            iconId = R.drawable.button_icon_arrow_to_right,
            stringId = R.string.add_new_package,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            fontSize = dimensionResource(id = R.dimen.text_5_level).value.sp,
            text = stringResource(id = R.string.package_list),
        )
        PackageList(
            verticalPaddingBox = 8.dp,
            height = 110.dp,
            packageItems = packageList,
            showAddItem = false,
            showBackground = false,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomInputFields(
    textDosageUnits: String,
    expirationDate: Long,
    textQuantityInPackage: String,
    onValuesChangedDosageUnits: (String) -> Unit,
    onValueChangedQuantityInPackage: (String) -> Unit,
    onExpirationDateChanged: (Long) -> Unit,
) {
    val dosageArray = stringArrayResource(id = R.array.dosage_array)
    val openDatePicker = remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    InputTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusable(enabled = false)
            .onFocusChanged {
                if (it.isFocused && !openDatePicker.value) {
                    openDatePicker.value = true
                    focusManager.clearFocus()
                }
            },
        textValue = if (expirationDate == 0L) "" else Date(expirationDate).toDisplayString(),
        hint = stringResource(id = R.string.expiration_date_hint),
        readOnly = true,
    )
    if (openDatePicker.value) {
        ExpirationDatePickerDialog(
            openDatePicker = openDatePicker,
            onDateChanged = onExpirationDateChanged
        )
    }
    Row {
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(0.4f),
            expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(),
                readOnly = true,
                value = textDosageUnits,
                onValueChange = {/*TODO send state in vm*/ },
                label = { Text(text = stringResource(id = R.string.units_hint)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { /*TODO*/ }) {
                dosageArray.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onValuesChangedDosageUnits(it)
                            isExpanded = !isExpanded
                        }
                    )
                }
            }
        }
        InputTextField(
            modifier = Modifier
                .weight(0.6f)
                .padding(start = 4.dp),
            textValue = textQuantityInPackage,
            //todo label = { Text(text = stringResource(id = R.string.hint)) },
            hint = stringResource(id = R.string.number_of_meds_per_package),
            isNumber = true,
            onValueChange = onValueChangedQuantityInPackage,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpirationDatePickerDialog(
    openDatePicker: MutableState<Boolean>,
    onDateChanged: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = {
            openDatePicker.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateChanged(it)
                    }
                    openDatePicker.value = false
                },
            ) {
                Text(stringResource(id = R.string.date_picker_confirm_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDatePicker.value = false
                }
            ) {
                Text(stringResource(id = R.string.date_picker_dismiss_button))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(backgroundColor = 0xFFDBDBDB, showSystemUi = true, showBackground = true)
@Composable
fun PreviewAddMedTrack() {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
    ) {
        AddMedTrack(AddMedTrackState())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(backgroundColor = 0xFFDBDBDB, showSystemUi = true, showBackground = true)
@Composable
fun PreviewAddMedTrackEditMode() {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
    ) {
        AddMedTrack(AddMedTrackState("test"))
    }
}

