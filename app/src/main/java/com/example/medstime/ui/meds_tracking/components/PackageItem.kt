package com.example.medstime.ui.meds_tracking.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.models.PackageItemModel
import com.example.medstime.R
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PackageItem(packageModel: PackageItemModel) {

    Card {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
        ) {
            RowWithIcon(
                drawableId = R.drawable.icon_calendar,
                text = "90 приемов",
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
        Text(text = text)
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
private fun PreviewPackageItem() {
    PackageItem(
        packageModel = PackageItemModel(
            id = "101",
            idMedsTrackModel = "102",
            intakesCount = 90,
            endDate = 1689333793000,
            expirationDate = 1694690593000,
        )
    )
}