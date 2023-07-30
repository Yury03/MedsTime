package com.example.medstime.data.mappers

import com.example.medstime.data.room.entity.MedicationEntity
import com.example.medstime.domain.models.MedicationModel
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
            endDate = dateToString(model.endDate)
        )
    }

    fun mapToModel(entity: MedicationEntity): MedicationModel {
        val intakeTimes = entity.intakeTimes.map { MedicationModel.Time(it.first, it.second) }
        val frequency = MedicationModel.Frequency.valueOf(entity.frequency)
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
            endDate = endDate
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
    /*private fun generateUniqueId(): String {
        val timestamp = System.currentTimeMillis()
        val random = (Math.random() * 1000).toInt()
        return "$timestamp$random"
    }*/
}
