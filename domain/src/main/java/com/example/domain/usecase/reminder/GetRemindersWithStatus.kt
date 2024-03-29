package com.example.domain.usecase.reminder

import com.example.domain.Repository
import com.example.domain.models.ReminderModel

class GetRemindersWithStatus(private val repository: Repository.ReminderContract) {

    suspend operator fun invoke(reminderStatus: ReminderModel.Status): List<ReminderModel> {
        return repository.getRemindersWithStatus(reminderStatus)
    }
}