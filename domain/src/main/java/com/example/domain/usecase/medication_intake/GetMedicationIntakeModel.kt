package com.example.domain.usecase.medication_intake

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel

class GetMedicationIntakeModel(private val repository: Repository.MedicationIntakeContract) {
    suspend fun invoke(medicationIntakeModelId: String): MedicationIntakeModel {
        return repository.getMedicationIntakeModel(medicationIntakeModelId)
    }
}