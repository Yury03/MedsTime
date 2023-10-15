package com.example.domain.usecase.medication_intake

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel

class GetIntakeList(private val repository: Repository.MedicationIntakeContract) {
    fun invoke(): List<MedicationIntakeModel> {
        return repository.getIntakeList()
    }
}
