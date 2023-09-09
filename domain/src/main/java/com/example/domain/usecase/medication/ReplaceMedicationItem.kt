package com.example.domain.usecase.medication

import com.example.domain.Repository
import com.example.domain.models.MedicationModel

class ReplaceMedicationItem(private val repository: Repository.MedicationContract) {
    fun invoke(medicationModel: MedicationModel) {
        return repository.replaceMedicationItem(medicationModel)
    }
}