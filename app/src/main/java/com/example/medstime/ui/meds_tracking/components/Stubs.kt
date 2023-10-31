package com.example.medstime.ui.meds_tracking.components

import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel

fun getListTrackingModel() =
    listOf(
        MedsTrackModel(
            "111", "item 1",
            endDate = 1683844600900,
            packageCounter = 90,
            recommendedPurchaseDate = 1683963600900,
            packageItems = listOf(
                PackageItemModel(
                    id = "",
                    idMedsTrackModel = "",
                    intakesCount = 0,
                    endDate = 0,
                    expirationDate = 0
                )
            ),
        ),
        MedsTrackModel(
            "111", "item 2",
            endDate = 1683844600900,
            packageCounter = 24,
            recommendedPurchaseDate = 1683963600900,
            packageItems = getListPackageModel(),
        ),
    )


fun getListPackageModel() = listOf(
    PackageItemModel(
        id = "fuisset",
        idMedsTrackModel = "verterem",
        intakesCount = 343,
        endDate = 1683963600900,
        expirationDate = 1683963600900
    ),
    PackageItemModel(
        id = "fuisset",
        idMedsTrackModel = "verterem",
        intakesCount = 33,
        endDate = 1683963600900,
        expirationDate = 1683963600900
    ),
    PackageItemModel(
        id = "fuisset",
        idMedsTrackModel = "verterem",
        intakesCount = 33,
        endDate = 1683963600900,
        expirationDate = 1683963600900
    ),
    PackageItemModel(
        id = "fuisset",
        idMedsTrackModel = "verterem",
        intakesCount = 33,
        endDate = 1683963600900,
        expirationDate = 1683963600900
    ),
)
