package com.example.data

import android.content.Context
import com.example.data.mappers.MedicationIntakeMapper
import com.example.data.mappers.ReminderMapper
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.ReminderDatabase
import com.example.data.room.dao.MedicationIntakeDao
import com.example.data.room.dao.ReminderDao
import com.example.domain.Repository
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

    override fun getReminderModelById(reminderId: String): ReminderModel {
        return ReminderMapper.mapToModel(reminderDao.getById(reminderId))
    }
}