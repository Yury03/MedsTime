package com.example.domain

import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel
import com.example.domain.models.ReminderModel

interface Repository {
    interface MedicationContract {
        fun getIntakeList(): List<MedicationIntakeModel>
        fun replaceMedicationItem(medicationModel: MedicationModel)
        fun removeMedicationItem(medicationModel: MedicationModel)
        fun getMedicationById(id: String): MedicationModel
        fun replaceMedicationIntake(medicationIntakeModel: MedicationIntakeModel)
    }

    interface AdditionContract {
        fun saveNewMedication(medicationModel: MedicationModel)
    }

    interface ReminderContract {
        fun getRemindersWithStatus(reminderStatus: ReminderModel.Status): List<ReminderModel>
        fun getMedicationIntakeModel(medicationIntakeId: String): MedicationIntakeModel
        fun changeNotificationStatus(reminderId: String, newStatus: ReminderModel.Status)
        fun changeMedicationIntakeIsTaken(
            medicationIntakeId: String,
            newIsTaken: Boolean,
            actualIntakeTime: MedicationIntakeModel.Time?
        )

        fun getReminderModelById(reminderId: String): ReminderModel
    }

}