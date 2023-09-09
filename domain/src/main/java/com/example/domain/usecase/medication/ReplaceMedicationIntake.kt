package com.example.domain.usecase.medication

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel

class ReplaceMedicationIntake(private val repository: Repository.MedicationContract) {
    fun invoke(medicationIntakeModel: MedicationIntakeModel){
        repository.replaceMedicationIntake(medicationIntakeModel)
    }
}