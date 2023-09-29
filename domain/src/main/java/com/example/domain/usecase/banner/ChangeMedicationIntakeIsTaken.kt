package com.example.domain.usecase.banner

import com.example.domain.Repository

class ChangeMedicationIntakeIsTaken(private val repository: Repository.BannerContract) {
    fun invoke(medicationIntakeId: String, newIsTaken: Boolean) {
        repository.changeMedicationIntakeIsTaken(medicationIntakeId, newIsTaken)
    }
}