package com.example.medstime.ui.add_track.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.MedicationModel
import com.example.domain.models.MedsTrackModel
import com.example.medstime.R
import com.example.medstime.ui.common_components.AddMedButton
import com.example.medstime.ui.meds_tracking.components.PackageList
import com.example.medstime.ui.meds_tracking.components.getListMedicationModel
import com.example.medstime.ui.meds_tracking.components.getListTrackingModel

@Composable
fun AddMedTrack(
    expirationDate: String = "",
    medicationModel: MedicationModel,
    medsTrackModel: MedsTrackModel,
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
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
                        .clickable { }
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
                textValue = medicationModel.name,
                hint = stringResource(id = R.string.med_name_hint),
            )
            PackageList(
                verticalPaddingBox = 8.dp,
                height = 110.dp,
                trackModel = medsTrackModel,
                showAddItem = false,
                showBackground = false,
            )
            InputTextField(
                textValue = expirationDate,//todo
                hint = stringResource(id = R.string.expiration_date_hint),
            )
            InputTextField(
                textValue = "",
                hint = medicationModel.dosageUnit.padEnd(medicationModel.dosageUnit.length + 1) +
                        stringResource(id = R.string.number_of_meds_per_package),
                isNumber = true,
            )

            AddMedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                onClick = {

                },
                iconId = R.drawable.button_icon_arrow_to_right,
                stringId = R.string.add,
            )
        }
    }
}

@Preview(backgroundColor = 0xFFDBDBDB, showSystemUi = true, showBackground = true)
@Composable
fun PreviewAddMedTrack() {
    Box(modifier = Modifier.fillMaxSize()) {
        AddMedTrack(
            medsTrackModel = getListTrackingModel()[0],
            medicationModel = getListMedicationModel()[0]
        )
    }
}