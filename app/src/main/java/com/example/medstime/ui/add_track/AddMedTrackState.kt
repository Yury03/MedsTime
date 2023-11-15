package com.example.medstime.ui.add_track

import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel

data class AddMedTrackState(
    val medName: String = "",
    val medTrack: MedsTrackModel? = null,
    val medsTrackId: String? = null,
    val actualPackageList: List<PackageItemModel> = emptyList(),
    val expirationDate: String = "",
    val dosageUnit: String = "",
    val quantityInPackage: String = "",
    val errorCode: Int = VALID,
) {
    companion object ERROR {
        const val VALID = 0
        const val EXPIRATION_DATE = 1
        const val QUANTITY_IN_PACKAGE = 2
    }
}