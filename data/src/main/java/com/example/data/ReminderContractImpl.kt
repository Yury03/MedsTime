package com.example.data

import android.content.Context
import com.example.data.mappers.MedicationIntakeMapper
import com.example.data.mappers.ReminderMapper
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.ReminderDatabase
import com.example.data.room.dao.MedicationIntakeDao
import com.example.data.room.dao.ReminderDao
import com.example.data.room.entity.MedicationIntakeEntity
import com.example.data.room.entity.ReminderEntity
import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.ReminderModel

class ReminderContractImpl(private val context: Context) : Repository.ReminderContract {
    private val reminderDatabase: ReminderDatabase by lazy {
        ReminderDatabase.getDatabase(context)
    }
    private val medicationIntakeDatabase: MedicationIntakeDatabase by lazy {
        MedicationIntakeDatabase.getDatabase(context)
    }
    private val reminderDao: ReminderDao by lazy { reminderDatabase.reminderDao() }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntakeDao() }
    override fun getRemindersWithStatus(reminderStatus: ReminderModel.Status): List<ReminderModel> {
        return reminderDao.getWithStatus(reminderStatus.toString()).map {
            ReminderMapper.mapToModel(it)
        }
    }

    override fun getMedicationIntakeModel(medicationIntakeId: String) =
        MedicationIntakeMapper.mapToModel(medicationIntakeDao.getById(medicationIntakeId))

    override fun changeNotificationStatus(
        reminderId: String,
        newStatus: ReminderModel.Status
    ) {
        val previousModel = reminderDao.getById(reminderId)
        reminderDao.deleteById(reminderId)
        with(previousModel) {
            reminderDao.insert(
                ReminderEntity(
                    id = id,
                    medicationIntakeId = medicationIntakeId,
                    type = type,
                    status = newStatus.toString(),
                    timeShow = timeShow,
                )
            )
        }
    }

    override fun changeMedicationIntakeIsTaken(
        medicationIntakeId: String,
        newIsTaken: Boolean,
        actualIntakeTime: MedicationIntakeModel.Time?
    ) {
        val previousModel = medicationIntakeDao.getById(medicationIntakeId)
        medicationIntakeDao.deleteById(medicationIntakeId)
        val actualIntakeTimePair = if (actualIntakeTime != null) {
            Pair(actualIntakeTime.hour, actualIntakeTime.minute)
        } else {
            null
        }
//            actualIntakeTime?:Pair<Int, Int>?(actualIntakeTime?.hour, actualIntakeTime?.minute)
        with(previousModel) {
            medicationIntakeDao.insert(
                MedicationIntakeEntity(
                    id = id,
                    name = name,
                    dosage = dosage,
                    dosageUnit = dosageUnit,
                    isTaken = newIsTaken,
                    reminderTime = reminderTime,
                    medicationId = medicationId,
                    intakeTime = intakeTime,
                    intakeDate = intakeDate,
                    actualIntakeTime = actualIntakeTimePair,
                    actualIntakeDate = actualIntakeDate,
                    intakeType = intakeType,
                )
            )
        }
    }

    override fun getReminderModelById(reminderId: String): ReminderModel {
        return ReminderMapper.mapToModel(reminderDao.getById(reminderId))
    }
}