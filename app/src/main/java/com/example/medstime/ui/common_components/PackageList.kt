package com.example.medstime.ui.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.domain.models.PackageItemModel
import com.example.medstime.R
import com.example.medstime.ui.compose_stubs.getListTrackingModel

@Composable
fun PackageList(
    verticalPaddingBox: Dp,
    height: Dp,
    packageItems: List<PackageItemModel>,
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
            itemsIndexed(packageItems) { position, item ->
                PackageItem(packageModel = item)
                if (position == packageItems.size - 1 && showAddItem)
                    AddPackageItem(height = 96.dp)
            }
        }

    }
}


@Preview
@Composable
private fun PreviewPackageList() {
    Column {
        PackageList(
            verticalPaddingBox = 8.dp,
            height = 110.dp,
            packageItems = getListTrackingModel()[0].packageItems,
        )
        PackageList(
            verticalPaddingBox = 8.dp,
            height = 110.dp,
            packageItems = getListTrackingModel()[1].packageItems,
            showAddItem = false,
        )
    }
}