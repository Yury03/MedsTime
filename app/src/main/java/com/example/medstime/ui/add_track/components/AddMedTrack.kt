package com.example.medstime.ui.add_track.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.compose.rememberNavController
import com.example.domain.models.MedsTrackModel
import com.example.medstime.R
import com.example.medstime.ui.common_components.AddMedButton
import com.example.medstime.ui.common_components.PackageList
import com.example.medstime.ui.compose_stubs.getListTrackingModel

/**## Функция AddMedTrack реализует весь ui экрана добавления/редактирования упаковок
 * ## Параметры:
 * - medName - необязательный параметр, передается если **medicationModel.name** уже существует;
 * - dosageUnit - обязательный параметр, берется из **AddMedFragment SpinDosageUnits**;
 * - medsTrackModel - необязательный параметр, равен **null**, если экран открыт в режиме добавления.*/
@ExperimentalMaterial3Api
@Composable
fun AddMedTrack(
    medName: String = "",
    dosageUnit: String,
    medsTrackModel: MedsTrackModel? = null,
) {
    val navController = rememberNavController()
    var textDosageUnits by remember { mutableStateOf(dosageUnit) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .clickable {
                        navController.navigate(R.id.addMedFragment)
                        /*TODO возврат на предыдущую страницу с сохранением всех данных*/
                    }
                    .padding(12.dp),
                painter = painterResource(id = R.drawable.button_icon_arrow_to_left),
                contentDescription = null,
            )
            Text(
                text = stringResource(id = R.string.add_package),
                fontSize = dimensionResource(id = R.dimen.text_4_level).value.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
            )
        }

        InputTextField(
            modifier = Modifier.fillMaxWidth(),
            textValue = medName,
            hint = stringResource(id = R.string.med_name_hint),
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            fontSize = dimensionResource(id = R.dimen.text_5_level).value.sp,
            text = stringResource(id = R.string.package_list),
        )

        PackageList(
            verticalPaddingBox = 8.dp,
            height = 110.dp,
            packageItems = medsTrackModel?.packageItems ?: emptyList(),
            showAddItem = false,
            showBackground = false,
        )

        BottomInputFields(
            expanded,
            textDosageUnits
        ) { newExpanded, newTextDosageUnits ->
            expanded = newExpanded
            textDosageUnits = newTextDosageUnits
        }
        AddMedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            onClick = {
                //todo сбор данных и формирование package item где intakes неопределены
            },
            iconId = R.drawable.button_icon_arrow_to_right,
            stringId = R.string.add,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomInputFields(
    expanded: Boolean,
    textDosageUnits: String,
    onValuesChanged: (Boolean, String) -> Unit,
) {
    val dosageArray = stringArrayResource(id = R.array.dosage_array)
    InputTextField(
        modifier = Modifier.fillMaxWidth(),
        textValue = "",//todo DatePicker
        hint = stringResource(id = R.string.expiration_date_hint),
    )
    Row {
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(0.4f),
            expanded = expanded,
            onExpandedChange = {
                onValuesChanged(
                    !expanded,
                    textDosageUnits
                )
            },//инверсия expanded, textDosageUnits не меняется
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(),
                readOnly = true,
                value = textDosageUnits,
                onValueChange = {/*TODO send state in vm*/ },
                label = { Text(text = stringResource(id = R.string.units_hint)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { /*TODO*/ }) {
                dosageArray.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onValuesChanged(false, it)
                        }
                    )
                }
            }
        }
        InputTextField(
            modifier = Modifier
                .weight(0.6f)
                .padding(start = 4.dp),
            textValue = "",
            //todo label = { Text(text = stringResource(id = R.string.hint)) },
            hint = stringResource(id = R.string.number_of_meds_per_package),
            isNumber = true,
        )
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
        AddMedTrack(
            medsTrackModel = getListTrackingModel()[1],
            dosageUnit = "Таблетки"
        )
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
        AddMedTrack(
            medsTrackModel = getListTrackingModel()[0],
            dosageUnit = "Мг",
        )
    }
}