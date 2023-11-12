package com.example.medstime.ui.common_components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.domain.models.PackageItemModel
import com.example.medstime.R
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PackageItem(packageModel: PackageItemModel) {

    Card(
        modifier = Modifier.padding(4.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.main_black_and_white)),
        colors = CardDefaults.cardColors(
            containerColor = Color(
                ContextCompat.getColor(
                    LocalContext.current,
                    R.color.screen_front,
                )
            ),
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
        ) {
            RowWithIcon(
                drawableId = R.drawable.icon_calendar,
                text = getIntakeText(packageModel),
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
fun AddPackageItem(height: Dp) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(128.dp)
            .height(height),
        border = BorderStroke(1.dp, colorResource(id = R.color.main_black_and_white)),
        colors = CardDefaults.cardColors(
            containerColor = Color(
                ContextCompat.getColor(
                    LocalContext.current,
                    R.color.screen_front,
                )
            ),
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.clickable { /*todo переход на фрагмент добавления */ },
                painter = painterResource(id = R.drawable.button_icon_plus),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun RowWithIcon(drawableId: Int, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "",
        )
        Text(text = text, modifier = Modifier.padding(4.dp))
    }
}

@Composable
fun getIntakeText(packageModel: PackageItemModel): String {
    return if (packageModel.intakesCount == -1) stringResource(id = R.string.intakes_count_is_not_defined)
    else "${packageModel.intakesCount} " + stringResource(id = R.string.intakes).padStart(
        0
    )
}

@Composable
@SuppressLint("SimpleDateFormat")
private fun dateToString(time: Long) =
    if (time == 0L) {
        stringResource(id = R.string.package_end_date_is_not_defined)
    } else {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val date = Date(time)
        formatter.format(date)
    }


@Preview(showBackground = true)
@Composable
fun GeneralPreview() {
    Row {
        PackageItem(
            packageModel = PackageItemModel(
                id = "101",
                intakesCount = 90,
                endDate = 1689333793000,
                expirationDate = 1694690593000,
            ),
        )
        AddPackageItem(96.dp)
    }
}
