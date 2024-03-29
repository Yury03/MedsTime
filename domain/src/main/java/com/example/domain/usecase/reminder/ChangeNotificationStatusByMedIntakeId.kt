package com.example.domain.usecase.reminder

import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class ChangeNotificationStatusByMedIntakeId(private val repository: Repository.ReminderContract) {

    suspend operator fun invoke(medicationIntakeId: String, newStatus: ReminderModel.Status) {
        repository.changeNotificationStatusByMedicationIntakeId(medicationIntakeId, newStatus)
    }
}