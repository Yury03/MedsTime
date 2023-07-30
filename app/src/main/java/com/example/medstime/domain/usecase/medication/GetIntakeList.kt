package com.example.medstime.domain.usecase.medication

import com.example.medstime.domain.Repository
import com.example.medstime.domain.models.MedicationIntakeModel
import com.example.medstime.domain.models.MedicationModel

class GetIntakeList(private val repository: Repository.MedicationContract) {
    fun invoke(): List<MedicationIntakeModel> {
        return repository.getIntakeList()
    }
}
