package com.example.domain.usecase.medication

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel

class GetIntakeList(private val repository: Repository.MedicationContract) {
    fun invoke(): List<MedicationIntakeModel> {
        return repository.getIntakeList()
    }
}
