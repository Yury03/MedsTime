package com.example.domain.usecase.common

import com.example.domain.Repository
import com.example.domain.models.MedicationModel

class GetMedicationById(private val repository: Repository.CommonContract) {
    fun invoke(id: String): MedicationModel {
        return repository.getMedicationById(id)
    }
}