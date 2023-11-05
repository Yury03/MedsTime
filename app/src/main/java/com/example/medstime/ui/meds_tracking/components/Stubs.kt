package com.example.medstime.ui.meds_tracking.components

import com.example.domain.models.MedicationModel
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel
import java.util.Date

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
        startDate = Date(),
        intakeType =MedicationModel.IntakeType.NONE,
        comment = "",
        useBanner = false,
        trackType = MedicationModel.TrackType.STOCK_OF_MEDICINE,
        stockOfMedicine = null,
        endDate = null,
        numberOfDays = null
    ),
    MedicationModel(
        id = "23",
        name = "Item2",
        dosage = 0.0,
        dosageUnit = "Таблетки",
        intakeTimes = listOf(),
        reminderTime = 0,
        frequency = MedicationModel.Frequency.DAILY,
        selectedDays = listOf(),
        startDate = Date(),
        intakeType =MedicationModel.IntakeType.NONE,
        comment = "",
        useBanner = false,
        trackType = MedicationModel.TrackType.NUMBER_OF_DAYS,
        stockOfMedicine = null,
        endDate = null,
        numberOfDays = 320.0,
    ),
    MedicationModel(
        id = "312",
        name = "Item3",
        dosage = 0.0,
        dosageUnit = "Шт",
        intakeTimes = listOf(),
        reminderTime = 0,
        frequency = MedicationModel.Frequency.DAILY,
        selectedDays = listOf(),
        startDate = Date(),
        intakeType =MedicationModel.IntakeType.NONE,
        comment = "",
        useBanner = false,
        trackType = MedicationModel.TrackType.NONE,
        stockOfMedicine = null,
        endDate = null,
        numberOfDays = null
    )
)
