package com.example.domain.usecase.common

import com.example.domain.Repository

class RemoveMedicationModel(private val repository: Repository.CommonContract) {

    suspend operator fun invoke(medicationModelId: String) {
        return repository.removeMedicationModel(medicationModelId)
    }
}