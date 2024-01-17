package com.example.data

import android.content.Context
import com.example.data.mappers.ReminderMapper
import com.example.data.room.ReminderDatabase
import com.example.data.room.dao.ReminderDao
import com.example.domain.Repository
import com.example.domain.models.ReminderModel

/**Класс реализует юзкейсы, связанные с напоминаниями. Используется только ReminderDatabase
 * - **get reminders with status** получает все уведомления с данным статусом;
 * - **change notification status by reminder id**  меняет статус уведомления по id модели Reminder;
 * - **change notification status by medication intake id** меняет статус уведомления по id модели MedicationIntake;
 * - **get reminder model by id** получает модель напоминания по id.*/
class ReminderContractImpl(private val context: Context) : Repository.ReminderContract {

    private val reminderDatabase: ReminderDatabase by lazy {
        ReminderDatabase.getDatabase(context)
    }
    private val reminderDao: ReminderDao by lazy { reminderDatabase.reminderDao() }

    override suspend fun getRemindersWithStatus(reminderStatus: ReminderModel.Status): List<ReminderModel> {
        return reminderDao.getWithStatus(reminderStatus.toString()).map {
            ReminderMapper.mapToModel(it)
        }
    }

    override suspend fun changeNotificationStatusByReminderId(
        reminderId: String,
        newStatus: ReminderModel.Status
    ) {
        reminderDao.updateStatusById(reminderId, newStatus.toString())
    }

    override suspend fun changeNotificationStatusByMedicationIntakeId(
        medicationIntakeId: String,
        newStatus: ReminderModel.Status
    ) {
        reminderDao.updateStatusByMedicationIntakeId(medicationIntakeId, newStatus.toString())
    }

    override suspend fun getReminderModelById(reminderId: String): ReminderModel {
        return ReminderMapper.mapToModel(reminderDao.getById(reminderId))
    }
}