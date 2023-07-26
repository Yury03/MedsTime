package com.example.medstime.data.mappers

import com.example.medstime.data.room.entity.MedicationEntity
import com.example.medstime.domain.models.MedicationModel

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
            selectedDays = model.selectedDays
        )
    }

    fun mapToModel(entity: MedicationEntity): MedicationModel {
        val intakeTimes = entity.intakeTimes.map { MedicationModel.Time(it.first, it.second) }
        val frequency = MedicationModel.Frequency.valueOf(entity.frequency)
        return MedicationModel(
            id = entity.id,
            name = entity.name,
            dosage = entity.dosage,
            dosageUnit = entity.dosageUnit,
            intakeTimes = intakeTimes,
            reminderTime = entity.reminderTime,
            frequency = frequency,
            selectedDays = entity.selectedDays
        )
    }
    /*private fun generateUniqueId(): String {
        val timestamp = System.currentTimeMillis()
        val random = (Math.random() * 1000).toInt()
        return "$timestamp$random"
    }*/
}
