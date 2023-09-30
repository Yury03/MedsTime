package com.example.domain.usecase.reminder

import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class ChangeNotificationStatus(private val repository: Repository.ReminderContract) {
    fun invoke(reminderId: String, newStatus: ReminderModel.Status) {
        repository.changeNotificationStatus(reminderId, newStatus)
    }
}