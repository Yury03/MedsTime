package com.example.data.mappers

import com.example.data.room.entity.MedicationEntity
import com.example.data.room.entity.MedsTrackEntity
import com.example.domain.models.MedicationModel
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MedicationMapper {
    fun mapToEntity(model: MedicationModel): Pair<MedicationEntity, MedsTrackEntity> {
        val intakeTimes = model.intakeTimes.map { it.hour to it.minute }

        return Pair(
            MedicationEntity(
                id = model.id,
                name = model.name,
                dosage = model.dosage,
                dosageUnit = model.dosageUnit,
                intakeTimes = intakeTimes,
                reminderTime = model.reminderTime,
                frequency = model.frequency.name,
                selectedDays = model.selectedDays,
                intakeType = model.intakeType.name,
                startDate = model.startDate.toString(),//todo change String to Long
                comment = model.comment,
                useBanner = model.useBanner,
                medsTrackModelId = model.trackModel.id,
            ),
            MedsTrackEntity(
                id = model.trackModel.id,
                name = model.trackModel.name,
                endDate = model.trackModel.endDate,
                packageCounter = model.trackModel.packageItems.size,
                recommendedPurchaseDate = model.trackModel.recommendedPurchaseDate,
                packageItems = mapListToString(model.trackModel.packageItems),
                stockOfMedicine = model.trackModel.stockOfMedicine,
                numberOfDays = model.trackModel.numberOfDays,
                trackType = model.trackModel.trackType.toString()
            )
        )
    }

    fun mapToModel(
        medicationEntity: MedicationEntity,
        medsTrackEntity: MedsTrackEntity,
    ): MedicationModel {
        val intakeTimes =
            medicationEntity.intakeTimes.map { MedicationModel.Time(it.first, it.second) }
        val frequency = MedicationModel.Frequency.valueOf(medicationEntity.frequency)
        val intakeType = MedicationModel.IntakeType.valueOf(medicationEntity.intakeType)
        val packageItems = mapStringToList(medsTrackEntity.packageItems)
        return MedicationModel(
            id = medicationEntity.id,
            name = medicationEntity.name,
            dosage = medicationEntity.dosage,
            dosageUnit = medicationEntity.dosageUnit,
            intakeTimes = intakeTimes,
            reminderTime = medicationEntity.reminderTime,
            frequency = frequency,
            selectedDays = medicationEntity.selectedDays,
            intakeType = intakeType,
            startDate = medicationEntity.startDate.toLong(),
            comment = medicationEntity.comment,
            useBanner = medicationEntity.useBanner,
            trackModel = MedsTrackModel(
                id = medicationEntity.medsTrackModelId,
                name = medicationEntity.name,
                recommendedPurchaseDate = 0,
                packageItems = packageItems,
                packageCounter = packageItems.size,
                stockOfMedicine = medsTrackEntity.stockOfMedicine,
                numberOfDays = medsTrackEntity.numberOfDays,
                endDate = medsTrackEntity.endDate,
                trackType = MedsTrackModel.TrackType.valueOf(medsTrackEntity.trackType)
            ),
        )
    }

    private fun mapStringToList(list: String): MutableList<PackageItemModel> {
        val gson = Gson()
        val type = object : TypeToken<List<PackageItemModel>>() {}.type
        return gson.fromJson(list, type)
    }

    private fun mapListToString(packageItems: List<PackageItemModel>): String {
        val gson = Gson()
        return gson.toJson(packageItems)
    }
}
