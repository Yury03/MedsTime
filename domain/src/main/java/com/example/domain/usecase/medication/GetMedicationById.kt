package com.example.domain.usecase.medication

import com.example.domain.Repository
import com.example.domain.models.MedicationModel

class GetMedicationById(private val repository: Repository.MedicationContract) {
    fun invoke(id: String): MedicationModel {
        return repository.getMedicationById(id)
    }
}