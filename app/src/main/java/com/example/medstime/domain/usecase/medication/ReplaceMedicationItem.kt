package com.example.medstime.domain.usecase.medication

import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationModel

class ReplaceMedicationItem(private val repository: Repository.MedicationContract) {
    fun invoke(medicationModel: MedicationModel) {
        return repository.replaceMedicationItem(medicationModel)
    }
}