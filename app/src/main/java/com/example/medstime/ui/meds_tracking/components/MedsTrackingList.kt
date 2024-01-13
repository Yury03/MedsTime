package com.example.medstime.ui.meds_tracking.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.domain.models.MedsTrackModel
import com.example.medstime.ui.compose_stubs.getListTrackingModel

@Composable
fun MedsTrackingList(trackingList: List<MedsTrackModel>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(trackingList) { position, item ->
                MedsTrackingItem(trackModel = item)
                if (position == trackingList.size - 1) {
                    AddMedsTrackingItem()
                }
            }
        }
    }
}

@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showSystemUi = true, showBackground = true)
private fun PreviewMedsTrackingList() {
    Box(modifier = Modifier.fillMaxSize()) {
        MedsTrackingList(
            trackingList = getListTrackingModel() +
                    getListTrackingModel() +
                    getListTrackingModel() +
                    getListTrackingModel() +
                    getListTrackingModel()
        )
    }
}

