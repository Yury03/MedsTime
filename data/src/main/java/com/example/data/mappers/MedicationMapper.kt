package com.example.data.mappers

import com.example.data.room.entity.MedicationEntity
import com.example.domain.models.MedicationModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MedicationMapper {
    fun mapToEntity(model: MedicationModel): MedicationEntity {
        val intakeTimes = model.intakeTimes.map { it.hour to it.minute }
        return MedicationEntity(
            id = model.id,
            name = model.name,
            dosage = model.dosage,
            dosageUnit = model.dosageUnit,
            intakeTimes = intakeTimes,
            reminderTime = model.reminderTime,
            frequency = model.frequency.name,
            selectedDays = model.selectedDays,
            intakeType = model.intakeType.name,
            startDate = dateToString(model.startDate)!!,
            endDate = dateToString(model.endDate),
            comment = model.comment,
            useBanner = model.useBanner,
            trackType = model.trackType.name,
            stockOfMedicine = model.stockOfMedicine,
            numberOfDays = model.numberOfDays,
        )
    }

    fun mapToModel(entity: MedicationEntity): MedicationModel {
        val intakeTimes = entity.intakeTimes.map { MedicationModel.Time(it.first, it.second) }
        val frequency = MedicationModel.Frequency.valueOf(entity.frequency)
        val trackType = MedicationModel.TrackType.valueOf(entity.trackType)
        val intakeType = MedicationModel.IntakeType.valueOf(entity.intakeType)
        val startDate = stringToDate(entity.startDate)!!
        val endDate = stringToDate(entity.endDate)
        return MedicationModel(
            id = entity.id,
            name = entity.name,
            dosage = entity.dosage,
            dosageUnit = entity.dosageUnit,
            intakeTimes = intakeTimes,
            reminderTime = entity.reminderTime,
            frequency = frequency,
            selectedDays = entity.selectedDays,
            intakeType = intakeType,
            startDate = startDate,
            endDate = endDate,
            comment = entity.comment,
            useBanner = entity.useBanner,
            trackType = trackType,
            stockOfMedicine = entity.stockOfMedicine,
            numberOfDays = entity.numberOfDays,
        )
    }

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private fun stringToDate(dateString: String?): Date? {
        return dateString?.let {
            try {
                dateFormatter.parse(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun dateToString(date: Date?): String? {
        return date?.let {
            dateFormatter.format(it)
        }
    }
}
