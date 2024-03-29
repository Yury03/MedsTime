package com.example.data.mappers

import com.example.data.room.entity.ReminderEntity
import com.example.domain.models.ReminderModel

object ReminderMapper {

    fun mapToEntity(model: ReminderModel): ReminderEntity {
        return ReminderEntity(
            id = model.id,
            medicationIntakeId = model.medicationIntakeId,
            type = model.type.toString(),
            status = model.status.toString(),
            timeShow = model.timeShow.toString(),
        )
    }

    fun mapToModel(entity: ReminderEntity): ReminderModel {
        return ReminderModel(
            id = entity.id,
            medicationIntakeId = entity.medicationIntakeId,
            type = ReminderModel.Type.valueOf(entity.type),
            status = ReminderModel.Status.valueOf(entity.status),
            timeShow = entity.timeShow.toLong()
        )
    }
}