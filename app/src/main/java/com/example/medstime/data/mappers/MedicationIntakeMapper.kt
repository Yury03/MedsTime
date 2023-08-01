package com.example.medstime.data.mappers

import com.example.medstime.data.room.entity.MedicationIntakeEntity
import com.example.medstime.domain.models.MedicationIntakeModel

object MedicationIntakeMapper {

    fun mapToEntity(model: MedicationIntakeModel): MedicationIntakeEntity {
        val actualIntakeTime = model.actualIntakeTime?.let {
            Pair(it.hour, it.minute)
        }
        val actualIntakeDate = model.actualIntakeDate?.let {
            Pair(it.day, it.month)
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
            intakeDate = Pair(model.intakeDate.day, model.intakeDate.month),
            actualIntakeTime = actualIntakeTime,
            actualIntakeDate = actualIntakeDate,
        )
    }

    fun mapToModel(entity: MedicationIntakeEntity): MedicationIntakeModel {
        val actualIntakeTime = entity.actualIntakeTime?.let {
            MedicationIntakeModel.Time(it.first, it.second)
        }
        val actualIntakeDate = entity.actualIntakeDate?.let {
            MedicationIntakeModel.Date(it.first, it.second)
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
            ),
        )
    }

}
