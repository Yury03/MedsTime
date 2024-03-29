package com.example.medstime.ui.compose_stubs

import com.example.domain.models.MedicationModel
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel
import java.util.Date

fun getListTrackingModel() = listOf(
    MedsTrackModel(
        "111", "item 1",
        endDate = 1683844600900,
        packageCounter = 90,
        recommendedPurchaseDate = 1683963600900,
        packageItems = getListPackageModelTracking()
    ),
    MedsTrackModel(
        "111", "item 2",
        endDate = 1683844600900,
        packageCounter = 24,
        recommendedPurchaseDate = 1683963600900,
        packageItems = getListEditPackageModel(),
    ),
)

fun getListPackageModelTracking() = mutableListOf(
    PackageItemModel(
        id = "23480109",
        intakesCount = 343,
        endDate = 1683963600900,
        expirationDate = 1683963600900,
        durationInDays = 34,
        startDate = 1683962690900,
        quantityInPackage = 704.0,
    ),
    PackageItemModel(
        id = "23410029",
        intakesCount = 33,
        endDate = 1683963600900,
        expirationDate = 1683963600900,
        durationInDays = 34,
        startDate = 1683962690900,
        quantityInPackage = 704.0,
    ),
    PackageItemModel(
        id = "21048029",
        intakesCount = 33,
        endDate = 1683963600900,
        expirationDate = 1683963600900,
        durationInDays = 34,
        startDate = 1683962690900,
        quantityInPackage = 704.0,
    ),
    PackageItemModel(
        id = "10348029",
        intakesCount = 33,
        endDate = 1683963600900,
        expirationDate = 1683963600900,
        durationInDays = 34,
        startDate = 1683962690900,
        quantityInPackage = 704.0,
    ),
)


fun getListMedicationModel() = listOf(
    MedicationModel(
        id = "123",
        name = "Item1",
        dosage = 0.0,
        dosageUnit = "Мг",
        intakeTimes = listOf(),
        reminderTime = 0,
        frequency = MedicationModel.Frequency.DAILY,
        selectedDays = listOf(),
        startDate = Date().time,
        intakeType = MedicationModel.IntakeType.NONE,
        comment = "",
        useBanner = false,
        trackModel = MedsTrackModel(
            trackType = MedsTrackModel.TrackType.STOCK_OF_MEDICINE,
            stockOfMedicine = 105.5,
        ),
    ), MedicationModel(
        id = "23",
        name = "Item2",
        dosage = 0.0,
        dosageUnit = "Таблетки",
        intakeTimes = listOf(),
        reminderTime = 0,
        frequency = MedicationModel.Frequency.DAILY,
        selectedDays = listOf(),
        startDate = Date().time,
        intakeType = MedicationModel.IntakeType.NONE,
        comment = "",
        useBanner = false,
        trackModel = MedsTrackModel(
            trackType = MedsTrackModel.TrackType.NUMBER_OF_DAYS,
            numberOfDays = 320
        ),
    ), MedicationModel(
        id = "312",
        name = "Item3",
        dosage = 0.0,
        dosageUnit = "Шт",
        intakeTimes = listOf(),
        reminderTime = 0,
        frequency = MedicationModel.Frequency.DAILY,
        selectedDays = listOf(),
        startDate = Date().time,
        intakeType = MedicationModel.IntakeType.NONE,
        comment = "",
        useBanner = false,
        trackModel = MedsTrackModel(
            trackType = MedsTrackModel.TrackType.NONE,
        ),
    )
)

private fun getListEditPackageModel() =
    mutableListOf(
        PackageItemModel(
            id = "443943924",
            intakesCount = -1,
            endDate = 0,
            expirationDate = 1683963600900,
            durationInDays = 34,
            startDate = 1683962690900,
            quantityInPackage = 704.0,
        ),
        PackageItemModel(
            id = "443942124",
            intakesCount = -1,
            endDate = 0,
            expirationDate = 1683963600900,
            durationInDays = 34,
            startDate = 1683962690900,
            quantityInPackage = 704.0,
        ),
    )
