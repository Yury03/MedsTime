package com.example.domain.usecase.common

import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class ChangeNotificationStatus(private val repository: Repository.CommonContract) {
    fun invoke(reminderId: String, newStatus: ReminderModel.Status) {
        repository.changeNotificationStatus(reminderId, newStatus)
    }

    fun invoke(newStatus: ReminderModel.Status, medicationIntakeId: String) {
        repository.changeNotificationStatus(newStatus, medicationIntakeId)
    }
}