package com.example.medstime.ui.meds_tracking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.domain.models.MedsTrackModel
import com.example.medstime.R

@Composable
fun PackageList(
    verticalPaddingBox: Dp,
    height: Dp,
    trackModel: MedsTrackModel,
    showAddItem: Boolean = true,
    showBackground: Boolean = true,
) {
    Box(
        modifier = Modifier
            .background(
                if (showBackground)
                    colorResource(id = R.color.light_grey_background)
                else
                    colorResource(id = R.color.local_transparent)
            )
            .padding(vertical = verticalPaddingBox)
            .height(height)
            .fillMaxWidth(),
        contentAlignment = Alignment.TopStart,
    ) {
        LazyRow {
            itemsIndexed(trackModel.packageItems) { position, item ->
                PackageItem(packageModel = item)
                if (position == trackModel.packageItems.size - 1 && showAddItem)
                    AddPackageItem(height = 96.dp)
            }
        }
    }
}


@Preview
@Composable
private fun PreviewPackageList() {
    PackageList(
        verticalPaddingBox = 8.dp,
        height = 110.dp,
        trackModel = getListTrackingModel()[0]
    )
}