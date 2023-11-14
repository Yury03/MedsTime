package com.example.medstime.ui.add_track

import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel

data class AddMedTrackState(
    val medName: String,
    val medTrack: MedsTrackModel?,
    val medsTrackId: String?,
    val actualPackageList: List<PackageItemModel>,
    val expirationDate: String,
    val dosageUnit: String,
    val quantityInPackage: String,
    val errorCode: Int,
) {
    companion object ERROR {
        const val VALID = 0
        const val EXPIRATION_DATE = 1
        const val QUANTITY_IN_PACKAGE = 2
    }
}