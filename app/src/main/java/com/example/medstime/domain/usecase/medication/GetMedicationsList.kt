package com.example.medstime.domain.usecase.medication

import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationModel

class GetMedicationsList(private val repository: Repository.MedicationContract) {
    fun invoke(): List<MedicationModel> {
        return repository.getMedicationsList()
    }
}
