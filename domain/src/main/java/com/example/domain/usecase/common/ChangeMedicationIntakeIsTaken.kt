package com.example.domain.usecase.common

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel


class ChangeMedicationIntakeIsTaken(private val repository: Repository.CommonContract) {
    fun invoke(
        medicationIntakeId: String,
        newIsTaken: Boolean,
        actualIntakeTime: MedicationIntakeModel.Time?
    ) {
        repository.changeMedicationIntakeIsTaken(medicationIntakeId, newIsTaken, actualIntakeTime)
    }
}