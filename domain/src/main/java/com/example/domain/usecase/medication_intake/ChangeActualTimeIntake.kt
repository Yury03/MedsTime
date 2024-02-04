package com.example.domain.usecase.medication_intake

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel

class ChangeActualTimeIntake(private val repository: Repository.MedicationIntakeContract) {

    suspend operator fun invoke(medicationIntakeId: String, newTime: MedicationIntakeModel.Time) {
        repository.changeActualTimeIntake(medicationIntakeId, newTime)
    }
}