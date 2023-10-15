package com.example.domain.usecase.medication

import com.example.domain.Repository

class RemoveMedicationModel(private val repository: Repository.MedicationContract) {
    fun invoke(medicationModelId: String) {
        return repository.removeMedicationModel(medicationModelId)
    }
}