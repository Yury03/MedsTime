package com.example.domain.usecase.medication_intake

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel
import kotlinx.coroutines.flow.Flow

class GetIntakeList(private val repository: Repository.MedicationIntakeContract) {

    suspend fun invoke(): Flow<List<MedicationIntakeModel>> {
        return repository.getIntakeList()
    }
}
