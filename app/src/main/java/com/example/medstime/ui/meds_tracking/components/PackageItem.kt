package com.example.medstime.ui.meds_tracking.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.domain.models.PackageItemModel

@Composable
fun PackageItem(packageItemModel: PackageItemModel) {
    Card {
        Column {
            Text(text = "")
            Text(text = "")
            Text(text = "")
            Text(text = "")
        }
    }
}

@Preview
@Composable
fun PreviewPackageItem() {
    val stub = listOf(
        PackageItemModel(
            id = "fuisset",
            idMedsTrackModel = "verterem",
            intakesCount = 3423,
            endDate = 3267,
            expirationDate = 8217
        ),
        PackageItemModel(
            id = "fuisset",
            idMedsTrackModel = "verterem",
            intakesCount = 3423,
            endDate = 3267,
            expirationDate = 8217
        ),
    )
    LazyRow {
        itemsIndexed(stub) { _, item ->
            PackageItem(packageItemModel = item)
        }
    }

}