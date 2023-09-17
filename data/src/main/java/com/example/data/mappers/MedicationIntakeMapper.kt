package com.example.data.mappers

import com.example.data.room.entity.MedicationIntakeEntity
import com.example.domain.models.MedicationIntakeModel


object MedicationIntakeMapper {

    fun mapToEntity(model: MedicationIntakeModel): MedicationIntakeEntity {
        val actualIntakeTime = model.actualIntakeTime?.let {
            Pair(it.hour, it.minute)
        }
        val actualIntakeDate = model.actualIntakeDate?.let {
            Triple(it.day, it.month, it.year)
        }
        return MedicationIntakeEntity(
            id = model.id,
            name = model.name,
            dosage = model.dosage,
            dosageUnit = model.dosageUnit,
            isTaken = model.isTaken,
            reminderTime = model.reminderTime,
            medicationId = model.medicationId,
            intakeType = model.intakeType.name,
            intakeTime = Pair(model.intakeTime.hour, model.intakeTime.minute),
            intakeDate = Triple(
                model.intakeDate.day,
                model.intakeDate.month,
                model.intakeDate.year
            ),
            actualIntakeTime = actualIntakeTime,
            actualIntakeDate = actualIntakeDate,
        )
    }

    fun mapToModel(entity: MedicationIntakeEntity): MedicationIntakeModel {
        val actualIntakeTime = entity.actualIntakeTime?.let {
            MedicationIntakeModel.Time(it.first, it.second)
        }
        val actualIntakeDate = entity.actualIntakeDate?.let {
            MedicationIntakeModel.Date(it.first, it.second, it.third)
        }
        return MedicationIntakeModel(
            id = entity.id,
            name = entity.name,
            dosage = entity.dosage,
            dosageUnit = entity.dosageUnit,
            isTaken = entity.isTaken,
            reminderTime = entity.reminderTime,
            medicationId = entity.medicationId,
            actualIntakeTime = actualIntakeTime,
            actualIntakeDate = actualIntakeDate,
            intakeType = MedicationIntakeModel.IntakeType.valueOf(entity.intakeType),
            intakeTime = MedicationIntakeModel.Time(
                entity.intakeTime.first,
                entity.intakeTime.second,
            ),
            intakeDate = MedicationIntakeModel.Date(
                entity.intakeDate.first,
                entity.intakeDate.second,
                entity.intakeDate.third,
            ),
        )
    }

}
