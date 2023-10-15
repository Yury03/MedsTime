package com.example.domain

import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel
import com.example.domain.models.ReminderModel

interface Repository {
    //todo поделить по бд
    interface MedicationContract {
        fun getIntakeList(): List<MedicationIntakeModel>
        fun getMedicationById(id: String): MedicationModel
        fun changeActualTimeIntake(medicationIntakeId: String, newTime: MedicationIntakeModel.Time)
    }

    interface ReminderContract {
        fun getRemindersWithStatus(reminderStatus: ReminderModel.Status): List<ReminderModel>
        fun getMedicationIntakeModel(medicationIntakeId: String): MedicationIntakeModel
        fun getReminderModelById(reminderId: String): ReminderModel
    }

    interface CommonContract {
        fun changeMedicationIntakeIsTaken(
            medicationIntakeId: String,
            newIsTaken: Boolean,
            actualIntakeTime: MedicationIntakeModel.Time?
        )

        fun changeNotificationStatus(
            reminderId: String,
            newStatus: ReminderModel.Status
        )//todo переименовать

        fun changeNotificationStatus(
            newStatus: ReminderModel.Status,
            medicationIntakeId: String
        )//todo переименовать

        fun replaceMedicationModel(medicationModel: MedicationModel)
        fun saveNewMedication(medicationModel: MedicationModel)
        fun removeMedicationModel(medicationModelId: String)
    }
}