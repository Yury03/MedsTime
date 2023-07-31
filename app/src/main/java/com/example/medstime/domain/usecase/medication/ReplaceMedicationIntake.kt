package com.example.medstime.domain.usecase.medication

import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationIntakeModel

class ReplaceMedicationIntake(private val repository: Repository.MedicationContract) {
    fun invoke(medicationIntakeModel: MedicationIntakeModel){
        repository.replaceMedicationIntake(medicationIntakeModel)
    }
}