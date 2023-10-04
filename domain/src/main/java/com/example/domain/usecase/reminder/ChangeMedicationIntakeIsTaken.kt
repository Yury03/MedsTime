package com.example.domain.usecase.reminder

import com.example.domain.Repository
import com.example.domain.models.MedicationIntakeModel


class ChangeMedicationIntakeIsTaken(private val repository: Repository.ReminderContract) {
    fun invoke(
        medicationIntakeId: String,
        newIsTaken: Boolean,
        actualIntakeTime: MedicationIntakeModel.Time?
    ) {
        repository.changeMedicationIntakeIsTaken(medicationIntakeId, newIsTaken, actualIntakeTime)
    }
}