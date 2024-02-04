package com.example.domain.usecase.reminder

import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class GetReminderModelById(private val repository: Repository.ReminderContract) {

    suspend operator fun invoke(reminderId: String): ReminderModel {
        return repository.getReminderModelById(reminderId)
    }
}