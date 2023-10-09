package com.example.domain.usecase.medication

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel

class ChangeActualTimeIntake(private val repository: Repository.MedicationContract) {
    fun invoke(medicationIntakeId: String, newTime: MedicationIntakeModel.Time) {
        repository.changeActualTimeIntake(medicationIntakeId, newTime)
    }
}