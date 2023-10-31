package com.example.domain

import com.example.domain.models.MedicationIntakeModel
import com.example.domain.models.MedicationModel
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.ReminderModel
import kotlinx.coroutines.flow.Flow

interface Repository {
    interface MedicationIntakeContract {
        suspend fun getIntakeList(): Flow<List<MedicationIntakeModel>>
        suspend fun changeActualTimeIntake(
            medicationIntakeId: String,
            newTime: MedicationIntakeModel.Time
        )

        suspend fun changeMedicationIntakeIsTaken(
            medicationIntakeId: String,
            newIsTaken: Boolean,
            actualIntakeTime: MedicationIntakeModel.Time?
        )

        suspend fun getMedicationIntakeModel(medicationIntakeId: String): MedicationIntakeModel
    }

    interface ReminderContract {
        suspend fun getReminderModelById(reminderId: String): ReminderModel
        suspend fun getRemindersWithStatus(reminderStatus: ReminderModel.Status): List<ReminderModel>
        suspend fun changeNotificationStatusByReminderId(
            reminderId: String,
            newStatus: ReminderModel.Status
        )

        suspend fun changeNotificationStatusByMedicationIntakeId(
            medicationIntakeId: String,
            newStatus: ReminderModel.Status,
        )
    }

    interface CommonContract {
        suspend fun saveNewMedication(medicationModel: MedicationModel)
        suspend fun removeMedicationModel(medicationModelId: String)
        suspend fun replaceMedicationModel(medicationModel: MedicationModel)
        suspend fun getMedicationById(id: String): MedicationModel
    }

    interface MedsTrackContract {
        suspend fun getAllTracks(): List<MedsTrackModel>
    }
}