package com.example.domain.usecase.banner

import com.example.domain.Repository

class ChangeMedicationIsTaken(private val repository: Repository.BannerContract) {
    fun invoke(isTaken: Boolean) {
        repository.changeMedicationIsTaken(isTaken)
    }
}