package com.example.data

import android.content.Context
import com.example.data.room.MedicationIntakeDatabase
import com.example.data.room.ReminderDatabase
import com.example.data.room.dao.MedicationIntakeDao
import com.example.data.room.dao.ReminderDao
import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.ReminderModel

class CommonContractImpl(private val context: Context) : Repository.CommonContract {
    private val reminderDatabase: ReminderDatabase by lazy {
        ReminderDatabase.getDatabase(context)
    }
    private val medicationIntakeDatabase: MedicationIntakeDatabase by lazy {
        MedicationIntakeDatabase.getDatabase(context)
    }
    private val reminderDao: ReminderDao by lazy { reminderDatabase.reminderDao() }
    private val medicationIntakeDao: MedicationIntakeDao by lazy { medicationIntakeDatabase.medicationIntakeDao() }

    //TODO вынести в два разных usecase
    override fun changeNotificationStatus(
        reminderId: String, newStatus: ReminderModel.Status
    ) {
        reminderDao.updateStatusById(reminderId, newStatus.toString())
    }

    //TODO вынести в два разных usecase
    override fun changeNotificationStatus(
        newStatus: ReminderModel.Status,
        medicationIntakeId: String
    ) {
        reminderDao.updateStatusByMedicationIntakeId(medicationIntakeId, newStatus.toString())
    }

    override fun changeMedicationIntakeIsTaken(
        medicationIntakeId: String,
        newIsTaken: Boolean,
        actualIntakeTime: MedicationIntakeModel.Time?
    ) {
        medicationIntakeDao.updateIsTakenById(
            medicationIntakeId,
            newIsTaken,
            actualIntakeTime?.toEntityString()
        )
    }
}