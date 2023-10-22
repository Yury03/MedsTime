package com.example.medstime.ui.meds_tracking.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.models.PackageItemModel
import com.example.medstime.R
import java.text.SimpleDateFormat
import java.util.Date

/**Item не особо юзерфрендли, нуждается в доработке или удалении*/
@Composable
fun PackageItemSecond(packageModel: PackageItemModel) {
    Card(
        modifier = Modifier.padding(4.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.main_black_and_white))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
        ) {
            RowWithIcon(
                drawableId = R.drawable.icon_calendar,
                text = "${packageModel.intakesCount} " +
                        stringResource(id = R.string.intakes).padStart(0),
            )
            RowWithIcon(
                drawableId = R.drawable.icon_box_plus,
                text = dateToString(packageModel.endDate),
            )
            RowWithIcon(
                drawableId = R.drawable.icon_expiration_date,
                text = dateToString(packageModel.expirationDate),
            )

        }
    }
}

@Composable
private fun RowWithIcon(drawableId: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "",
        )
        Text(text = text, modifier = Modifier.padding(4.dp))
    }
}

@SuppressLint("SimpleDateFormat")
private fun dateToString(time: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy")
    val date = Date(time)
    return formatter.format(date)
}

@Preview(showBackground = true)
@Composable
private fun PreviewSecondPackageItem() {
    PackageItemSecond(
        packageModel = PackageItemModel(
            id = "101",
            idMedsTrackModel = "102",
            intakesCount = 90,
            endDate = 1689333793000,
            expirationDate = 1694690593000,
        )
    )
}