package com.example.medstime.domain.usecase.medication

import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationModel

class GetMedicationById(private val repository: Repository.MedicationContract) {
    fun invoke(id: String): MedicationModel {
        return repository.getMedicationById(id)
    }
}