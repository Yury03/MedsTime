package com.example.domain.usecase.addition

import com.example.domain.Repository
import com.example.domain.models.MedicationModel

class SaveNewMedication(private val repository: Repository.AdditionContract) {
    fun invoke(medicationModel: MedicationModel){
        repository.saveNewMedication(medicationModel)
    }
}