package com.example.domain.usecase.common

import com.example.domain.Repository
import com.example.domain.models.MedicationModel

class SaveNewMedication(private val repository: Repository.CommonContract) {

    suspend operator fun invoke(medicationModel: MedicationModel) {
        repository.saveNewMedication(medicationModel)
    }
}