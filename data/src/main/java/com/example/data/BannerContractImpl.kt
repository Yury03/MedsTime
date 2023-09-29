package com.example.data

import android.content.Context
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.ReminderDatabase
import com.example.data.room.dao.MedicationIntakeDao
import com.example.data.room.dao.ReminderDao
import com.example.data.room.entity.MedicationIntakeEntity
import com.example.data.room.entity.ReminderEntity
import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class BannerContractImpl(private val context: Context) : Repository.BannerContract {
    private val reminderDatabase: ReminderDatabase by lazy {
        ReminderDatabase.getDatabase(context)
    }
    private val reminderDao: ReminderDao by lazy { reminderDatabase.reminderDao() }
    private val medicationIntakeDatabase: MedicationIntakeDatabase by lazy {
        MedicationIntakeDatabase.getDatabase(context)
    }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntakeDao() }

    override fun changeNotificationStatus(
        reminderId: String,
        newStatus: ReminderModel.Status
    ) {
        val previousModel = reminderDao.getById(reminderId)
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

    override fun changeMedicationIntakeIsTaken(medicationIntakeId: String, newIsTaken: Boolean) {
        val previousModel = medicationIntakeDao.getById(medicationIntakeId)
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
                    actualIntakeTime = actualIntakeTime,
                    actualIntakeDate = actualIntakeDate,
                    intakeType = intakeType,
                )
            )
        }
    }
}