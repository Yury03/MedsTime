package com.example.domain.usecase.reminder

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel

class GetMedicationIntakeModel(private val repository: Repository.ReminderContract) {
    fun invoke(medicationIntakeModelId: String): MedicationIntakeModel {
        return repository.getMedicationIntakeModel(medicationIntakeModelId)
    }
}