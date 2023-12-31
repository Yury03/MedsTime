package com.example.domain.usecase.common

import com.example.domain.Repository
import com.example.domain.models.MedicationModel

class ReplaceMedicationModel(private val repository: Repository.CommonContract) {
    suspend fun invoke(medicationModel: MedicationModel) {
        return repository.replaceMedicationModel(medicationModel)
    }
}