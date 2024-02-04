package com.example.domain.usecase.reminder

import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class ChangeNotificationStatusByReminderId(private val repository: Repository.ReminderContract) {

    suspend operator fun invoke(reminderId: String, newStatus: ReminderModel.Status) {
        repository.changeNotificationStatusByReminderId(reminderId, newStatus)
    }
}