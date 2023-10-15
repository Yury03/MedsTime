package com.example.domain

import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel
import com.example.domain.models.ReminderModel

interface Repository {
    interface MedicationIntakeContract {
        fun getIntakeList(): List<MedicationIntakeModel>
        fun changeActualTimeIntake(medicationIntakeId: String, newTime: MedicationIntakeModel.Time)
        fun changeMedicationIntakeIsTaken(
            medicationIntakeId: String,
            newIsTaken: Boolean,
            actualIntakeTime: MedicationIntakeModel.Time?
        )

        fun getMedicationIntakeModel(medicationIntakeId: String): MedicationIntakeModel
    }

    interface ReminderContract {
        fun getReminderModelById(reminderId: String): ReminderModel
        fun getRemindersWithStatus(reminderStatus: ReminderModel.Status): List<ReminderModel>
        fun changeNotificationStatusByReminderId(
            reminderId: String,
            newStatus: ReminderModel.Status
        )

        fun changeNotificationStatusByMedicationIntakeId(
            medicationIntakeId: String,
            newStatus: ReminderModel.Status,
        )
    }

    interface CommonContract {
        fun saveNewMedication(medicationModel: MedicationModel)
        fun removeMedicationModel(medicationModelId: String)
        fun replaceMedicationModel(medicationModel: MedicationModel)
        fun getMedicationById(id: String): MedicationModel
    }
}