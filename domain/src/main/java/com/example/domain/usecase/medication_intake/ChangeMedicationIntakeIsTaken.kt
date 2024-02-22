package com.example.domain.usecase.medication_intake

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel


class ChangeMedicationIntakeIsTaken(private val repository: Repository.MedicationIntakeContract) {

    suspend operator fun invoke(
        medicationIntakeId: String,
        newIsTaken: Boolean,
        actualIntakeTime: MedicationIntakeModel.Time?
    ) {
        repository.changeMedicationIntakeIsTaken(medicationIntakeId, newIsTaken, actualIntakeTime)
    }
}